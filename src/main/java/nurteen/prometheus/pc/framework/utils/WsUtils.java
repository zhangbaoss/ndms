package nurteen.prometheus.pc.framework.utils;

import nurteen.prometheus.pc.framework.web.socket.WsMsgDispatcher;

public class WsUtils {
    public static void request(String url, Object object) {
        WsMsgDispatcher.request(url, object);
    }
    public static void request(String ndid, String url, Object object) {
        WsMsgDispatcher.request(ndid, url, object);
    }
}
