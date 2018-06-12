package nurteen.prometheus.pc.framework.exception.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nurteen.prometheus.pc.framework.Response;
import nurteen.prometheus.pc.framework.exception.ExceptionResponse;
import nurteen.prometheus.pc.framework.exception.InvalidArgumentException;
import nurteen.prometheus.pc.framework.exception.PermissionDeniedException;
import nurteen.prometheus.pc.framework.utils.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler
	public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		exceptionHandler(response, Response.exceptionOccurred(ex.toString(), new ExceptionResponse(ex.getStackTrace())));
	}
	@ExceptionHandler(InvalidArgumentException.class)
	public void invalidArgumentExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		exceptionHandler(response, Response.invalidArgument(ex.getMessage()));
	}
	@ExceptionHandler(PermissionDeniedException.class)
	public void permissionDeniedExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		exceptionHandler(response, Response.permissionDenied(ex.getMessage()));
	}

	private void exceptionHandler(HttpServletResponse servletResponse, Response response) {
		try {
			servletResponse.setStatus(HttpStatus.OK.value());
			servletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			// servletResponse.setHeader("Cache-Control", "no-cache, must-revalidate");
			servletResponse.getWriter().write(JsonUtils.toJSON(response));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
