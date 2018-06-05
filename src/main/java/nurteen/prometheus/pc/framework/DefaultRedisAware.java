package nurteen.prometheus.pc.framework;

import nurteen.prometheus.pc.framework.entities.RedisAccountInfo;

import java.util.HashMap;
import java.util.Map;

public class DefaultRedisAware extends RedisAware {
    Map<String, DefaultRedisAccountInfo> redisAccountInfoMap = new HashMap<>();

    @Override
    public synchronized boolean hasAccessToken(String accessToken) {
        DefaultRedisAccountInfo redisAccountInfo = redisAccountInfoMap.get(accessToken);
        if (redisAccountInfo == null) {
            return false;
        }

        Long curTime = System.nanoTime() / 1000 / 1000;
        if ((curTime - redisAccountInfo.lastAccessTime) > redisAccountInfo.timeout) {
            redisAccountInfoMap.remove(accessToken);
            return false;
        }
        return true;
    }

    @Override
    public synchronized void updateAccessToken(String accessToken, Integer timeout) {
        DefaultRedisAccountInfo redisAccountInfo = redisAccountInfoMap.get(accessToken);
        if (redisAccountInfo != null) {
            redisAccountInfo.lastAccessTime = System.nanoTime() / 1000 / 1000;
        }
    }

    @Override
    public synchronized void updateAccessToken(String accessToken, String nuid, String ndid, Integer timeout) {
        DefaultRedisAccountInfo redisAccountInfo = redisAccountInfoMap.get(accessToken);
        if (redisAccountInfo == null) {
            redisAccountInfo = new DefaultRedisAccountInfo(nuid, ndid, timeout);
            redisAccountInfoMap.put(accessToken, redisAccountInfo);
        }
        else {
            redisAccountInfo.setNuid(nuid);
            redisAccountInfo.setNdid(ndid);
            redisAccountInfo.lastAccessTime = System.nanoTime() / 1000 / 1000;
            redisAccountInfo.timeout = timeout * 60 * 1000L;
        }
    }

    class DefaultRedisAccountInfo extends RedisAccountInfo {
        Long lastAccessTime;
        Long timeout;

        DefaultRedisAccountInfo(String nuid, String ndid, Integer timeout) {
            super(nuid, ndid);
            this.lastAccessTime = System.nanoTime() / 1000 / 1000;
            this.timeout = timeout * 60 * 1000L;
        }
    }
}
