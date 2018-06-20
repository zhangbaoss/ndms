package nurteen.prometheus.pc.framework.authentication.argument;

import nurteen.prometheus.pc.framework.Argument;
import nurteen.prometheus.pc.framework.entities.ApplicationType;
import nurteen.prometheus.pc.framework.entities.DevicePlatform;
import nurteen.prometheus.pc.framework.entities.DeviceType;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class LoginArgument extends Argument {

    @NotNull(message = "设备信息不能为空")
    Device device;

    public static class Device {
        @NotNull(message = "设备类型不能为空")
        @Range(min = 1, max = 6, message = "设备类型值不合法")
        Integer type;

        @NotNull(message = "设备运行平台不能为空")
        @Range(min = 1, max = 7, message = "设备平台值不合法")
        Integer platform;

        @NotNull(message = "设备名称不能为空")
        String name;

        @NotNull(message = "设备硬件唯一标识不能为空")
        String hid;

        public Integer getType() {
            return type;
        }
        public void setType(Integer type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
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
        public ApplicationType getApplicationType() {
            switch (this.getDeviceType()) {
                case App_Mobile:
                case Controller_Mobile:
                    return ApplicationType.App_Mobile;
                case App_Browser:
                case App_Pc:
                case Controller_Pc:
                case Center_Pc:
                    return ApplicationType.App_Website;
                default:
                    return null;
            }
        }
    }
}
