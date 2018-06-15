package nurteen.prometheus.pc.framework.web.socket;

import nurteen.prometheus.pc.framework.Reason;

import java.util.List;

public interface WsResponse {
    void resolve(List<String> routes);
    void resolve(WsMessage message);
    void reject(List<String> routes, Reason reason);
}
