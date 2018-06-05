package nurteen.prometheus.pc.framework.web.socket.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WsOnOpen {
}
