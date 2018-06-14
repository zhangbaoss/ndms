package nurteen.prometheus.pc.framework.utils;

import nurteen.prometheus.pc.framework.web.socket.WsMessage;
import nurteen.prometheus.pc.framework.web.socket.WsMessageDispatcher;
import nurteen.prometheus.pc.framework.web.socket.WsResponse;
import nurteen.prometheus.pc.framework.web.socket.WsRouteResponse;

import javax.servlet.http.HttpServletRequest;

public class WsUtils {
    public static void request(HttpServletRequest request, String url, String target, String payload, long timeout, WsResponse response) {
        WsMessageDispatcher.request(request, url, target, payload, timeout, response);
    }

    public static void request(WsMessage message, long timeout, WsResponse response) {
        WsMessageDispatcher.request(message, timeout, response);
    }

    public static void request(WsMessage message, String payload, long timeout, WsResponse response) {
        WsMessageDispatcher.request(message, payload, timeout, response);
    }

    public static void request(WsMessage message, String target, String payload, long timeout, WsResponse response) {
        WsMessageDispatcher.request(message, target, payload, timeout, response);
    }

    public static void request(WsMessage message, String url, String target, String payload, long timeout, WsResponse response) {
        WsMessageDispatcher.request(message, url, target, payload, timeout, response);
    }

    public static void request(String url, String target, String payload, long timeout, WsResponse response) {
        WsMessageDispatcher.request(url, target, payload, timeout, response);
    }

    public static void forward(WsMessage message, long timeout, WsRouteResponse response) {
        WsMessageDispatcher.forward(message, timeout, response);
    }

    public static void forward(WsMessage message, String payload, long timeout, WsRouteResponse response) {
        WsMessageDispatcher.forward(message, payload, timeout, response);
    }

    public static void forward(WsMessage message, String url, String payload, long timeout, WsRouteResponse response) {
        WsMessageDispatcher.forward(message, url, payload, timeout, response);
    }

    public static void forward(WsMessage message, String url, String target, String payload, long timeout, WsRouteResponse response) {
        WsMessageDispatcher.forward(message, url, target, payload, timeout, response);
    }

    public static void forward(String url, String target, String payload, long timeout, WsRouteResponse response) {
        WsMessageDispatcher.forward(url, target, payload, timeout, response);
    }
}
