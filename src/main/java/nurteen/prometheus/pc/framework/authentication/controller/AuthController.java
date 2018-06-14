package nurteen.prometheus.pc.framework.authentication.controller;

import nurteen.prometheus.pc.framework.*;
import nurteen.prometheus.pc.framework.authentication.argument.NaLoginArgument;
import nurteen.prometheus.pc.framework.authentication.argument.WXaLoginArgument;
import nurteen.prometheus.pc.framework.authentication.response.WXaLoginResponse;
import nurteen.prometheus.pc.framework.entities.DeviceInfo;
import nurteen.prometheus.pc.framework.entities.DeviceType;
import nurteen.prometheus.pc.framework.entities.ThirdpartyAccountType;
import nurteen.prometheus.pc.framework.entities.UserInfo;
import nurteen.prometheus.pc.framework.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@CrossOrigin(origins = {"http://localhost:4200", "http://192.168.1.7:4200", "*"}, allowCredentials = "true")
public class AuthController {
    @Autowired
    protected ServerConfigProperties configProperties;

    @RequestMapping(path = "/devices/authentication/nalogin")
    public @ResponseBody Response login(HttpServletRequest request, HttpServletResponse response, @RequestBody NaLoginArgument args) {
        /*
        NaLoginResponse reps = new NaLoginResponse();
        String accessToken = request.getSession().getId();
        CookieUtils.setAccessToken(response, accessToken);
        return Response.ok("ok", reps);
        */
        return Response.ok("ok");
    }

    @RequestMapping(path = "/devices/authentication/wxalogin", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody Response wxalogin(HttpServletRequest request, HttpServletResponse response, @RequestBody WXaLoginArgument argument) throws Exception {
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
            deviceInfo = new DeviceInfo(DeviceType.App_Browser, argument.getDevice().getDevicePlatform(), argument.getDevice().getHid());
        }
        else {
            deviceInfo = ObjectFactory.storageAware.fromHid(argument.getDevice().getHid());
            if (deviceInfo != null) {
                ObjectFactory.storageAware.insertNew(userInfo.getNuid(), deviceInfo.getNdid(), argument.getDevice().getName());
            }
            else {
                deviceInfo = new DeviceInfo(argument.getDevice().getDeviceType(), argument.getDevice().getDevicePlatform(), argument.getDevice().getHid());
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
