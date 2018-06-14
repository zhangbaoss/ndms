package nurteen.prometheus.pc.framework.web.socket;

import nurteen.prometheus.pc.framework.Reason;
import nurteen.prometheus.pc.framework.ServerProperties;
import nurteen.prometheus.pc.framework.utils.ContainerUtils;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ClientEndpoint
public class WsCenterClient extends WsCenterEndpoint {
    static Map<String, List<WsCenterClient>> servers = new HashMap<>(); // ndid -> httpsessionid -> WsCenterClient

    boolean connecting;
    WsMessageDispatcher.FindEndpoint finder;

    public WsCenterClient(String ndid, WsMessageDispatcher.FindEndpoint finder) {
        this.connecting = false;
        this.finder = finder;
        this.ndid = ndid;
    }

    public static void connectToServer(String serverNdid, String serverAddress, WsMessageDispatcher.FindEndpoint finder) {
        try {
            WsCenterClient server = null;

            synchronized (servers) {
                List<WsCenterClient> connections = servers.get(serverNdid);
                if (connections != null) {
                    if (connections.size() > 1) {
                        server = connections.remove(0);
                        connections.add(server);
                    } else {
                        server = connections.get(0);
                    }
                }
            }

            if (server != null) {
                finder.resolve(server);
                return;
            }

            server = new WsCenterClient(serverNdid, finder);

            String url = String.format("ws://%s/center?ndid=%s", serverAddress, ServerProperties.getNdid());
            URI uri = URI.create(url);
            ContainerProvider.getWebSocketContainer().connectToServer(server, uri);

            if (!server.sendConnectReq()) {
                server.close();
                finder.reject(Reason.error("发送登录请求失败"));
            }
        } catch (Exception e) {
            finder.reject(Reason.exceptionOccurred(e.toString()));
            e.printStackTrace();
        }
    }

    @Override
    protected void onConnected() {
        super.onConnected();

        System.out.println(String.format("Center Server WebSocket Connected. ndid = %s", ndid));

        if (this.finder != null) {
            WsMessageDispatcher.FindEndpoint finder = this.finder;
            this.finder = null;

            finder.resolve(this);
        }

        synchronized (servers) {
            ContainerUtils.addLinkedList(servers, this.ndid, this);
        }
    }

    @Override
    protected void onDisconnected() {
        super.onDisconnected();

        System.out.println(String.format("Center Server WebSocket Disconnected. ndid = %s", ndid));

        synchronized (servers) {
            ContainerUtils.deleteLinkedList(servers, this.ndid, this);
        }
    }
}
