package nurteen.prometheus.pc.center.server.controller;

import nurteen.prometheus.pc.framework.authentication.BaseController;
import nurteen.prometheus.pc.framework.authentication.annotation.AuthValidate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@AuthValidate
public class AppMobileController extends BaseController {

    @RequestMapping(path = "/app/mobile/resource/search")
    public @ResponseBody Object resourceSearch() {
        return "{}";
    }
}
