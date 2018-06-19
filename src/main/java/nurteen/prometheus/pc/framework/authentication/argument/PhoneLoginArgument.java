package nurteen.prometheus.pc.framework.authentication.argument;

import nurteen.prometheus.pc.framework.exception.InvalidArgumentException;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class PhoneLoginArgument extends LoginArgument {
    @NotNull(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机号长度为11个字符")
    String phone;

    @NotNull(message = "密码不能为空")
    @Length(min = 32, max = 32, message = "密码无效")
    String passwd;

    @NotNull(message = "验证码不能为空")
    @Length(min = 5, max = 5, message = "验证码无效")
    String verifyCode;

    @NotNull(message = "loginTime不能为空")
    @Length(min = 19, max = 19, message = "loginTime无效")
    String loginTime;

    @NotNull(message = "secretKey不能为空")
    @Length(min = 32, max = 32, message = "secretKey无效")
    String secretKey;

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
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

    @Override
    public void validate() throws InvalidArgumentException {
        super.validate();
    }
}
