package nurteen.prometheus.pc.framework;

import nurteen.prometheus.pc.framework.service.RedisConnectionService;
import nurteen.prometheus.pc.framework.web.socket.WsMessageDispatcher;
import nurteen.prometheus.pc.framework.web.socket.annotation.WsController;
import nurteen.prometheus.pc.framework.web.socket.annotation.WsOnMessage;
import org.springframework.beans.BeansException;

import java.lang.reflect.Method;

public class BeanPostProcessor implements org.springframework.beans.factory.config.BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("对象：" + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    	if (bean.getClass().isAssignableFrom(RedisConnectionService.class)) {
    		ObjectFactory.redisConnectionService = (RedisConnectionService) bean;
    	} else if (CacheAware.class.isAssignableFrom(bean.getClass())) {
            ObjectFactory.cacheAware = (CacheAware) bean;
        } else if (StorageAware.class.isAssignableFrom(bean.getClass())) {
            ObjectFactory.storageAware = (StorageAware) bean;
        } else if (bean.getClass().isAssignableFrom(WsMessageDispatcher.class)) {
            ObjectFactory.messageDispatcher = (WsMessageDispatcher) bean;
        } else if (bean.getClass().getAnnotation(WsController.class) != null) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                WsOnMessage wsOnMessage = method.getAnnotation(WsOnMessage.class);
                if ((wsOnMessage != null) && (wsOnMessage.url().length() > 0)) {
                    WsMessageDispatcher.addMessageHandler(wsOnMessage.url(), bean, method);
                }/* else if (method.getAnnotation(WsOnOpen.class) != null) {
                    WsMessageDispatcher.addOnOpenHandler(bean, method);
                } else if (method.getAnnotation(WsOnClose.class) != null) {
                    WsMessageDispatcher.addOnCloseHandler(bean, method);
                }
                */
            }
        }
        return bean;
    }
}
