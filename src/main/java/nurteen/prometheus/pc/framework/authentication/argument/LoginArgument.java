package nurteen.prometheus.pc.framework.authentication.argument;

import nurteen.prometheus.pc.framework.Argument;
import nurteen.prometheus.pc.framework.entities.DevicePlatform;
import nurteen.prometheus.pc.framework.entities.DeviceType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class LoginArgument extends Argument {
    @NotNull(message = "账号不能为空")
    @Length(min = 1, max = 128, message = "账号长度为1~128个字符")
    String account;
    @NotNull(message = "密码不能为空")
    @Length(min = 32, max = 32, message = "密码无效")
    String passwd;
    @NotNull(message = "验证码不能为空")
    @Length(min = 5, max = 5, message = "验证码无效")
    String verifyCode;
    @NotNull(message = "设备信息不能为空")
    LoginArgument.Device device;
    @NotNull(message = "loginTime不能为空")
    @Length(min = 19, max = 19, message = "loginTime无效")
    String loginTime;
    @NotNull(message = "secretKey不能为空")
    @Length(min = 32, max = 32, message = "secretKey无效")
    String secretKey;

    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    public String getPasswd() {
        return passwd;
    }
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getVerifyCode() {
        return verifyCode;
    }
    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Device getDevice() {
        return device;
    }
    public void setDevice(Device device) {
        this.device = device;
    }

    public String getLoginTime() {
        return loginTime;
    }
    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }


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
    }
}
