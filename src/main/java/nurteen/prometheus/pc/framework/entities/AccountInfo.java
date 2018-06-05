package nurteen.prometheus.pc.framework.entities;

import nurteen.prometheus.pc.framework.AccountDBAware;

public class AccountInfo {
    String nuid;
    Integer type;
    String account;
    String passwd;
    String nickname;
    String headimg;

    public AccountInfo() {

    }
    public AccountInfo(Integer type, String account, String passwd, String nickname, String headimg) {
        this.nuid = AccountDBAware.getAccountDBAware().genNuid(type, account);
        this.type = type;
        this.account = account;
        this.passwd = passwd;
        this.nickname = nickname;
        this.headimg = headimg;
    }
    public AccountInfo(String nuid, Integer type, String account, String passwd, String nickname, String headimg) {
        this.nuid = nuid;
        this.type = type;
        this.account = account;
        this.passwd = passwd;
        this.nickname = nickname;
        this.headimg = headimg;
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

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimg() {
        return headimg;
    }
    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }


    public static interface Type {
        int NURTEEN = 1;    // 乐丁账号
        int WX = 2;         // 微信账号
        int QQ = 3;         // QQ账号
        int WB = 4;         // 微博账号
    }
}
