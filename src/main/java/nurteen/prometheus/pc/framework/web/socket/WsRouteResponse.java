package nurteen.prometheus.pc.framework.web.socket;

import nurteen.prometheus.pc.framework.Response;

import java.util.List;

public interface WsRouteResponse {
    void response(List<String> routes, Response response);
}
