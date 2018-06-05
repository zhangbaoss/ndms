package nurteen.prometheus.pc.center.server.service;

import org.springframework.stereotype.Service;

// @Service
public class RedisAware extends nurteen.prometheus.pc.framework.RedisAware {
    @Override
    public boolean hasAccessToken(String accessToken) {
        return false;
    }

    @Override
    public void updateAccessToken(String accessToken, Integer timeout) {

    }

    @Override
    public void updateAccessToken(String accessToken, String nuid, String ndid, Integer timeout) {

    }
}
