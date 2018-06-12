package nurteen.prometheus.pc.framework.entities;

import nurteen.prometheus.pc.framework.ObjectFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DeviceInfo {
    @Id
    @Column(name = "ndid")
    String ndid;
    @Column(name = "type")
    Integer type;
    @Column(name = "platform")
    Integer platform;
    @Column(name = "hid")
    String hid;

    public DeviceInfo() {
    }
    public DeviceInfo(Integer type, Integer platform, String hid) throws Exception {
        this.ndid = ObjectFactory.storageAware.genNdid(type);
        this.type = type;
        this.platform = platform;
        this.hid = hid;
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

    public Integer getPlatform() {
        return platform;
    }
    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getHid() {
        return hid;
    }
    public void setHid(String hid) {
        this.hid = hid;
    }
}
