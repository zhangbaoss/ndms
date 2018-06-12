package nurteen.prometheus.pc.framework.authentication.controller;

import nurteen.prometheus.pc.framework.*;
import nurteen.prometheus.pc.framework.authentication.AuthBaseController;
import nurteen.prometheus.pc.framework.authentication.argument.NaLoginArgument;
import nurteen.prometheus.pc.framework.authentication.argument.WXaLoginArgument;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
public class AuthAppMobileController extends AuthBaseController {

    @RequestMapping(path = "/nalogin")
    public @ResponseBody
    Response login(HttpServletRequest request, HttpServletResponse response, @RequestBody NaLoginArgument args) {
        /*
        NaLoginResponse reps = new NaLoginResponse();
        String accessToken = request.getSession().getId();
        CookieUtils.setAccessToken(response, accessToken);
        return Response.ok("ok", reps);
        */
        return Response.ok("ok");
    }

    @RequestMapping(path = "/app/mobile/authentication/wxalogin", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    Response wxalogin(HttpServletRequest request, HttpServletResponse response, @RequestBody WXaLoginArgument argument) throws Exception {
        return super.wxalogin(request, response, argument);
    }
}
