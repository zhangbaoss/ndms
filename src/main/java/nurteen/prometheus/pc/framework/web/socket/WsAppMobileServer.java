package nurteen.prometheus.pc.framework.web.socket;

import org.apache.tomcat.websocket.WsSession;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;

@ServerEndpoint(value = "/app/mobile")
public class WsAppMobileServer extends WsServer {
    static String PEER_TYPE = "app-mobile";
    // "/app/mobile?nuid=gggg&ndid=ggg&access-token=xxx&secret-key=gggg"

    public WsAppMobileServer() {
        super(PEER_TYPE);
    }

    public static void request(String url, Object object) {
        synchronized (WsMsgDispatcher.peers) {
            Map<String, WsServer> wsServerMap = WsMsgDispatcher.peers.get(PEER_TYPE);
            for (Map.Entry<String, WsServer> entry: wsServerMap.entrySet()) {
                WsMsgDispatcher.request(entry.getValue(), url, object);
            }
        }
    }
    public static void request(String ndid, String url, Object object) {
        synchronized (WsMsgDispatcher.peers) {
            WsServer wsServer = WsMsgDispatcher.peers.get(PEER_TYPE).get(ndid);
            if (wsServer != null) {
                WsMsgDispatcher.request(wsServer, url, object);
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = (WsSession) session;
        WsMsgDispatcher.onOpen(this);
    }
    @OnMessage
    public void onMessage(String message) {
        WsMsgDispatcher.onMessage(this, message);
    }
    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }
    @OnClose
    public void onClose(CloseReason reason) {
        WsMsgDispatcher.onClose(this);
    }
}
