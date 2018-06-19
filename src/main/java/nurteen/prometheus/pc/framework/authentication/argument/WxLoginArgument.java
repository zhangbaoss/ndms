package nurteen.prometheus.pc.framework.authentication.argument;

import nurteen.prometheus.pc.framework.Argument;
import nurteen.prometheus.pc.framework.exception.InvalidArgumentException;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class WxLoginArgument extends LoginArgument {
    private static final String _wxaencfront = "prometheus.wxa.encfront[";
    private static final String _wxaencback = "]prometheus.wxa.encback";

    String product;
    String code;

    @NotNull(message = "loginTime不能为空")
    @Length(min = 19, max = 19, message = "loginTime无效")
    String loginTime;

    @NotNull(message = "secretKey不能为空")
    @Length(min = 32, max = 32, message = "secretKey无效")
    String secretKey;

    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
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

        /*
        String passwdChk = Md5Utils.md5(_wxaencfront + Md5Utils.md5(phone) + _wxaencback);
        if (!this.passwd.equals(passwdChk)) {
            throw new InvalidArgumentException("密码校验失败");
        }

        String original = String.format("{'phone':'%s','passwd':'%s','deviceArgs':{'type':'%d','name':'%s','platform':'%d','hid':'%s'},'loginTime':'%s'}", phone, passwd, deviceArgs.type, deviceArgs.name, deviceArgs.platform, deviceArgs.hid, loginTime);
        String secretKeyChk = Md5Utils.md5(_wxaencfront + Md5Utils.md5(original) + _wxaencback);
        if (!this.secretKey.equals(secretKeyChk)) {
            throw new InvalidArgumentException("参数校验失败");
        }
        */
    }
}
