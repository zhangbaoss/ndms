package nurteen.prometheus.pc.framework.cache;

import java.util.Map;

public class MemoryCacheBean {

	private Map<String, String> value;
	
	private int timeout;
	
	private long destoryTime;

	public MemoryCacheBean() {
		super();
	}

	public MemoryCacheBean(Map<String, String> value, int timeout) {
		super();
		this.value = value;
		this.timeout = timeout <= 0 ? 0 : timeout * 1000;
		this.destoryTime = System.currentTimeMillis() + this.timeout;
	}

	public Map<String, String> getValue() {
		return value;
	}

	public void setValue(Map<String, String> value) {
		this.value = value;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout * 1000;
		this.destoryTime = System.currentTimeMillis() + this.timeout;
	}

	public long getDestoryTime() {
		return destoryTime;
	}

	public void setDestoryTime(long destoryTime) {
		this.destoryTime = destoryTime;
	}

}
