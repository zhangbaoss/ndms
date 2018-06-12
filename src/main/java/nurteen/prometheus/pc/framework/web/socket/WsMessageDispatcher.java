package nurteen.prometheus.pc.framework.web.socket;

import nurteen.prometheus.pc.framework.ObjectFactory;
import nurteen.prometheus.pc.framework.Constants;
import nurteen.prometheus.pc.framework.Response;
import nurteen.prometheus.pc.framework.ServerProperties;
import nurteen.prometheus.pc.framework.entities.DeviceOnlineInfo;
import nurteen.prometheus.pc.framework.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WsMessageDispatcher {
    private static final String _wsencfront = "prometheus.ws.encfront";
    private static final String _wsencback = "prometheus.ws.encback";

    static ConcurrentMap<String, Handler> reqHandlers = new ConcurrentHashMap<>(); // url -> requestHandler
    static ConcurrentMap<String, ResponseHandler> respHandlers = new ConcurrentHashMap<>();    // msgId -> responseHandler
    static ConcurrentMap<String, RouteResponseHandler> routeRespHandlers = new ConcurrentHashMap<>();    // msgId -> forwardResponseHandler

    static Long _sequence = 1L;

    public static void addOnOpenHandler(Object bean, Method method) {
        addMessageHandler(Constants.WSMSGHANDLER_ONOPEN_URL, bean, method);
    }

    public static void addOnCloseHandler(Object bean, Method method) {
        addMessageHandler(Constants.WSMSGHANDLER_ONCLOSE_URL, bean, method);
    }

    public static void addMessageHandler(String url, Object bean, Method method) {
        reqHandlers.put(url, new Handler(bean, method));
    }


    /*
    public static void request(String url, String payload, long timeout, WsResponse response) {
        synchronized (peers) {
            for (Map.Entry<String, WsEndpoint> entry : ndids.entrySet()) {
                request(entry.getValue(), url, entry.getKey(), payload, timeout, response);
            }
        }
    }
    */

    public static boolean request(HttpServletRequest request, String ndid, String url, String payload, long timeout, WsResponse response) {
        WsEndpoint endpoint = fromNdid(ndid);
        if (endpoint != null) {
            return request(endpoint, url, ndid, payload, timeout, response);
        }
        return false;
    }

    public static boolean request(String ndid, String url, String payload, long timeout, WsResponse response) {
        WsEndpoint endpoint = fromNdid(ndid);
        if (endpoint != null) {
            return request(endpoint, url, ndid, payload, timeout, response);
        }
        return false;
    }

    public static boolean request(WsMessage msg, String payload, long timeout, WsResponse response) {
        return request(msg.message, payload, timeout, response);
    }

    public static boolean request(WsMessage msg, long timeout, WsResponse response) {
        return request(msg.message, timeout, response);
    }

    static boolean request(Message msg, String payload, long timeout, WsResponse response) {
        WsEndpoint endpoint = fromNdid(msg.target);
        if (endpoint != null) {
            return request(endpoint, msg, payload, timeout, response);
        }
        return false;
    }

    static boolean request(Message msg, long timeout, WsResponse response) {
        WsEndpoint endpoint = fromNdid(msg.target);
        if (endpoint != null) {
            return request(endpoint, msg, timeout, response);
        }
        return false;
    }

    static boolean request(WsEndpoint endpoint, String url, String target, String payload, long timeout, WsResponse response) {
        return _request(endpoint, Message.requestReq(url, genMsgId(), target, payload), timeout, response);
    }

    static boolean request(WsEndpoint endpoint, Message msg, String payload, long timeout, WsResponse response) {
        return _request(endpoint, Message.requestReq(msg.url, msg.msgId, msg.target, payload), timeout, response);
    }

    static boolean request(WsEndpoint endpoint, Message msg, long timeout, WsResponse response) {
        return _request(endpoint, Message.requestReq(msg.url, msg.msgId, msg.target, msg.payload), timeout, response);
    }

    static boolean _request(WsEndpoint endpoint, Message msg, long timeout, WsResponse response) {
        if (response == null) {
            return endpoint.sendMsg(msg);
        } else if (endpoint.sendMsg(msg)) {
            respHandlers.put(msg.msgId, new ResponseHandler(timeout, response));
            return true;
        }
        return false;
    }


    public static void forward(WsMessage message, String payload, long timeout, WsRouteResponse response) {
        forward(message.message, payload, timeout, response);
    }

    public static void forward(WsMessage message, long timeout, WsRouteResponse response) {
        forward(message.message, timeout, response);
    }

    static void forward(Message msg, String payload, long timeout, WsRouteResponse response) {
        WsEndpoint endpoint = fromNdid(msg.target);
        if (endpoint != null) {
            forward(endpoint, msg, payload, timeout, response);
        } else {
            response.response(new ArrayList<>(), Response.error("Target Unreachable"));
        }
    }

    static void forward(Message message, long timeout, WsRouteResponse response) {
        WsEndpoint endpoint = fromNdid(message.target);
        if (endpoint != null) {
            forward(endpoint, message, timeout, response);
        } else {
            response.response(new ArrayList<>(), Response.error("Target Unreachable"));
        }
    }

    static void forward(WsEndpoint endpoint, Message message, String payload, long timeout, WsRouteResponse response) {
        _forward(endpoint, Message.forwardReq(message.url, message.msgId, message.target, payload), timeout, response);
    }

    static void forward(WsEndpoint endpoint, Message message, long timeout, WsRouteResponse response) {
        _forward(endpoint, Message.forwardReq(message.url, message.msgId, message.target, message.payload), timeout, response);
    }

    static void _forward(WsEndpoint endpoint, Message message, long timeout, WsRouteResponse response) {
        if (response == null) {
            endpoint.sendMsg(message);
        } else if (endpoint.sendMsg(message)) {
            routeRespHandlers.put(message.msgId, new RouteResponseHandler(timeout, response));
        } else {
            response.response(new ArrayList<>(), Response.error("Send Failed"));
        }
    }

    /*
    static void onOpen(WsEndpoint endpoint) {
        Handler handler = reqHandlers.get(Constants.WSMSGHANDLER_ONOPEN_URL);
        if (handler != null) {
            handler.invoke(endpoint);
        }
    }
    static void onClose(WsEndpoint endpoint) {
        Handler handler = reqHandlers.get(Constants.WSMSGHANDLER_ONCLOSE_URL);
        if (handler != null) {
            handler.invoke(endpoint);
        }
    }
    */
    void onMessage(WsEndpoint endpoint, String message) {
        WsMessage msg = new WsMessage(endpoint, JsonUtils.fromJSON(message, Message.class));
        if (msg.message.type == null) {
            System.out.println("error message: " + message);
            endpoint.close(Response.error("error message"));
        } else if (endpoint.connected) {
            // System.out.println(String.format("message, from = %s, source = %s, target = %s, msgId = %s, url = %s, type = %s.", endpoint.ndid, msg.source, msg.target, msg.msgId, msg.url, msg.type.toString()));
            switch (msg.message.type) {
                case RequestReq: {
                    Handler handler = reqHandlers.get(msg.message.url);
                    if (handler != null) {
                        if (handler.method.getParameterTypes().length == 1) {
                            handler.invoke(msg);
                        } else {
                            handler.invoke();
                        }
                    } else if ((msg.message.target != null) && !msg.message.target.equals(ServerProperties.getNdid())) {
                        request(msg, 10000, (resp) -> msg.response(resp.getPayload()));
                    } else {

                    }
                    break;
                }
                case RequestResp: {
                    ResponseHandler handler = respHandlers.remove(msg.message.msgId);
                    if (handler != null) {
                        handler.invoke(msg);
                    }
                    break;
                }
                case RequestBatchResp:
                    break;
                case ForwardReq: {
                    Handler handler = reqHandlers.get(msg.message.url);
                    if (handler != null) {
                        if (handler.method.getParameterTypes().length == 1) {
                            handler.invoke(msg);
                        } else {
                            handler.invoke();
                        }
                    } else if ((msg.message.target != null) && !msg.message.target.equals(ServerProperties.getNdid())) {
                        forward(msg, 10000, (routes, resp) -> {
                            routes.add(ServerProperties.getNdid());
                            endpoint.sendMsg(Message.forwardResp(msg.message.url, msg.message.msgId, routes, resp));
                        });
                    } else {

                    }
                    break;
                }
                case ForwardResp: {
                    RouteResponseHandler handler = routeRespHandlers.remove(msg.message.msgId);
                    if (handler != null) {
                        Message.RouteResponse resp = msg.getPayload(Message.RouteResponse.class);
                        handler.invoke(resp.routes, resp);
                    }
                    break;
                }
                default:
                    break;
            }
        } else {
            switch (msg.message.type) {
                case LoginReq: {
                    Response resp = new Response();
                    if (endpoint.loginReqCheck(msg, resp)) {
                        endpoint.connected = true;
                        endpoint.onConnected();
                    } else {
                        endpoint.close(resp);
                    }
                    break;
                }
                case LoginResp: {
                    Response resp = msg.getPayload(Response.class);
                    if (resp.getSuccess()) {
                        endpoint.connected = true;
                        endpoint.onConnected();
                    } else {
                        endpoint.close(resp);
                    }
                    break;
                }
                default: {
                    endpoint.close(Response.permissionDenied("please login first."));
                    break;
                }
            }
        }
    }


    private static WsEndpoint fromNdid(String ndid) {
        WsEndpoint endpoint = WsDeviceServer.fromNdid(ndid);
        if (endpoint != null) {
            return endpoint;
        }

        DeviceOnlineInfo deviceOnlineInfo = ObjectFactory.cacheAware.findDeviceOnlineInfo(ndid);
        if (deviceOnlineInfo == null) {
            return null;
        }

        endpoint = WsCenterClient.connectToServer(deviceOnlineInfo.getServerNdid(), deviceOnlineInfo.getServerAddress());
        if (endpoint != null) {
            return endpoint;
        }
        return null;
    }

    private static Long sequence() {
        synchronized (_sequence) {
            if (_sequence == Long.MAX_VALUE) {
                _sequence = 1L;
            }
            return _sequence++;
        }
    }

    private static String genMsgId() {
        return String.format("%s$%d", ServerProperties.getNdid(), sequence());
    }


    static class Message {
        Message.Type type;      // 请求类型
        // "LoginReq": payload
        // "LoginResp": payload
        // "RequestReq": url, msgId, payload; url, msgId, target, payload
        // "RequestResp": url, msgId, payload; url, msgId, batch, index, payload
        // "ForwardReq": url, msgId, target, payload
        // "ForwardResp": url, msgId, routes, payload

        String url;
        String msgId;
        String target;
        String payload;

        WsMessage.Batch batch;
        Integer index;


        static Message connectReq(String payload) {
            Message msg = new Message();
            msg.type = Type.LoginReq;
            msg.payload = payload;
            return msg;
        }

        static Message connectResp(String payload) {
            Message msg = new Message();
            msg.type = Type.LoginResp;
            msg.payload = payload;
            return msg;
        }

        static Message requestReq(String url, String msgId, String payload) {
            Message msg = new Message();
            msg.type = Type.RequestReq;
            msg.url = url;
            msg.msgId = msgId;
            msg.payload = payload;
            return msg;
        }

        static Message requestReq(String url, String msgId, String target, String payload) {
            Message msg = new Message();
            msg.type = Type.RequestReq;
            msg.url = url;
            msg.msgId = msgId;
            msg.target = target;
            msg.payload = payload;
            return msg;
        }

        static Message requestResp(String url, String msgId, String payload) {
            Message msg = new Message();
            msg.type = Type.RequestResp;
            msg.url = url;
            msg.msgId = msgId;
            msg.payload = payload;
            return msg;
        }

        static Message requestBatchResp(WsMessage.Batch batch, Integer index, String url, String msgId, String payload) {
            Message msg = new Message();
            msg.type = Type.RequestBatchResp;
            msg.batch = batch;
            msg.index = index;
            msg.url = url;
            msg.msgId = msgId;
            msg.payload = payload;
            return msg;
        }

        static Message forwardReq(String url, String msgId, String target, String payload) {
            Message msg = new Message();
            msg.type = Type.ForwardReq;
            msg.url = url;
            msg.msgId = msgId;
            msg.target = target;
            msg.payload = payload;
            return msg;
        }

        static Message forwardResp(String url, String msgId, List<String> routes, Response response) {
            Message msg = new Message();
            msg.type = Type.ForwardResp;
            msg.url = url;
            msg.msgId = msgId;
            RouteResponse resp = new RouteResponse(response.getSuccess(), response.getStatus(), response.getReason(), routes);
            msg.payload = JsonUtils.toJSON(resp);
            return msg;
        }

        static Message forwardResp(String url, String msgId, Response response) {
            Message msg = new Message();
            msg.type = Type.ForwardResp;
            msg.url = url;
            msg.msgId = msgId;
            List<String> routes = new ArrayList<>(1);
            routes.set(0, ServerProperties.getNdid());
            RouteResponse resp = new RouteResponse(response.getSuccess(), response.getStatus(), response.getReason(), routes);
            msg.payload = JsonUtils.toJSON(resp);
            return msg;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public WsMessage.Batch getBatch() {
            return batch;
        }

        public void setBatch(WsMessage.Batch batch) {
            this.batch = batch;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        static enum Type {
            LoginReq,
            LoginResp,
            RequestReq,
            RequestResp,
            RequestBatchResp,
            ForwardReq,
            ForwardResp,
        }

        static class RouteResponse extends Response {
            List<String> routes;

            public RouteResponse() {
            }

            public RouteResponse(Boolean success, Status status, String reason, List<String> routes) {
                super(success, status, reason);
                this.routes = routes;
            }

            public List<String> getRoutes() {
                return routes;
            }

            public void setRoutes(List<String> routes) {
                this.routes = routes;
            }
        }
    }

    static class Handler {
        Object object;
        Method method;

        Handler(Object object, Method method) {
            this.object = object;
            this.method = method;
        }

        Object invoke(Object... args) {
            try {
                return method.invoke(object, args);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    static class ResponseHandler {
        Long timeout;
        WsResponse response;

        public ResponseHandler(Long timeout, WsResponse response) {
            this.timeout = timeout;
            this.response = response;
        }

        void invoke(WsMessage message) {
            try {
                this.response.response(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class RouteResponseHandler {
        Long timeout;
        WsRouteResponse response;

        public RouteResponseHandler(Long timeout, WsRouteResponse response) {
            this.timeout = timeout;
            this.response = response;
        }

        void invoke(List<String> routes, Response response) {
            try {
                this.response.response(routes, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
