package nurteen.prometheus.pc.framework;

import nurteen.prometheus.pc.framework.web.socket.WsMsgDispatcher;
import nurteen.prometheus.pc.framework.web.socket.annotation.WsController;
import nurteen.prometheus.pc.framework.web.socket.annotation.WsOnClose;
import nurteen.prometheus.pc.framework.web.socket.annotation.WsOnMessage;
import nurteen.prometheus.pc.framework.web.socket.annotation.WsOnOpen;
import org.springframework.beans.BeansException;

import java.lang.reflect.Method;

public class BeanPostProcessor implements org.springframework.beans.factory.config.BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // System.out.println(beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (RedisAware.class.isAssignableFrom(bean.getClass())) {
            RedisAware.redisAware = (RedisAware) bean;
        }
        else if (AccountDBAware.class.isAssignableFrom(bean.getClass())) {
            AccountDBAware.accountDBAware = (AccountDBAware) bean;
        }
        else if (bean.getClass().getAnnotation(WsController.class) != null) {
            for (Method method: bean.getClass().getDeclaredMethods()) {
                WsOnMessage wsOnMessage = method.getAnnotation(WsOnMessage.class);
                if ((wsOnMessage != null) && (wsOnMessage.url().length() > 0)) {
                    WsMsgDispatcher.addMessageHandler(wsOnMessage.url(), bean, method);
                }
                else if (method.getAnnotation(WsOnOpen.class) != null) {
                    WsMsgDispatcher.addOnOpenHandler(bean, method);
                }
                else if (method.getAnnotation(WsOnClose.class) != null) {
                    WsMsgDispatcher.addOnCloseHandler(bean, method);
                }
            }
        }
        return bean;
    }
}
