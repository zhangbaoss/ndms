package nurteen.prometheus.pc.framework.web.socket;

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
    public String getUrl() {
        return message.url;
    }
    public String getMsgId() {
        return message.msgId;
    }
    public String  getPayload() {
        return message.payload;
    }
    public <T> T getPayload(Class<T> type) {
        return JsonUtils.fromJSON(message.payload, type);
    }

    public void response(Object payload) {
        this.response(JsonUtils.toJSON(payload));
    }
    public void response(String payload) {
        endpoint.sendMsg(WsMessageDispatcher.Message.requestResp(message.url, message.msgId, payload));
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
