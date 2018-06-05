package nurteen.prometheus.pc.framework.entities;

public class RedisAccountInfo {
    String nuid;
    String ndid;

    public RedisAccountInfo(String nuid, String ndid) {
        this.nuid = nuid;
        this.ndid = ndid;
    }

    public String getNuid() {
        return nuid;
    }
    public void setNuid(String nuid) {
        this.nuid = nuid;
    }

    public String getNdid() {
        return ndid;
    }
    public void setNdid(String ndid) {
        this.ndid = ndid;
    }
}
