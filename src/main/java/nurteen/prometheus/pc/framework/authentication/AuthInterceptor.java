package nurteen.prometheus.pc.framework.authentication;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor extends HandlerInterceptorAdapter {
	public static String NURTEEN_REQUEST_ATTRIBUTES_HANDLER_METHOD = "nurteen.request.attributes-HANDLER_METHOD";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute(NURTEEN_REQUEST_ATTRIBUTES_HANDLER_METHOD, handler);
		return super.preHandle(request, response, handler);
	}

}
