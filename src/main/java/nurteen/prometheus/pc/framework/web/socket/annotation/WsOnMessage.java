package nurteen.prometheus.pc.framework.web.socket.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WsOnMessage {
    @AliasFor("url")
    String value() default "";
    @AliasFor("value")
    String url() default "";
}
