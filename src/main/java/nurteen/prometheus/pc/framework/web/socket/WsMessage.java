package nurteen.prometheus.pc.framework.web.socket;

import nurteen.prometheus.pc.framework.ServerProperties;
import nurteen.prometheus.pc.framework.utils.JsonUtils;

public class WsMessage {
    WsEndpoint endpoint;
    WsMessageDispatcher.Message message;

    WsMessage(WsEndpoint endpoint, WsMessageDispatcher.Message message) {
        this.endpoint = endpoint;
        this.message = message;
        this.message.index = null;
    }

    public WsEndpoint getEndpoint() {
        return endpoint;
    }

    public WsMessageDispatcher.Message.Type getType() {
        return message.type;
    }

    public String getUrl() {
        return message.url;
    }

    public String getMsgId() {
        return message.msgId;
    }

    public String getTarget() {
        return message.getTarget();
    }

    public String getPayload() {
        return message.payload;
    }

    public <T> T getPayload(Class<T> type) {
        return JsonUtils.fromJSON(message.payload, type);
    }

    public boolean forMe() {
        return (message.target == null) || ServerProperties.getNdid().equals(message.target);
    }

    public boolean response(Object payload) {
        return this.response(JsonUtils.toJSON(payload));
    }

    public boolean response(String payload) {
        return endpoint.sendRequestResp(message.url, message.msgId, payload);
    }

    public void responseBatch(Batch batch, Object payload) {
        this.responseBatch(batch, JsonUtils.toJSON(payload));
    }

    public void responseBatch(Batch batch, String payload) {
        message.index = (batch != Batch.Begin) ? (message.index + 1) : 0;
        endpoint.sendMsg(WsMessageDispatcher.Message.requestBatchResp(batch, message.index, message.url, message.msgId, payload));
    }

    public static enum Batch {
        Begin,
        Continue,
        End,
    }
}
