package nurteen.prometheus.pc.framework.authentication.response;

public class WxLoginResponse extends LoginResponse {
    public WxLoginResponse(String nuid, String ndid, String accessToken, String nickname, String headimg) {
        super(nuid, ndid, accessToken, nickname, headimg);
    }
}
