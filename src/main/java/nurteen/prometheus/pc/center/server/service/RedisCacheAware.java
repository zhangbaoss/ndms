package nurteen.prometheus.pc.center.server.service;

import nurteen.prometheus.pc.framework.CacheAware;
import nurteen.prometheus.pc.framework.ServerProperties;
import nurteen.prometheus.pc.framework.entities.AccessTokenInfo;
import nurteen.prometheus.pc.framework.entities.DeviceOnlineInfo;
import nurteen.prometheus.pc.framework.entities.DeviceType;
import nurteen.prometheus.pc.framework.utils.ContainerUtils;
import nurteen.prometheus.pc.framework.utils.RedisUtils;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class RedisCacheAware extends CacheAware {
	
	/**
	 * 检查给定的accessToken是否存在
	 */
    @Override
    public boolean hasAccessToken(String accessToken) {
        boolean exists = false;
        try(Jedis jedis = RedisUtils.getJedis()) {
            exists = jedis.exists(accessToken);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
	 * 返回对应accessToken的map表中"nuid", "ndid", "type"所对应的值。
	 * 如果给定的域（即"nuid", "ndid", "type"）不存在于哈希表（map），那么返回一个 null 值。
	 */
    @Override
    public AccessTokenInfo getAccessTokenInfo(String accessToken) {
    	try(Jedis jedis = RedisUtils.getJedis()) {
            List<String> values = jedis.hmget(accessToken, "nuid", "ndid", "type");

            if (ContainerUtils.notNull(values, 3)) {
                return new AccessTokenInfo(values.get(0), values.get(1), Integer.valueOf(values.get(2)));
            }
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新对应的accessToken的失效时间
     */
    @Override
    public void updateAccessToken(String accessToken, int timeout) {
    	try(Jedis jedis = RedisUtils.getJedis()) {
            jedis.expire(accessToken, timeout);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将用户所对应的信息存入redis中
	 * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
	 * 此命令会覆盖哈希表中已存在的域。
	 * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作。
	 * 一个key对应一个map集合
	 */
    @Override
    public void updateAccessToken(String accessToken, String nuid, String ndid, DeviceType type, int timeout) {
        try(Jedis jedis = RedisUtils.getJedis()) {
            jedis.hmset(accessToken, ContainerUtils.make("nuid", nuid).put("ndid", ndid).put("type", Integer.toString(type.getValue())).get());
            jedis.expire(accessToken, timeout);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将设备信息存入redis中
     */
    @Override
    public void addDevice(String ndid, String nuid, int type) {
    	try(Jedis jedis = RedisUtils.getJedis()) {
            jedis.hmset(ndid, ContainerUtils.make("nuid", nuid).put("type", Integer.toString(type)).put("serverNdid", ServerProperties.getNdid()).put("serverAddress", configProperties.getServerAddress()).get());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设备掉线即将设备信息从redis中移除
     */
    @Override
    public void removeDevice(String ndid) {
    	try(Jedis jedis = RedisUtils.getJedis()) {
            jedis.del(ndid);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找在线设备信息
     */
    @Override
    public DeviceOnlineInfo findDeviceOnlineInfo(String ndid) {
    	try(Jedis jedis = RedisUtils.getJedis()) {
            List<String> values = jedis.hmget(ndid, "nuid", "type", "serverNdid", "serverAddress");

            if (ContainerUtils.notNull(values, 4)) {
                return new DeviceOnlineInfo(values.get(0), Integer.valueOf(values.get(1)), values.get(2), values.get(3));
            }
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
