package nurteen.prometheus.pc.framework.web.socket;

import nurteen.prometheus.pc.framework.ServerProperties;
import nurteen.prometheus.pc.framework.utils.MapUtils;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@ClientEndpoint
public class WsCenterClient extends WsCenterEndpoint {
    static Map<String, WsCenterClient> servers = new HashMap<>(); // ndid -> WsCenterClient

    boolean connecting;

    public WsCenterClient(String ndid) {
        this.connecting = false;
        this.ndid = ndid;
    }

    public static WsEndpoint connectToServer(String serverNdid, String serverAddress) {
        try {
            WsCenterClient server;

            synchronized (servers) {
                server = servers.get(serverNdid);
                if (server == null) {
                    servers.put(serverNdid, server = new WsCenterClient(serverNdid));
                }
            }

            if (server.connected) {
                return server;
            }

            synchronized (server) {

                if (server.connected) {
                    return server;
                }

                String url = String.format("ws://%s/center?ndid=%s", serverAddress, ServerProperties.getNdid());
                URI uri = URI.create(url);
                ContainerProvider.getWebSocketContainer().connectToServer(server, uri);
                server.wait(3000);
            }

            if (server.connected) {
                return server;
            }
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onConnected() {
        super.onConnected();

        System.out.println(String.format("Center Server WebSocket Connected. ndid = %s", ndid));

        this.notifyAll();

        /*
        synchronized (servers) {
            MapUtils.put(servers, this.ndid, this);
        }
        */
    }

    @Override
    protected void onDisconnected() {
        super.onDisconnected();

        System.out.println(String.format("Center Server WebSocket Disconnected. ndid = %s", ndid));

        synchronized (servers) {
            MapUtils.remove(servers, this.ndid);
        }
    }
}
