package nurteen.prometheus.pc.center.server.service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nurteen.prometheus.pc.framework.CacheAware;
import nurteen.prometheus.pc.framework.ServerProperties;
import nurteen.prometheus.pc.framework.entities.AccessTokenInfo;
import nurteen.prometheus.pc.framework.entities.DeviceOnlineInfo;
import nurteen.prometheus.pc.framework.entities.DeviceType;
import nurteen.prometheus.pc.framework.service.MemoryCacheService;
import nurteen.prometheus.pc.framework.utils.ContainerUtils;

@Service
public class MemoryCacheAware extends CacheAware {

	@Autowired
	private MemoryCacheService cache;
	
	@PostConstruct
	public void test() {
		
		ScheduledExecutorService schedulePool = Executors.newScheduledThreadPool(3);
		schedulePool.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					cache.hmset("accessToken"+i, ContainerUtils.make("nuid", "nuid"+i).put("ndid", "ndid"+i).put("type", "type"+i).get());
				}
				System.out.println("插入成功!");
			}
		}, 1, 2, TimeUnit.SECONDS);
		
		schedulePool.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					cache.del("accessToken"+i);
				}
				System.out.println("删除成功！");
			}
		}, 1, 2, TimeUnit.SECONDS);
		
		schedulePool.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					boolean a = cache.exists("accessToken"+(100-i));
					System.out.println("是否存在：" + a);
				}
			}
		}, 1, 2, TimeUnit.SECONDS);
		
		
//		String key = "accessToken";
//		updateAccessToken(key, "nuid", "ndid", DeviceType.Controller_Pc, 60*60);
//		boolean a = hasAccessToken(key);
//		AccessTokenInfo ati = getAccessTokenInfo(key);
//		System.out.println(ati.getNuid());
//		updateAccessToken(key, 60*61);
//		
//		addDevice("ndid1", "nuid1", 1);
//		DeviceOnlineInfo doi = findDeviceOnlineInfo("ndid1");
//		System.out.println(doi);
//		removeDevice("ndid1");
	}
	
	/**
	 * 检查给定的accessToken是否存在
	 */
	public boolean hasAccessToken(String accessToken) {
		boolean exists = false;
        try {
            exists = cache.exists(accessToken);
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
		try {
            List<String> values = cache.hmget(accessToken, "nuid", "ndid", "type");

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
		try {
            cache.expire(accessToken, timeout);
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
		try {
            cache.hmset(accessToken, ContainerUtils.make("nuid", nuid).put("ndid", ndid).put("type", Integer.toString(type.getValue())).get());
            cache.expire(accessToken, timeout);
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
		try {
            cache.hmset(ndid, ContainerUtils.make("nuid", nuid).put("type", Integer.toString(type)).put("serverNdid", ServerProperties.getNdid()).put("serverAddress", configProperties.getServerAddress()).get());
//            cache.hmset(ndid, ContainerUtils.make("nuid", nuid).put("type", Integer.toString(type)).put("serverNdid", "serverNdid1").put("serverAddress", "serverAddress1").get());
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
		try {
            cache.del(ndid);
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
		try {
            List<String> values = cache.hmget(ndid, "nuid", "type", "serverNdid", "serverAddress");

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
