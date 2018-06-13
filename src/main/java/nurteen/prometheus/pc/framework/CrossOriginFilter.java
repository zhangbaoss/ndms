package nurteen.prometheus.pc.framework;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// @WebFilter(filterName = "CrossOriginFilter", urlPatterns = "/*")
public class CrossOriginFilter implements Filter {

    public CrossOriginFilter() {
        System.out.println("CrossOriginFilter");
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String origin = request.getHeader("Origin");
        System.out.println("================================================" + origin);
        if ((origin != null) && !origin.isEmpty()) {
            System.out.println("***********************************************************");
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type");
            response.setHeader("Access-Control-Allow-Methods", "POST");
            response.setHeader("Access-Control-Max-Age", "1800");
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
