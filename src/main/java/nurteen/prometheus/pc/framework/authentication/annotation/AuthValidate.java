package nurteen.prometheus.pc.framework.authentication.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthValidate {
    @AliasFor("validate")
    boolean value() default true;
    @AliasFor("value")
    boolean validate() default true;
}
