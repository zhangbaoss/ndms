package nurteen.prometheus.pc.framework.web.socket;

import nurteen.prometheus.pc.framework.ObjectFactory;
import nurteen.prometheus.pc.framework.Response;
import nurteen.prometheus.pc.framework.entities.AccessTokenInfo;
import nurteen.prometheus.pc.framework.utils.ContainerUtils;

import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint("/devices")
public class WsDeviceServer extends WsEndpoint {
    static Map<String, WsDeviceServer> devices = new HashMap<>(); // ndid -> WsDeviceServer
    static Map<Integer, Map<String, Map<String, WsDeviceServer>>> types = new HashMap<>(); // type -> nuid -> ndid -> WsDeviceServer

    String nuid;
    String ndid;
    Integer type;


    static WsDeviceServer fromNdid(String ndid) {
        synchronized (devices) {
            return devices.get(ndid);
        }
    }

    @Override
    protected boolean loginReqCheck(WsMessage message, Response response) {
        if (!super.loginReqCheck(message, response)) {
            return false;
        }

        ConnectReq connectReq = message.getPayload(ConnectReq.class);

        // 检查参数
        /*
        if (!secretKey.equals(Md5Utils.md5(_wsencfront + Md5Utils.md5(String.format("{'nuid':'%s','ndid':'%s','accessToken':'%s'}", nuid, ndid, accessToken)) + _wsencback))) {
            System.out.println("WebSocket connect failed, because parameters invalid.");
            endpoint.permissionDeniedClose();
            return;
        }
        */

        // 检查是否已经认证
        if ((connectReq.accessToken == null) || (connectReq.accessToken.length() <= 0)) {
            System.out.println("WebSocket connect failed, because access token invalid.");
            response.setSuccess(false);
            response.setStatus(Response.Status.InvalidArgument);
            response.setReason("accessToken无效");
            return false;
        }

        AccessTokenInfo accountInfo = ObjectFactory.cacheAware.getAccessTokenInfo(connectReq.accessToken);
        if (accountInfo == null) {
            System.out.println("WebSocket connect failed, because access token maybe expired.");
            response.setSuccess(false);
            response.setStatus(Response.Status.InvalidArgument);
            response.setReason("accessToken无效或者已经过期");
            return false;
        }
        else if (!connectReq.nuid.equals(accountInfo.getNuid()) || !connectReq.ndid.equals(accountInfo.getNdid())) {
            System.out.println("WebSocket connect failed, because information not matched.");
            response.setSuccess(false);
            response.setStatus(Response.Status.InvalidArgument);
            response.setReason("信息不匹配");
            return false;
        }

        this.nuid = connectReq.nuid;
        this.ndid = connectReq.ndid;
        this.type = accountInfo.getType();
        return true;
    }

    @Override
    protected void onConnected() {
        super.onConnected();

        System.out.println(String.format("Device WebSocket Connected. nuid = %s, ndid = %s", nuid, ndid));

        ObjectFactory.cacheAware.addDevice(ndid, nuid, type);

        synchronized (devices) {
            ContainerUtils.put(devices, ndid, this);
            ContainerUtils.put(types, type, nuid, ndid, this);
        }
    }

    @Override
    protected void onDisconnected() {
        super.onDisconnected();

        System.out.println(String.format("Device WebSocket Disconnected. nuid = %s, ndid = %s", nuid, ndid));

        ObjectFactory.cacheAware.removeDevice(ndid);

        synchronized (devices) {
            ContainerUtils.remove(devices, ndid);
            ContainerUtils.remove(types, type, nuid, ndid);
        }
    }

    /*
    public static void request(DeviceType type, String url, String payload, WsResponse response) {
        synchronized (devices) {
            Map<String, Map<String, WsDeviceServer>> map1 = ContainerUtils.get(types, type.value);
            if (map != null) {
                for (Map.Entry<String, Map<String, WsDeviceServer>> entry1 : map1.entrySet()) {
                    for (Map.Entry<String, WsDeviceServer> entry2 : entry1.getValue().entrySet()) {
                        WsMessageDispatcher.request(entry2.getValue(), url, entry2.getKey(), payload, response);
                    }
                }
            }
        }
    }
    public static void request(String ndid, String url, String message, WsResponse response) {
        WsMessageDispatcher.request(ndid, url, message, response);
    }
    */


    static class ConnectReq {
        String nuid;
        String ndid;
        String accessToken;
        String connectTime;
        String secretKey;

        public String getNuid() {
            return nuid;
        }
        public void setNuid(String nuid) {
            this.nuid = nuid;
        }

        public String getNdid() {
            return ndid;
        }
        public void setNdid(String ndid) {
            this.ndid = ndid;
        }

        public String getAccessToken() {
            return accessToken;
        }
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getConnectTime() {
            return connectTime;
        }
        public void setConnectTime(String connectTime) {
            this.connectTime = connectTime;
        }

        public String getSecretKey() {
            return secretKey;
        }
        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }
    }
}
