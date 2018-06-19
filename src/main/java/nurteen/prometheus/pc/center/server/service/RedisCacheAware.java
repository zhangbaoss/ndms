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

    @Override
    public boolean hasAccessToken(String accessToken) {
        boolean exists = false;
        try {
            Jedis jedis = RedisUtils.getJedis();
            exists = jedis.exists(accessToken);
            RedisUtils.releaseJedis(jedis);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }

    @Override
    public AccessTokenInfo getAccessTokenInfo(String accessToken) {
        try {
            List<String> values;

            Jedis jedis = RedisUtils.getJedis();
            values = jedis.hmget(accessToken, "nuid", "ndid", "type");
            RedisUtils.releaseJedis(jedis);

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

    @Override
    public void updateAccessToken(String accessToken, int timeout) {
        try {
            Jedis jedis = RedisUtils.getJedis();
            jedis.expire(accessToken, timeout);
            RedisUtils.releaseJedis(jedis);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAccessToken(String accessToken, String nuid, String ndid, DeviceType type, int timeout) {
        try {
            Jedis jedis = RedisUtils.getJedis();
            jedis.hmset(accessToken, ContainerUtils.make("nuid", nuid).put("ndid", ndid).put("type", Integer.toString(type.getValue())).get());
            jedis.expire(accessToken, timeout);
            RedisUtils.releaseJedis(jedis);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addDevice(String ndid, String nuid, int type) {
        try {
            Jedis jedis = RedisUtils.getJedis();
            jedis.hmset(ndid, ContainerUtils.make("nuid", nuid).put("type", Integer.toString(type)).put("serverNdid", ServerProperties.getNdid()).put("serverAddress", configProperties.getServerAddress()).get());
            RedisUtils.releaseJedis(jedis);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeDevice(String ndid) {
        try {
            Jedis jedis = RedisUtils.getJedis();
            jedis.del(ndid);
            RedisUtils.releaseJedis(jedis);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public DeviceOnlineInfo findDeviceOnlineInfo(String ndid) {
        try {
            Jedis jedis = RedisUtils.getJedis();
            List<String> values = jedis.hmget(ndid, "nuid", "type", "serverNdid", "serverAddress");
            RedisUtils.releaseJedis(jedis);

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
