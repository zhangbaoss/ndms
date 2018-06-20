package nurteen.prometheus.pc.framework.web.socket;

import nurteen.prometheus.pc.framework.*;
import nurteen.prometheus.pc.framework.entities.DeviceOnlineInfo;
import nurteen.prometheus.pc.framework.utils.ContainerUtils;
import nurteen.prometheus.pc.framework.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WsMessageDispatcher {
    private static final String _wsencfront = "prometheus.ws.encfront";
    private static final String _wsencback = "prometheus.ws.encback";

    static ConcurrentMap<String, Handler> reqHandlers = new ConcurrentHashMap<>(); // url -> requestHandler
    static ConcurrentMap<String, ResponseHandler> respHandlers = new ConcurrentHashMap<>();    // msgId -> responseHandler
    static ConcurrentMap<String, RouteResponseHandler> routeRespHandlers = new ConcurrentHashMap<>();    // msgId -> forwardResponseHandler

    static Long _sequence = 1L;

    public static void addOnOpenHandler(Object bean, Method method) {
        addMessageHandler(Constants.WSMSGHANDLER_ONOPEN_URL, bean, method);
    }

    public static void addOnCloseHandler(Object bean, Method method) {
        addMessageHandler(Constants.WSMSGHANDLER_ONCLOSE_URL, bean, method);
    }

    public static void addMessageHandler(String url, Object bean, Method method) {
        reqHandlers.put(url, new Handler(bean, method));
    }


    /*
    public static void request(String url, String payload, long timeout, WsResponse response) {
        synchronized (peers) {
            for (Map.Entry<String, WsEndpoint> entry : ndids.entrySet()) {
                request(entry.getValue(), url, entry.getKey(), payload, timeout, response);
            }
        }
    }
    */

    public static void request(HttpServletRequest request, String url, String target, String payload, long timeout, WsResponse response) {
        request(url, target, payload, timeout, response);
    }

    public static void request(WsMessage message, long timeout, WsResponse response) {
        _request(message.getUrl(), message.getMsgId(), message.getTarget(), message.getPayload(), timeout, response);
    }

    public static void request(WsMessage message, String payload, long timeout, WsResponse response) {
        _request(message.getUrl(), message.getMsgId(), message.getTarget(), payload, timeout, response);
    }

    public static void request(WsMessage message, String target, String payload, long timeout, WsResponse response) {
        _request(message.getUrl(), message.getMsgId(), target, payload, timeout, response);
    }

    public static void request(WsMessage message, String url, String target, String payload, long timeout, WsResponse response) {
        _request(url, message.getMsgId(), target, payload, timeout, response);
    }

    public static void request(String url, String target, String payload, long timeout, WsResponse response) {
        _request(url, genMsgId(), target, payload, timeout, response);
    }

    static void _request(String url, String msgId, String target, String payload, long timeout, WsResponse response) {
        fromNdid(target, new FindEndpoint() {
            @Override
            public void resolve(WsEndpoint endpoint) {
                _request(endpoint, url, msgId, target, payload, timeout, response);
            }

            @Override
            public void reject(Reason reason) {
                response.reject(new ArrayList<>(), reason);
            }
        });
    }

    static void _request(WsEndpoint endpoint, String url, String msgId, String target, String payload, long timeout, WsResponse response) {
        if (response == null) {
            endpoint.sendRequestReq(url, msgId, target, payload);
        } else if (endpoint.sendRequestReq(url, msgId, target, payload)) {
            respHandlers.put(msgId, new ResponseHandler(timeout, response));
        } else {
            response.reject(new ArrayList<>(), Reason.error("Send Failed"));
        }
    }


    public static void forward(WsMessage message, long timeout, WsRouteResponse response) {
        _forward(message.getUrl(), message.getMsgId(), message.getTarget(), message.getPayload(), timeout, response);
    }

    public static void forward(WsMessage message, String payload, long timeout, WsRouteResponse response) {
        _forward(message.getUrl(), message.getMsgId(), message.getTarget(), payload, timeout, response);
    }

    public static void forward(WsMessage message, String url, String payload, long timeout, WsRouteResponse response) {
        _forward(url, message.getMsgId(), message.getTarget(), payload, timeout, response);
    }

    public static void forward(WsMessage message, String url, String target, String payload, long timeout, WsRouteResponse response) {
        _forward(url, message.getMsgId(), target, payload, timeout, response);
    }

    public static void forward(String url, String target, String payload, long timeout, WsRouteResponse response) {
        _forward(url, genMsgId(), target, payload, timeout, response);
    }

    static void _forward(String url, String msgId, String target, String payload, long timeout, WsRouteResponse response) {
        fromNdid(target, new FindEndpoint() {
            @Override
            public void resolve(WsEndpoint endpoint) {
                _forward(endpoint, url, msgId, target, payload, timeout, response);
            }

            @Override
            public void reject(Reason reason) {
                response.reject(ContainerUtils.makeArrayList(ServerProperties.getNdid()).get(), reason);
            }
        });
    }

    static void _forward(WsEndpoint endpoint, String url, String msgId, String target, String payload, long timeout, WsRouteResponse response) {
        if (response == null) {
            endpoint.sendForwardReq(url, msgId, target, payload);
        } else if (endpoint.sendForwardReq(url, msgId, target, payload)) {
            routeRespHandlers.put(msgId, new RouteResponseHandler(timeout, response));
        } else {
            response.reject(new ArrayList<>(), Reason.error("Send Failed"));
        }
    }

    /*
    static void onOpen(WsEndpoint endpoint) {
        Handler handler = reqHandlers.get(Constants.WSMSGHANDLER_ONOPEN_URL);
        if (handler != null) {
            handler.invoke(endpoint);
        }
    }
    static void onClose(WsEndpoint endpoint) {
        Handler handler = reqHandlers.get(Constants.WSMSGHANDLER_ONCLOSE_URL);
        if (handler != null) {
            handler.invoke(endpoint);
        }
    }
    */
    void onMessage(WsEndpoint endpoint, String message) {
        WsMessage msg = new WsMessage(endpoint, JsonUtils.fromJSON(message, Message.class));
        if (msg.getType() == null) {
            endpoint.close(Response.error("数据格式错误，请检查！"));
        } else if (endpoint.connected) {
            // System.out.println(String.format("message, from = %s, source = %s, target = %s, msgId = %s, url = %s, type = %s.", endpoint.ndid, msg.source, msg.target, msg.msgId, msg.url, msg.type.toString()));
            switch (msg.getType()) {
                case RequestReq: {
                    if (msg.forMe()) {
                        endpoint.sendRequestReachedResp(msg.getUrl(), msg.getMsgId(), ServerProperties.getNdidList());
                    }

                    Handler handler = reqHandlers.get(msg.getUrl());
                    if (handler != null) {
                        if (handler.method.getParameterTypes().length == 1) {
                            handler.invoke(msg);
                        } else {
                            handler.invoke();
                        }
                    } else if (!msg.forMe()) {
                        request(msg, 10000, new WsResponse() {
                            @Override
                            public void resolve(List<String> routes) {
                                endpoint.sendRequestReachedResp(msg.getUrl(), msg.getMsgId(), ContainerUtils.add(ServerProperties.getNdidList(), routes));
                            }

                            @Override
                            public void resolve(WsMessage resp) {
                                msg.response(resp.getPayload());
                            }

                            @Override
                            public void reject(List<String> routes, Reason reason) {
                                // System.out.println("request fail. target = " + msg.message.target + ", url = " + msg.message.url);
                                endpoint.sendRequestReachedResp(msg.getUrl(), msg.getMsgId(), ContainerUtils.add(ServerProperties.getNdidList(), routes), reason);
                            }
                        });
                    } else {
                        System.out.println("no request mapping method. url = " + msg.message.url);
                    }
                    break;
                }
                case RequestReachedResp: {
                    ResponseHandler handler = respHandlers.get(msg.getMsgId());
                    if (handler != null) {
                        Message.ReachedRespPayload payload = msg.getPayload(Message.ReachedRespPayload.class);
                        if (payload.reason.getStatus() == Reason.Status.Ok) {
                            handler.resolve(payload.routes);
                        } else {
                            handler.reject(payload.routes, payload.reason);
                        }
                    }
                    break;
                }
                case RequestResp: {
                    ResponseHandler handler = respHandlers.remove(msg.getMsgId());
                    if (handler != null) {
                        handler.resolve(msg);
                    }
                    break;
                }
                case RequestBatchResp:
                    break;
                case ForwardReq: {
                    if (msg.forMe()) {
                        endpoint.sendForwardReachedResp(msg.getUrl(), msg.getMsgId(), ServerProperties.getNdidList());
                    }

                    Handler handler = reqHandlers.get(msg.getUrl());
                    if (handler != null) {
                        if (handler.method.getParameterTypes().length == 1) {
                            handler.invoke(msg);
                        } else {
                            handler.invoke();
                        }
                    } else if (!msg.forMe()) {
                        forward(msg, 10000, new WsRouteResponse() {
                            @Override
                            public void resolve(List<String> routes) {
                                endpoint.sendForwardReachedResp(msg.getUrl(), msg.getMsgId(), ContainerUtils.add(ServerProperties.getNdidList(), routes));
                            }

                            @Override
                            public void reject(List<String> routes, Reason reason) {
                                endpoint.sendForwardReachedResp(msg.getUrl(), msg.getMsgId(), ContainerUtils.add(ServerProperties.getNdidList(), routes), reason);
                            }
                        });
                    } else {
                        System.out.println("no request mapping method. url = " + msg.message.url);
                    }
                    break;
                }
                case ForwardReachedResp: {
                    RouteResponseHandler handler = routeRespHandlers.remove(msg.getMsgId());
                    if (handler != null) {
                        Message.ReachedRespPayload payload = msg.getPayload(Message.ReachedRespPayload.class);
                        if (payload.reason.getStatus() == Reason.Status.Ok) {
                            handler.resolve(payload.routes);
                        } else {
                            handler.reject(payload.routes, payload.reason);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        } else {
            switch (msg.message.type) {
                case LoginReq: {
                    Response resp = new Response();
                    if (endpoint.loginReqCheck(msg, resp)) {
                        endpoint.connected = true;
                        endpoint.sendConnectResp(Response.ok("ok"));
                        endpoint.onConnected();
                    } else {
                        endpoint.close(resp);
                    }
                    break;
                }
                case LoginResp: {
                    Response resp = msg.getPayload(Response.class);
                    if (resp.getSuccess()) {
                        endpoint.connected = true;
                        endpoint.onConnected();
                    } else {
                        endpoint.close(resp);
                    }
                    break;
                }
                default: {
                    endpoint.close(Response.permissionDenied());
                    break;
                }
            }
        }
    }


    private static void fromNdid(String target, FindEndpoint finder) {
        if (target == null) {
            finder.reject(Reason.error("目标不能为空"));
            return;
        }

        WsEndpoint endpoint = WsDeviceServer.fromNdid(target);
        if (endpoint != null) {
            finder.resolve(endpoint);
            return;
        }

        endpoint = WsCenterServer.fromNdid(target);
        if (endpoint != null) {
            finder.resolve(endpoint);
            return;
        }

        DeviceOnlineInfo deviceOnlineInfo = ObjectFactory.cacheAware.findDeviceOnlineInfo(target);
        if (deviceOnlineInfo != null) {
            WsCenterClient.connectToServer(deviceOnlineInfo.getServerNdid(), deviceOnlineInfo.getServerAddress(), finder);
        } else {
            finder.reject(Reason.error(String.format("目标[%s]不可达！", target)));
        }
    }

    private static Long sequence() {
        synchronized (_sequence) {
            if (_sequence == Long.MAX_VALUE) {
                _sequence = 1L;
            }
            return _sequence++;
        }
    }

    private static String genMsgId() {
        return String.format("%s$%d", ServerProperties.getNdid(), sequence());
    }


    static class Message {
        Message.Type type;      // 请求类型
        // "LoginReq": payload
        // "LoginResp": payload
        // "RequestReq": url, msgId, payload; url, msgId, target, payload
        // "RequestResp": url, msgId, payload; url, msgId, batch, index, payload
        // "ForwardReq": url, msgId, target, payload
        // "ForwardResp": url, msgId, routes, payload

        String url;
        String msgId;
        String target;
        String payload;

        WsMessage.Batch batch;
        Integer index;

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public WsMessage.Batch getBatch() {
            return batch;
        }

        public void setBatch(WsMessage.Batch batch) {
            this.batch = batch;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        static enum Type {
            LoginReq,
            LoginResp,
            RequestReq,
            RequestReachedResp,
            RequestResp,
            RequestBatchResp,
            ForwardReq,
            ForwardReachedResp,
        }

        static class ReachedRespPayload {
            List<String> routes;
            Reason reason;

            public ReachedRespPayload() {
            }

            public ReachedRespPayload(List<String> routes, Reason reason) {
                this.routes = routes;
                this.reason = reason;
            }

            public List<String> getRoutes() {
                return routes;
            }
            public void setRoutes(List<String> routes) {
                this.routes = routes;
            }

            public Reason getReason() {
                return reason;
            }
            public void setReason(Reason reason) {
                this.reason = reason;
            }
        }
    }

    static class Handler {
        Object object;
        Method method;

        Handler(Object object, Method method) {
            this.object = object;
            this.method = method;
        }

        Object invoke(Object... args) {
            try {
                return method.invoke(object, args);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    static class ResponseHandler {
        Long timeout;
        WsResponse response;

        public ResponseHandler(Long timeout, WsResponse response) {
            this.timeout = timeout;
            this.response = response;
        }

        void resolve(List<String> routes) {
            try {
                this.response.resolve(routes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        void resolve(WsMessage message) {
            try {
                this.response.resolve(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        void reject(List<String> routes, Reason reason) {
            try {
                this.response.reject(routes, reason);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class RouteResponseHandler {
        Long timeout;
        WsRouteResponse response;

        public RouteResponseHandler(Long timeout, WsRouteResponse response) {
            this.timeout = timeout;
            this.response = response;
        }

        void resolve(List<String> routes) {
            try {
                response.resolve(routes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        void reject(List<String> routes, Reason reason) {
            try {
                response.reject(routes, reason);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static interface FindEndpoint {
        void resolve(WsEndpoint endpoint);
        void reject(Reason reason);
    }
}
