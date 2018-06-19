package nurteen.prometheus.pc.framework.utils;

import nurteen.prometheus.pc.framework.ObjectFactory;
import redis.clients.jedis.Jedis;

public class RedisUtils {

    public static Jedis getJedis(){
    	return ObjectFactory.redisAware.getJedis();
    }
}
