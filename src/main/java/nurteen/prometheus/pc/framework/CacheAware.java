package nurteen.prometheus.pc.framework;

import nurteen.prometheus.pc.framework.entities.AccessTokenInfo;
import nurteen.prometheus.pc.framework.entities.DeviceOnlineInfo;
import nurteen.prometheus.pc.framework.entities.DeviceType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public abstract class CacheAware {

    @Autowired
    protected ServerConfigProperties configProperties;

    public String genAccessToken(HttpServletRequest request) {
        return request.getSession().getId();
    }

    public abstract boolean hasAccessToken(String accessToken);
    public abstract AccessTokenInfo getAccessTokenInfo(String accessToken);
    public abstract void updateAccessToken(String accessToken, int timeout);
    public abstract void updateAccessToken(String accessToken, String nuid, String ndid, DeviceType type, int timeout);

    public abstract void addDevice(String ndid, String nuid, int type);
    public abstract void removeDevice(String ndid);
    public abstract DeviceOnlineInfo findDeviceOnlineInfo(String ndid);
}
