package nurteen.prometheus.pc.framework.authentication.response;

public class WXaLoginResponse extends LoginResponse {
    public WXaLoginResponse(String nuid, String ndid, String accessToken, String nickname, String headimg) {
        super(nuid, ndid, accessToken, nickname, headimg);
    }
}
