package nurteen.prometheus.pc.framework.utils;

import nurteen.prometheus.pc.framework.Constants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    public static String getAccessToken(HttpServletRequest request) {
        return getCookie(request, Constants.ACCESS_TOKEN);
    }
    public static String getCookie(HttpServletRequest request, String name) {
        for (Cookie cookie: request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static void setAccessToken(HttpServletResponse response, String accessToken) {
        setCookie(response, Constants.ACCESS_TOKEN, accessToken);
    }
    public static void setCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        response.addCookie(cookie);
    }
}
