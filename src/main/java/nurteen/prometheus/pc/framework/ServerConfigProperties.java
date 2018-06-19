package nurteen.prometheus.pc.framework;

public class ServerConfigProperties extends ServerProperties {
	String idGenPrefix;
    String serverAddress;
    Integer wsRequestTimeout;
    Integer wsForwardTimeout;
    String redisHost;
    String redisPassword;
    Integer redisPort;
    Integer redisTimeout;
    Integer redisPoolMaxTotal;
    Integer redisPoolMaxIdle;
    Integer redisPoolMaxWaitMillis;
    Boolean redisPoolTestOnBorrow;

    public ServerConfigProperties() {
        this.idGenPrefix = "11";
        this.wsRequestTimeout = this.wsForwardTimeout = 30000;
    }

    public String getIdGenPrefix() {
        return idGenPrefix;
    }
    public void setIdGenPrefix(String idGenPrefix) {
        this.idGenPrefix = idGenPrefix;
    }

    public String getServerAddress() {
        return serverAddress;
    }
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public Integer getWsRequestTimeout() {
        return wsRequestTimeout;
    }
    public void setWsRequestTimeout(Integer wsRequestTimeout) {
        this.wsRequestTimeout = wsRequestTimeout;
    }

    public Integer getWsForwardTimeout() {
        return wsForwardTimeout;
    }
    public void setWsForwardTimeout(Integer wsForwardTimeout) {
        this.wsForwardTimeout = wsForwardTimeout;
    }

	public String getRedisHost() {
		return redisHost;
	}

	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}

	public String getRedisPassword() {
		return redisPassword;
	}

	public void setRedisPassword(String redisPassword) {
		this.redisPassword = redisPassword;
	}

	public Integer getRedisPort() {
		return redisPort;
	}

	public void setRedisPort(Integer redisPort) {
		this.redisPort = redisPort;
	}

	public Integer getRedisTimeout() {
		return redisTimeout;
	}

	public void setRedisTimeout(Integer redisTimeout) {
		this.redisTimeout = redisTimeout;
	}

	public Integer getRedisPoolMaxTotal() {
		return redisPoolMaxTotal;
	}

	public void setRedisPoolMaxTotal(Integer redisPoolMaxTotal) {
		this.redisPoolMaxTotal = redisPoolMaxTotal;
	}

	public Integer getRedisPoolMaxIdle() {
		return redisPoolMaxIdle;
	}

	public void setRedisPoolMaxIdle(Integer redisPoolMaxIdle) {
		this.redisPoolMaxIdle = redisPoolMaxIdle;
	}

	public Integer getRedisPoolMaxWaitMillis() {
		return redisPoolMaxWaitMillis;
	}

	public void setRedisPoolMaxWaitMillis(Integer redisPoolMaxWaitMillis) {
		this.redisPoolMaxWaitMillis = redisPoolMaxWaitMillis;
	}

	public Boolean getRedisPoolTestOnBorrow() {
		return redisPoolTestOnBorrow;
	}

	public void setRedisPoolTestOnBorrow(Boolean redisPoolTestOnBorrow) {
		this.redisPoolTestOnBorrow = redisPoolTestOnBorrow;
	}
}
