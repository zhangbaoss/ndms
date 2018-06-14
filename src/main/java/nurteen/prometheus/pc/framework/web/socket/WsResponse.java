package nurteen.prometheus.pc.framework.web.socket;

import nurteen.prometheus.pc.framework.Reason;

public interface WsResponse {
    void resolve(WsMessage message);
    void reject(Reason reason);
}
