package nurteen.prometheus.pc.framework.entities;

public class DeviceOnlineInfo {
    String nuid;
    Integer type;
    String serverNdid;
    String serverAddress;

    public DeviceOnlineInfo(String nuid, Integer type, String serverNdid, String serverAddress) {
        this.nuid = nuid;
        this.type = type;
        this.serverNdid = serverNdid;
        this.serverAddress = serverAddress;
    }

    public String getNuid() {
        return nuid;
    }
    public void setNuid(String nuid) {
        this.nuid = nuid;
    }

    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public String getServerNdid() {
        return serverNdid;
    }
    public void setServerNdid(String serverNdid) {
        this.serverNdid = serverNdid;
    }

    public String getServerAddress() {
        return serverAddress;
    }
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
}
