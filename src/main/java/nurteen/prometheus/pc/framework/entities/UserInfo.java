package nurteen.prometheus.pc.framework.entities;

import nurteen.prometheus.pc.framework.ObjectFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserInfo {
    @Id
    // @GenericGenerator(name = "nuidGenerator", strategy = "nurteen.prometheus.pc.framework.id.NuidGenerator")
    // @GeneratedValue(generator = "nuidGenerator")
    @Column(name = "nuid")
    String nuid;
    @Column(name = "account")
    String account;
    @Column(name = "nickname")
    String nickname;
    @Column(name = "phone")
    String phone;
    // String email;
    @Column(name = "headimg")
    String headimg;

    public UserInfo() {
    }
    public UserInfo(String nickname, String phone, String headimg) throws Exception {
        this.nuid = ObjectFactory.storageAware.genNuid();
        this.account = ObjectFactory.storageAware.genAccount();
        this.nickname = nickname;
        this.phone = phone;
        this.headimg = headimg;
    }

    public String getNuid() {
        return nuid;
    }
    public void setNuid(String nuid) {
        this.nuid = nuid;
    }

    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadimg() {
        return headimg;
    }
    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }
}
