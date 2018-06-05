package nurteen.prometheus.pc.framework.authentication.entities;

public class WXaLoginArgs extends LoginArgs {
    String nickname;
    String headimg;

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
}
