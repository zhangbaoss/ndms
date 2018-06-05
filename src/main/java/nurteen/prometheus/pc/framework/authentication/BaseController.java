package nurteen.prometheus.pc.framework.authentication;

import nurteen.prometheus.pc.framework.authentication.annotation.AuthValidate;
import nurteen.prometheus.pc.framework.exception.PermissionDeniedException;
import nurteen.prometheus.pc.framework.utils.CookieUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {

    @ModelAttribute
    public void authValidate(HttpServletRequest request, HttpServletResponse response) throws PermissionDeniedException {
        HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute(AuthInterceptor.NURTEEN_REQUEST_ATTRIBUTES_HANDLER_METHOD);
        request.removeAttribute(AuthInterceptor.NURTEEN_REQUEST_ATTRIBUTES_HANDLER_METHOD);

        System.out.println(handlerMethod.getBeanType().getName() + "." + handlerMethod.getMethod().getName());

        // 检查注解
        AuthValidate authValidate = handlerMethod.getMethodAnnotation(AuthValidate.class);
        if (authValidate == null) {
            authValidate = handlerMethod.getBeanType().getAnnotation(AuthValidate.class);
            if ((authValidate == null) || !authValidate.validate()) {
                return;
            }
        }
        else if (!authValidate.validate()) {
            return;
        }

        // 检查是否登录过
        String accessToken = CookieUtils.getAccessToken(request);
        if (accessToken == null) {
            throw new PermissionDeniedException();
        }

        /*
        // 检查Redis是否过期
        else if () {

        }
        */
    }
}
