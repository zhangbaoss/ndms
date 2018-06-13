package nurteen.prometheus.pc.framework.web.socket;

public interface WsResponse {
    void resolve(WsMessage message);
    void reject();
}
