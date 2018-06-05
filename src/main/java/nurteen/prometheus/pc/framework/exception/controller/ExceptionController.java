package nurteen.prometheus.pc.framework.exception.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nurteen.prometheus.pc.framework.exception.PermissionDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler
	public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		try {
			response.setStatus(HttpStatus.OK.value());
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			// response.setHeader("Cache-Control", "no-cache, must-revalidate");
			
			// String jsonValue = Http.exceptionOccurred(ex.getMessage()).toString();
			// response.getWriter().write(jsonValue);
			response.getWriter().write("{gggggggggggggggg");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	@ExceptionHandler(PermissionDeniedException.class)
	public void permissionDeniedExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		try {
			response.setStatus(HttpStatus.OK.value());
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			// response.setHeader("Cache-Control", "no-cache, must-revalidate");

			// String jsonValue = Http.exceptionOccurred(ex.getMessage()).toString();
			// response.getWriter().write(jsonValue);
			response.getWriter().write("PermissionDeniedException");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
