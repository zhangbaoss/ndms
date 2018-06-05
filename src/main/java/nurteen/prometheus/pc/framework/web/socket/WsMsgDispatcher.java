package nurteen.prometheus.pc.framework.web.socket;

import nurteen.prometheus.pc.framework.Constants;
import nurteen.prometheus.pc.framework.RedisAware;
import nurteen.prometheus.pc.framework.utils.JsonUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WsMsgDispatcher {
    static Map<String, MsgHandler> msgHandlers = new HashMap<>(); // url -> handler
    static Map<String, Map<String, WsServer>> peers = new HashMap<>(); // peerType -> sessionId -> server
    static Map<String, WsServer> ndids = new HashMap<>();    // ndid -> server
    static Long _sequence = 1L;

    static {
        peers.put(WsAppMobileServer.PEER_TYPE, new HashMap<String, WsServer>());
    }


    public static void addOnOpenHandler(Object bean, Method method) {
        addMessageHandler(Constants.WSMSGHANDLER_ONOPEN_URL, bean, method);
    }
    public static void addOnCloseHandler(Object bean, Method method) {
        addMessageHandler(Constants.WSMSGHANDLER_ONCLOSE_URL, bean, method);
    }
    public static void addMessageHandler(String url, Object bean, Method method) {
        msgHandlers.put(url, new MsgHandler(url, bean, method));
    }

    public static void request(String url, Object object) {
        synchronized (peers) {
            for (Map.Entry<String, WsServer> entry: ndids.entrySet()) {
                request(entry.getValue(), url, object);
            }
        }
    }
    public static void request(String ndid, String url, Object object) {
        synchronized (peers) {
            WsServer wsServer = ndids.get(ndid);
            if (wsServer != null) {
                request(wsServer, url, object);
            }
        }
    }
    public static void request(WsServer wsServer, String url, Object object) {
        Msg msg;
        if (!object.getClass().isAssignableFrom(String.class)) {
            msg = new Msg(genMsgId(), url, Msg.Type.Request, Msg.PayloadType.StringObject, JsonUtils.toJSON(object));
        }
        else {
            msg = new Msg(genMsgId(), url, Msg.Type.Request, Msg.PayloadType.StringObject, (String) object);
        }

        String txtMsg = JsonUtils.toJSON(msg);
        wsServer.sendText(txtMsg);
    }
    public static void reply(WsServer wsServer, Msg msg) {
        reply(wsServer, msg, "");
    }
    public static void reply(WsServer wsServer, Msg msg, Object reps) {
        Msg repMsg;
        if (!reps.getClass().isAssignableFrom(String.class)) {
            repMsg = new Msg(msg.msgId, msg.url, Msg.Type.Reply, Msg.PayloadType.JsonObject, JsonUtils.toJSON(reps));
        }
        else {
            repMsg = new Msg(msg.msgId, msg.url, Msg.Type.Reply, Msg.PayloadType.StringObject, (String) reps);
        }

        String txtMsg = JsonUtils.toJSON(repMsg);
        wsServer.sendText(txtMsg);
    }

    // "/app/mobile?nuid=gggg&ndid=ggg&access-token=xxx&secret-key=gggg"
    static void onOpen(WsServer wsServer) {
        String nuid = getParameter(wsServer, Constants.NUID),
               ndid = getParameter(wsServer, Constants.NDID),
               accessToken = getParameter(wsServer, Constants.ACCESS_TOKEN),
               secretKey = getParameter(wsServer, Constants.SECRET_KEY);
        // 检查参数

        // 检查是否已经认证
        if ((accessToken == null) || !RedisAware.getRedisAware().hasAccessToken(accessToken)) {
            wsServer.permissionDeniedClose();
            return;
        }

        wsServer.connected = true;
        wsServer.nuid = nuid;
        wsServer.ndid = ndid;
        wsServer.accessToken = accessToken;
        synchronized (peers) {
            peers.get(wsServer.getPeerType()).put(wsServer.getNdid(), wsServer);
            ndids.put(wsServer.getNdid(), wsServer);
        }

        MsgHandler msgHandler = msgHandlers.get(Constants.WSMSGHANDLER_ONOPEN_URL);
        if (msgHandler != null) {
            msgHandler.invoke(wsServer);
        }

    }
    static void onClose(WsServer wsServer) {
        if (!wsServer.connected) {
            return;
        }

        wsServer.connected = false;

        MsgHandler msgHandler = msgHandlers.get(Constants.WSMSGHANDLER_ONCLOSE_URL);
        if (msgHandler != null) {
            msgHandler.invoke(wsServer);
        }

        synchronized (peers) {
            peers.get(wsServer.getPeerType()).remove(wsServer.getNdid());
            ndids.remove(wsServer.getNdid());
        }
    }
    static void onMessage(WsServer wsServer, String message) {
        Msg msg = JsonUtils.fromJSON(message, Msg.class);
        MsgHandler msgHandler = msgHandlers.get(msg.url);
        if (msgHandler == null) {
            return;
        }

        Object reps = null;
        Class<?>[] parameterTypes = msgHandler.method.getParameterTypes();
        Type[] genericParameterTypes = msgHandler.method.getGenericParameterTypes();
        if (parameterTypes.length > 1) {
            System.out.println("WsOnMessage method parameter count too more, must only zero or one.");
        }
        else if (parameterTypes.length == 1) {
            Object arg = getInvokeParameter(wsServer, msg, parameterTypes[0], genericParameterTypes[0]);
            reps = msgHandler.invoke(arg);
        }
        else if (parameterTypes.length == 0) {
            reps = msgHandler.invoke();
        }

        if (msg.reply == Msg.Reply.MustReply) {
            if (reps != null) {
                reply(wsServer, msg, reps);
            }
            else {
                reply(wsServer, msg);
            }
        }
    }

    static String getParameter(WsServer wsServer, String name) {
        List<String> value = wsServer.session.getRequestParameterMap().get(name);
        if ((value != null) && (value.size() > 0)) {
            return value.get(0);
        }
        return null;
    }
    static Object getInvokeParameter(WsServer wsServer, Msg msg, Class<?> parameterType, Type genericParameterType) {
        if (!parameterType.isAssignableFrom(WsMsg.class)) {
            return null;
        }

        WsMsgBase wsMsg;
        try {
            switch (msg.payloadType) {
            case JsonObject:
                Class<?> payloadType = (Class<?>) ((ParameterizedType) genericParameterType).getActualTypeArguments()[0];
                wsMsg = (WsMsgBase) parameterType.getConstructors()[0].newInstance(JsonUtils.fromJSON(msg.payload, payloadType));
                break;
            case StringObject:
                wsMsg = (WsMsgBase) parameterType.getConstructors()[0].newInstance(msg.payload);
                break;
            default:
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        wsMsg._wsServer = wsServer;
        wsMsg._msg = msg;
        return wsMsg;
    }
    static Long sequence() {
        synchronized (_sequence) {
            if (_sequence == Long.MAX_VALUE) {
                _sequence = 1L;
            }
            return _sequence++;
        }
    }
    static String genMsgId() {
        return "$" + sequence();
    }


    static class Msg {
        String msgId;
        String url;
        Msg.Type type;
        Msg.Reply reply;
        Msg.PayloadType payloadType;
        String payload;

        public Msg() {

        }
        public Msg(String msgId, String url, Msg.Type type, Msg.PayloadType payloadType, String payload) {
            this.msgId = msgId;
            this.url = url;
            this.type = type;
            this.reply = null;
            this.payloadType = payloadType;
            this.payload = payload;
        }
        public Msg(String msgId, String url, Msg.Type type, Msg.Reply reply, Msg.PayloadType payloadType, String payload) {
            this.msgId = msgId;
            this.url = url;
            this.type = type;
            this.reply = reply;
            this.payloadType = payloadType;
            this.payload = payload;
        }

        public String getMsgId() {
            return msgId;
        }
        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }

        public Msg.Type getType() {
            return type;
        }
        public void setType(Msg.Type type) {
            this.type = type;
        }

        public Reply getReply() {
            return reply;
        }
        public void setReply(Reply reply) {
            this.reply = reply;
        }

        public PayloadType getPayloadType() {
            return payloadType;
        }
        public void setPayloadType(PayloadType payloadType) {
            this.payloadType = payloadType;
        }

        public String getPayload() {
            return payload;
        }
        public void setPayload(String payload) {
            this.payload = payload;
        }

        static enum Type {
            Request,
            Reply,
            ForwardRequest,
            ForwardReply
        }
        static enum Reply {
            MustReply,
            NoReply
        }
        static enum PayloadType {
            JsonObject,
            StringObject
        }
    }
    static class MsgHandler {
        String url;
        Object bean;
        Method method;

        public MsgHandler(String url, Object bean, Method method) {
            this.url = url;
            this.bean = bean;
            this.method = method;
        }

        public Object invoke(Object... args) {
            try {
                return method.invoke(bean, args);
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    static class WsMsgBase {
        WsServer _wsServer;
        WsMsgDispatcher.Msg _msg;

        public String getUrl() {
            return _msg.url;
        }
        public void reply(Object object) {
            WsMsgDispatcher.reply(_wsServer, _msg, object);
        }
    }
}
