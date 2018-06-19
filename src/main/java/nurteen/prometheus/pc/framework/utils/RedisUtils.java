package nurteen.prometheus.pc.framework.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nurteen.prometheus.pc.framework.ServerConfigProperties;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

@Service
public class RedisUtils {
	
	@Autowired
    protected ServerConfigProperties configProperties;
	
	JedisPool jedisPool;
	
	public void initJedisPool() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(configProperties.getRedisPoolMaxTotal());
		config.setMaxIdle(configProperties.getRedisPoolMaxIdle());
		config.setMaxWaitMillis(configProperties.getRedisPoolMaxWaitMillis());
		config.setTestOnBorrow(configProperties.getRedisPoolTestOnBorrow());
		jedisPool = new JedisPool(config, configProperties.getRedisHost(), configProperties.getRedisPort(),
				configProperties.getRedisTimeout(), configProperties.getRedisPassword());
	}

    public Jedis getJedis(){
    	Jedis jedis = null;
		try {
            jedis = jedisPool.getResource();
        } catch (JedisConnectionException e) {
        	String message = e.getMessage().trim();
        	if("Could not get a resource from the pool".equalsIgnoreCase(message)){
        		System.out.println("++++++++++请检查你的redis服务++++++++");
        		System.out.println("|①.请检查是否安装redis服务|");
        		System.out.println("|②.请检查redis 服务是否启动。|");
        		System.out.println("|③.请检查redis启动是否有密码。|");
        		System.out.println("|④.请检查redis启动端口是否有变化（默认6379）|");
        		
        		System.out.println("项目退出中....生产环境中，请删除这些东西。我来自。RedisUtils.java line:43");
        		System.exit(0);//停止项目
        	}
        	throw new JedisConnectionException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
		return jedis;
    }

    public void releaseJedis(Jedis jedis){
        jedis.close();
    }
}
