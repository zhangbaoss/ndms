package nurteen.prometheus.pc.framework.web.socket;

public class WsMsg<T> extends WsMsgDispatcher.WsMsgBase {
    T payload;

    public WsMsg(T payload) {
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }
}
