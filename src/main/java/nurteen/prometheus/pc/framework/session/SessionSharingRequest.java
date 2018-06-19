package nurteen.prometheus.pc.framework.session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import nurteen.prometheus.pc.center.server.service.RedisCacheAware;
import nurteen.prometheus.pc.framework.utils.SpringContextHelper;

public class SessionSharingRequest extends HttpServletRequestWrapper {

	private RedisCacheAware cache;
	
	public SessionSharingRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public HttpSession getSession() {
		//获取cache对象
		cache = (RedisCacheAware) SpringContextHelper.getBean("redisCacheAware");
		
		String jSessionId = null;
		//获取当前登录用户提交的jSessionId
		Cookie[] cookies = getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if ("JSESSIONID".equals(cookie.getName())) {
					jSessionId = cookie.getValue();
				}
			}
		}
		
		//当前用户第一次登录，或者浏览器的cookie被清理掉了，相当于第一次访问
		if (jSessionId == null) {
			HttpSession session = super.getSession();
			SessionSharingSession sss = new SessionSharingSession(session);
			return sss;
		} else {//用户以前登录过
			//从redis中取出数据
			SessionSharingSession sss = cache.getSession(jSessionId);
			if (sss == null) {//session不存在，session过期了
				HttpSession session = super.getSession();
				return new SessionSharingSession(session);
				
			}
			cache.expire(jSessionId, 30);
			return sss;
		}
	}
}
