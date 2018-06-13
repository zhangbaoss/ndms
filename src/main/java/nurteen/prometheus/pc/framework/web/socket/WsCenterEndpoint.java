package nurteen.prometheus.pc.framework.web.socket;

public class WsCenterEndpoint extends WsEndpoint {

    String ndid;

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
