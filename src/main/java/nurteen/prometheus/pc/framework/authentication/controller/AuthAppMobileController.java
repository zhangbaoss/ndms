package nurteen.prometheus.pc.framework.authentication.controller;

import nurteen.prometheus.pc.framework.AccountDBAware;
import nurteen.prometheus.pc.framework.Constants;
import nurteen.prometheus.pc.framework.RedisAware;
import nurteen.prometheus.pc.framework.authentication.entities.NaLoginArgs;
import nurteen.prometheus.pc.framework.authentication.entities.NaLoginReps;
import nurteen.prometheus.pc.framework.authentication.entities.WXaLoginArgs;
import nurteen.prometheus.pc.framework.authentication.entities.WXaLoginReps;
import nurteen.prometheus.pc.framework.HttpReps;
import nurteen.prometheus.pc.framework.entities.AccountInfo;
import nurteen.prometheus.pc.framework.utils.CookieUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
public class AuthAppMobileController {

    @RequestMapping(path = "/nalogin")
    public @ResponseBody HttpReps login(HttpServletRequest request, HttpServletResponse response, @RequestBody NaLoginArgs args) {
        /*
        NaLoginReps reps = new NaLoginReps();
        String accessToken = request.getSession().getId();
        CookieUtils.setAccessToken(response, accessToken);
        return HttpReps.ok("ok", reps);
        */
        return HttpReps.ok("ok");
    }

    @RequestMapping(path = "/app/mobile/authentication/wxalogin", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody HttpReps wxalogin(HttpServletRequest request, HttpServletResponse response, @RequestBody WXaLoginArgs args) {
        args.validate();

        // 使用WX接口进行认证

        // 判断是否为首次登录
        AccountInfo accountInfo = AccountDBAware.getAccountDBAware().find(AccountInfo.Type.WX, args.getAccount());
        if (accountInfo == null) {
            accountInfo = new AccountInfo(AccountInfo.Type.WX, args.getAccount(), null, args.getNickname(), args.getHeadimg());
            AccountDBAware.getAccountDBAware().insertNew(accountInfo);
        }

        String accessToken = RedisAware.getRedisAware().genAccessToken(request);
        RedisAware.getRedisAware().updateAccessToken(accessToken, accountInfo.getNuid(), args.getNdid(), Constants.DEFAULT_SESSION_TIMEOUT);

        WXaLoginReps reps = new WXaLoginReps(accountInfo.getNuid(), args.getNdid(), accessToken, accountInfo.getNickname(), accountInfo.getHeadimg());
        CookieUtils.setAccessToken(response, accessToken);
        return HttpReps.ok("ok", reps);
    }
}
