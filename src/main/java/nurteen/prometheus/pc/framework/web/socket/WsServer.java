package nurteen.prometheus.pc.framework.web.socket;

import nurteen.prometheus.pc.framework.Constants;
import org.apache.tomcat.websocket.WsSession;

import javax.websocket.CloseReason;

public class WsServer {
    Boolean connected;
    WsSession session;
    String peerType;
    String nuid;
    String ndid;
    String accessToken;

    public WsServer(String peerType) {
        this.connected = false;
        this.session = null;
        this.peerType = peerType;
        this.nuid = null;
        this.ndid = null;
        this.accessToken = null;
    }

    public String getHttpSessionId() {
        return this.session.getHttpSessionId();
    }
    public String getPeerType() {
        return this.peerType;
    }
    public String getNuid() {
        return this.nuid;
    }
    public String getNdid() {
        return this.ndid;
    }
    public String getAccessToken() {
        return this.accessToken;
    }

    public void sendText(String text) {
        try {
            session.getBasicRemote().sendText(text);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        this.close(CloseReason.CloseCodes.NORMAL_CLOSURE, Constants.OK);
    }
    public void permissionDeniedClose() {
        this.close(CloseReason.CloseCodes.UNEXPECTED_CONDITION, Constants.PERMISSION_DENIED);
    }

    private void close(CloseReason.CloseCode closeCode, String reason) {
        try {
            session.close(new CloseReason(closeCode, reason));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
