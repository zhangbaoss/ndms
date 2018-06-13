package nurteen.prometheus.pc.framework.web.socket;

import nurteen.prometheus.pc.framework.Reason;

import java.util.List;

public interface WsRouteResponse {
    void resolve(List<String> routes);
    void reject(List<String> routes, Reason reason);
}
