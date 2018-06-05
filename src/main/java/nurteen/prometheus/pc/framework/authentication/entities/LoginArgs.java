package nurteen.prometheus.pc.framework.authentication.entities;

import nurteen.prometheus.pc.framework.HttpArgs;

public class LoginArgs extends HttpArgs {
    String ndid;
    String account;
    String passwd;
    String loginTime;
    String secretKey;

    public String getNdid() {
        return ndid;
    }
    public void setNdid(String ndid) {
        this.ndid = ndid;
    }

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
}
