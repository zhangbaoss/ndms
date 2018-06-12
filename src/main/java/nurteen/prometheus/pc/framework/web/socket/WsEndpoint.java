package nurteen.prometheus.pc.framework.web.socket;

import nurteen.prometheus.pc.framework.ObjectFactory;
import nurteen.prometheus.pc.framework.Response;
import nurteen.prometheus.pc.framework.utils.JsonUtils;

import javax.websocket.*;

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
        this.sendMsg(WsMessageDispatcher.Message.connectResp(JsonUtils.toJSON(Response.ok("ok"))));
    }
    protected void onDisconnected() {
    }


    public boolean sendMsg(WsMessageDispatcher.Message message) {
        try {
            this.sendObject(message);
            return true;
        }
        catch (Exception e) {
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
        this.sendMsg(WsMessageDispatcher.Message.connectResp(JsonUtils.toJSON(response)));
        this.close();
    }
    public void close() {
        try {
            session.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
