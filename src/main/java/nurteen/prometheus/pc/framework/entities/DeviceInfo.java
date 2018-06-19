package nurteen.prometheus.pc.framework.entities;

import nurteen.prometheus.pc.framework.ObjectFactory;
import nurteen.prometheus.pc.framework.authentication.argument.LoginArgument;

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
    public DeviceInfo(DeviceType type, DevicePlatform platform, String hid) throws Exception {
        this.ndid = ObjectFactory.storageAware.genNdid(type);
        this.type = type.getValue();
        this.platform = platform.getValue();
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

    public DeviceType getDeviceType() {
        switch (type) {
            case 1: return DeviceType.App_Browser;
            case 2: return DeviceType.App_Mobile;
            case 3: return DeviceType.App_Pc;
            case 4: return DeviceType.Controller_Mobile;
            case 5: return DeviceType.Controller_Pc;
            case 6: return DeviceType.Center_Pc;
            default: return null;
        }
    }
    public DevicePlatform getDevicePlatform() {
        switch (type) {
            case 1: return DevicePlatform.Browser;
            case 2: return DevicePlatform.Windows;
            case 3: return DevicePlatform.Linux;
            case 4: return DevicePlatform.Unix;
            case 5: return DevicePlatform.MacOS;
            case 6: return DevicePlatform.IOS;
            case 7: return DevicePlatform.Android;
            default: return null;
        }
    }
}
