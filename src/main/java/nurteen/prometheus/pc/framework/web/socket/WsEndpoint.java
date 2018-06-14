package nurteen.prometheus.pc.framework.web.socket;

import nurteen.prometheus.pc.framework.ObjectFactory;
import nurteen.prometheus.pc.framework.Reason;
import nurteen.prometheus.pc.framework.Response;
import nurteen.prometheus.pc.framework.utils.JsonUtils;

import javax.websocket.*;
import java.util.List;

public class WsEndpoint {
    Boolean connected;
    Session session;

    public WsEndpoint() {
        this.connected = false;
        this.session = null;
    }

    @OnOpen
    public final void onOpen(Session session) {
        this.connected = false;
        this.session = session;
    }

    @OnMessage
    public final void onMessage(String message) {
        ObjectFactory.messageDispatcher.onMessage(this, message);
    }

    @OnError
    public final void onError(Throwable error) {
        // error.printStackTrace();
    }

    @OnClose
    public final void onClose() {
        if (this.connected) {
            this.connected = false;
            this.onDisconnected();
        }
    }

    protected boolean loginReqCheck(WsMessage message, Response response) {
        return true;
    }

    protected void onConnected() {
    }

    protected void onDisconnected() {
    }


    public boolean sendConnectReq(Object connectReq) {
        WsMessageDispatcher.Message message = new WsMessageDispatcher.Message();
        message.type = WsMessageDispatcher.Message.Type.LoginReq;
        message.payload = JsonUtils.toJSON(connectReq);
        return sendMsg(message);
    }

    public boolean sendConnectResp(Response connectResp) {
        WsMessageDispatcher.Message message = new WsMessageDispatcher.Message();
        message.type = WsMessageDispatcher.Message.Type.LoginResp;
        message.payload = JsonUtils.toJSON(connectResp);
        return sendMsg(message);
    }

    public boolean sendRequestReq(String url, String msgId, String target, String payload) {
        WsMessageDispatcher.Message message = new WsMessageDispatcher.Message();
        message.type = WsMessageDispatcher.Message.Type.RequestReq;
        message.url = url;
        message.msgId = msgId;
        message.target = target;
        message.payload = payload;
        return sendMsg(message);
    }

    public boolean sendRequestResp(String url, String msgId, String payload) {
        WsMessageDispatcher.Message message = new WsMessageDispatcher.Message();
        message.type = WsMessageDispatcher.Message.Type.RequestResp;
        message.url = url;
        message.msgId = msgId;
        message.payload = payload;
        return sendMsg(message);
    }

    public boolean sendForwardReq(String url, String msgId, String target, String payload) {
        WsMessageDispatcher.Message message = new WsMessageDispatcher.Message();
        message.type = WsMessageDispatcher.Message.Type.ForwardReq;
        message.url = url;
        message.msgId = msgId;
        message.target = target;
        message.payload = payload;
        return sendMsg(message);
    }

    public boolean sendForwardResp(String url, String msgId, List<String> routes) {
        return sendForwardResp(url, msgId, routes, null);
    }

    public boolean sendForwardResp(String url, String msgId, List<String> routes, Reason reason) {
        WsMessageDispatcher.Message message = new WsMessageDispatcher.Message();
        message.type = WsMessageDispatcher.Message.Type.ForwardResp;
        message.url = url;
        message.msgId = msgId;
        message.payload = JsonUtils.toJSON(new WsMessageDispatcher.Message.RoutePayload(routes, reason));
        return sendMsg(message);
    }

    public boolean sendMsg(WsMessageDispatcher.Message message) {
        try {
            this.sendObject(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendObject(Object object) throws Exception {
        sendText(JsonUtils.toJSON(object));
    }

    public void sendText(String text) throws Exception {
        synchronized (session) {
            session.getBasicRemote().sendText(text);
        }
    }

    public void close(Response response) {
        this.sendConnectResp(response);
        this.close();
    }

    public void close() {
        try {
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
