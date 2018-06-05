package nurteen.prometheus.pc.framework.authentication.entities;

public class LoginReps {
    String nuid;
    String ndid;
    String accessToken;
    String nickname;
    String headimg;

    public LoginReps(String nuid, String ndid, String accessToken, String nickname, String headimg) {
        this.nuid = nuid;
        this.ndid = ndid;
        this.accessToken = accessToken;
        this.nickname = nickname;
        this.headimg = headimg;
    }

    public String getNuid() {
        return nuid;
    }
    public void setNuid(String nuid) {
        this.nuid = nuid;
    }

    public String getNdid() {
        return ndid;
    }
    public void setNdid(String ndid) {
        this.ndid = ndid;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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
}
