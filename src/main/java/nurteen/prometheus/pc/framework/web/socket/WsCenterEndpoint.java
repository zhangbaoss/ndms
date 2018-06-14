package nurteen.prometheus.pc.framework.web.socket;

import nurteen.prometheus.pc.framework.ServerProperties;
import nurteen.prometheus.pc.framework.utils.JsonUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WsCenterEndpoint extends WsEndpoint {

    String ndid;


    public boolean sendConnectReq() {
        String ndid = ServerProperties.getNdid();
        String connectTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String secretKey = "22222222222222222222222222222222";
        ConnectReq connectReq = new ConnectReq(ndid, connectTime, secretKey);
        return super.sendConnectReq(connectReq);
    }

    static class ConnectReq {
        String ndid;
        String connectTime;
        String secretKey;

        public ConnectReq() {

        }

        public ConnectReq(String ndid, String connectTime, String secretKey) {
            this.ndid = ndid;
            this.connectTime = connectTime;
            this.secretKey = secretKey;
        }

        public String getNdid() {
            return ndid;
        }

        public void setNdid(String ndid) {
            this.ndid = ndid;
        }

        public String getConnectTime() {
            return connectTime;
        }

        public void setConnectTime(String connectTime) {
            this.connectTime = connectTime;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }
    }
}
