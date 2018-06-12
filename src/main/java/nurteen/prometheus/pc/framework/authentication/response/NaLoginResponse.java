package nurteen.prometheus.pc.framework.authentication.response;

public class NaLoginResponse extends LoginResponse {
    public NaLoginResponse(String nuid, String ndid, String accessToken, String nickname, String headimg) {
        super(nuid, ndid, accessToken, nickname, headimg);
    }
}
