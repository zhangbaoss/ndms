package nurteen.prometheus.pc.framework;

import javax.servlet.http.HttpServletRequest;

public abstract class RedisAware {
    static RedisAware redisAware;

    public static RedisAware getRedisAware() {
        return redisAware;
    }

    public String genAccessToken(HttpServletRequest request) {
        return request.getSession().getId();
    }

    public abstract boolean hasAccessToken(String accessToken);
    public abstract void updateAccessToken(String accessToken, Integer timeout);
    public abstract void updateAccessToken(String accessToken, String nuid, String ndid, Integer timeout);
}
