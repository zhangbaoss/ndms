package nurteen.prometheus.pc.framework.web.socket;

import nurteen.prometheus.pc.framework.Response;
import nurteen.prometheus.pc.framework.utils.ContainerUtils;

import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint("/center")
public class WsCenterServer extends WsCenterEndpoint {
    static Map<String, WsCenterServer> clients = new HashMap<>(); // ndid -> WsCenterServer

    static WsCenterServer fromNdid(String ndid) {
        synchronized (clients) {
            return clients.get(ndid);
        }
    }

    @Override
    protected boolean loginReqCheck(WsMessage message, Response response) {
        if (!super.loginReqCheck(message, response)) {
            return false;
        }

        ConnectReq connectReq = message.getPayload(ConnectReq.class);

        // 检查参数
        /*
        if (!secretKey.equals(Md5Utils.md5(_wsencfront + Md5Utils.md5(String.format("{'ndid':'%s','connect-time':'%s','secret-key':''}", ndid, connectTime, secretKey)) + _wsencback))) {
            System.out.println("WebSocket connect failed, because parameters invalid.");
            endpoint.permissionDeniedClose();
            return;
        }
        */

        this.ndid = connectReq.ndid;

        return true;
    }

    @Override
    protected void onConnected() {
        super.onConnected();

        System.out.println(String.format("Center Client WebSocket Connected. ndid = %s", ndid));

        synchronized (clients) {
            ContainerUtils.put(clients, this.ndid, this);
        }
    }

    @Override
    protected void onDisconnected() {
        super.onDisconnected();

        System.out.println(String.format("Center Client WebSocket Disconnected. ndid = %s", ndid));

        synchronized (clients) {
            ContainerUtils.remove(clients, this.ndid);
        }
    }
}
