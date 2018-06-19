package nurteen.prometheus.pc.framework;

import nurteen.prometheus.pc.framework.utils.RedisUtils;
import nurteen.prometheus.pc.framework.web.socket.WsMessageDispatcher;

public class ObjectFactory {
    public static WsMessageDispatcher messageDispatcher;
    public static CacheAware cacheAware;
    public static StorageAware storageAware;
    public static RedisUtils redisUtils;


}
