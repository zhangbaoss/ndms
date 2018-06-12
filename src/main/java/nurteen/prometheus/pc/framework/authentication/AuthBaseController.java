package nurteen.prometheus.pc.framework.authentication;

import nurteen.prometheus.pc.framework.ObjectFactory;
import nurteen.prometheus.pc.framework.Constants;
import nurteen.prometheus.pc.framework.Response;
import nurteen.prometheus.pc.framework.ServerConfigProperties;
import nurteen.prometheus.pc.framework.authentication.argument.WXaLoginArgument;
import nurteen.prometheus.pc.framework.authentication.response.WXaLoginResponse;
import nurteen.prometheus.pc.framework.entities.DeviceInfo;
import nurteen.prometheus.pc.framework.entities.DeviceType;
import nurteen.prometheus.pc.framework.entities.ThirdpartyAccountType;
import nurteen.prometheus.pc.framework.entities.UserInfo;
import nurteen.prometheus.pc.framework.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthBaseController {

    @Autowired
    protected ServerConfigProperties configProperties;

    public Response wxalogin(HttpServletRequest request, HttpServletResponse response, WXaLoginArgument argument) throws Exception {
        argument.validate();

        // 使用WX接口进行认证

        // 判断是否为首次登录
        UserInfo userInfo = ObjectFactory.storageAware.fromThirdpartyAccount(ThirdpartyAccountType.WX, argument.getProduct());
        if (userInfo == null) {
            userInfo = new UserInfo("", "", "");
            ObjectFactory.storageAware.insertNewFromThirdparty(userInfo, ThirdpartyAccountType.WX, argument.getProduct());
        }

        DeviceInfo deviceInfo;
        if (argument.getDevice().getType() == DeviceType.App_Browser.getValue()) {
            deviceInfo = new DeviceInfo(DeviceType.App_Browser.getValue(), argument.getDevice().getPlatform(), argument.getDevice().getHid());
        }
        else {
            deviceInfo = ObjectFactory.storageAware.fromHid(argument.getDevice().getHid());
            if (deviceInfo != null) {
                ObjectFactory.storageAware.insertNew(userInfo.getNuid(), deviceInfo.getNdid(), argument.getDevice().getName());
            }
            else {
                deviceInfo = new DeviceInfo(argument.getDevice().getType(), argument.getDevice().getPlatform(), argument.getDevice().getHid());
                ObjectFactory.storageAware.insertNew(deviceInfo);
                ObjectFactory.storageAware.insertNew(userInfo.getNuid(), deviceInfo.getNdid(), argument.getDevice().getName());
            }
        }

        String accessToken = ObjectFactory.cacheAware.genAccessToken(request);
        ObjectFactory.cacheAware.updateAccessToken(accessToken, userInfo.getNuid(), deviceInfo.getNdid(), argument.getDevice().getType(), Constants.DEFAULT_SESSION_TIMEOUT);

        WXaLoginResponse loginResponse = new WXaLoginResponse(userInfo.getNuid(), deviceInfo.getNdid(), accessToken, userInfo.getNickname(), userInfo.getHeadimg());
        CookieUtils.setAccessToken(response, accessToken);
        return Response.ok("ok", loginResponse);
    }
}
