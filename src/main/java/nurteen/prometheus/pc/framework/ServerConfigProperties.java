package nurteen.prometheus.pc.framework;

public class ServerConfigProperties extends ServerProperties {
    String idGenPrefix;
    String serverAddress;
    Integer wsRequestTimeout;
    Integer wsForwardTimeout;

    public ServerConfigProperties() {
        this.idGenPrefix = "11";
        this.wsRequestTimeout = this.wsForwardTimeout = 30000;
    }

    public String getIdGenPrefix() {
        return idGenPrefix;
    }
    public void setIdGenPrefix(String idGenPrefix) {
        this.idGenPrefix = idGenPrefix;
    }

    public String getServerAddress() {
        return serverAddress;
    }
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public Integer getWsRequestTimeout() {
        return wsRequestTimeout;
    }
    public void setWsRequestTimeout(Integer wsRequestTimeout) {
        this.wsRequestTimeout = wsRequestTimeout;
    }

    public Integer getWsForwardTimeout() {
        return wsForwardTimeout;
    }
    public void setWsForwardTimeout(Integer wsForwardTimeout) {
        this.wsForwardTimeout = wsForwardTimeout;
    }
}
