package nurteen.prometheus.pc.framework.entities;

public class AccessTokenInfo {
    String nuid;
    String ndid;
    Integer type;

    public AccessTokenInfo(String nuid, String ndid, Integer type) {
        this.nuid = nuid;
        this.ndid = ndid;
        this.type = type;
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

    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
}
