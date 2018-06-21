package nurteen.prometheus.pc.framework.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import nurteen.prometheus.pc.framework.cache.MemoryCacheBean;
import nurteen.prometheus.pc.framework.utils.ContainerUtils;

@Service
public class MemoryCacheService {

	private Map<String, MemoryCacheBean> caches = new ConcurrentHashMap<>();
//	private Timer timer = new Timer();
	/**默认缓存过期时间*/
	private static final int DEFUALT_TIMEOUT = 3600;
	
	/**
	 * 
	 * @Description: 添加数据到缓存中
	 * @param key
	 * @param value
	 * @return void
	 * @date 2018年6月21日 下午2:39:35 
	 * @throws 
	 * @author zhangb
	 */
	public void hmset(String key, Map<String, String> value) {
		valiateKey(key);
		caches.put(key, new MemoryCacheBean(value, DEFUALT_TIMEOUT));
		/**添加自动删除key功能*/
//		timer.schedule(new CleanWorkerTask(key), DEFUALT_TIMEOUT);
	}
	
	/**
	 * 
	 * @Description: 从缓存中取数据，返回对应key值的map集合
	 * @param key
	 * @return Map<String,String>
	 * @date 2018年6月21日 下午5:35:41 
	 * @throws 
	 * @author zhangb
	 */
	public Map<String, String> hmget(String key) {
		valiateKey(key);
		if (caches.isEmpty()) {
			return null;
		}
		MemoryCacheBean cache = caches.get(key);
		if (cache == null) {
			return null;
		}
		if (cache.getTimeout() <= 0 || cache.getDestoryTime() > System.currentTimeMillis()) {
			return cache.getValue();
		}
		//如果cache已经过期,将其删除
		caches.remove(key);
		return null;
	}
	
	/**
	 * 
	 * @Description: 从缓存中取数据，对应key值的map集合中string数组对应的value值
	 * @param key map集合对应的key
	 * @param strings 从map集合中取出来的值所对应的key
	 * @return List<String>
	 * @date 2018年6月21日 下午5:36:48 
	 * @author zhangb
	 */
	public List<String> hmget(String key, String ...strings) {
		Map<String, String> map = hmget(key);
		if (map == null) {
			return null;
		}
		List<String> str = new ArrayList<String>();
		for (String string : strings) {
			String data = (String) map.get(string);
			str.add(data);
		}
		return str;
	}
	
	/**
	 * 
	 * @Description: 判断map中是否存在对应的key
	 * @param key
	 * @return boolean
	 * @date 2018年6月21日 下午5:40:59 
	 * @author zhangb
	 */
	public boolean exists(String key) {
		valiateKey(key);
		if (caches.isEmpty()) {
			return false;
		}
		return caches.containsKey(key);
	}
	
	/**
	 * 
	 * @Description: 设置缓存中对应key的过期时间
	 * @param key
	 * @param timeout 时间参数，单位为秒
	 * @date 2018年6月21日 下午5:42:14 
	 * @throws 
	 * @author zhangb
	 */
	public void expire(String key, int timeout) {
		valiateKey(key);
		if (!caches.isEmpty()) {
			MemoryCacheBean cache = caches.get(key);
			cache.setTimeout(timeout);
			/**添加自动删除key功能*/
//			timer.schedule(new CleanWorkerTask(key), timeout);
		}
	}
	
	/**
	 * 
	 * @Description: 从缓存中删除对应key数据
	 * @param key
	 * @date 2018年6月21日 下午5:43:36 
	 * @author zhangb
	 */
	public void del(String key) {
		valiateKey(key);
		caches.remove(key);
	}
	
	/**
	 * 
	 * @Description: 判断该缓存有没有过期
	 * @param key 缓存key
	 * @date 2018年6月20日 下午4:39:52 
	 * @author zhangb
	 */
	public void ttl(String key) {
		valiateKey(key);
		MemoryCacheBean cache = caches.get(key);
		//cache为空是因为获取到相同的随机数了，已经删除了一个就不再进行删除操作
		if (cache != null && cache.getDestoryTime() < System.currentTimeMillis() && cache.getTimeout() > 0) {
			System.out.println("删除的key"+key + "线程名称：" + Thread.currentThread().getName());
			caches.remove(key);
		}
	}
	
	/**
	 * 
	 * @Description: 校验key值不能为null
	 * @param key
	 * @date 2018年6月21日 下午5:59:39 
	 * @author zhangb
	 */
	public void valiateKey(String key) {
		if (key == null) {
			throw new IllegalArgumentException("key can not be blank!");
		}
	}
	
	/**
	 * 
	 * @Description: 定时器清除缓存
	 * @date 2018年6月20日 下午5:38:32 
	 * @throws 
	 * @author zhangb
	 */
	@PostConstruct
	public void time() {
		System.out.println(caches.size());
		
		//当前线程池最大允许创建3个线程,若创建线程数大于3个则多余的会等待前面线程执行完成后才执行
		ScheduledExecutorService schedulePool = Executors.newScheduledThreadPool(3);
		
		schedulePool.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				//每次循环都会从新的caches中获取key
				System.out.println("当前线程中cache个数："+caches.size() + Thread.currentThread().getName());
				String[] keys = caches.keySet().toArray(new String[0]);
				for (int i = 0; i < 20; i++) {
					Random random = new Random();  
					String randomKey = keys[random.nextInt(keys.length)];
					ttl(randomKey);
				}
			}
		}, 1, 2, TimeUnit.SECONDS);
		
		schedulePool.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				//每次循环都会从新的caches中获取key（caches的长度一直变化）
				System.out.println("当前线程中cache个数："+caches.size() + Thread.currentThread().getName());
				String[] keys = caches.keySet().toArray(new String[0]);
				for (int i = 0; i < 20; i++) {
					Random random = new Random();  
					String randomKey = keys[random.nextInt(keys.length)];
					ttl(randomKey);
				}
			}
		}, 1, 2, TimeUnit.SECONDS);
		
		schedulePool.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				//每次循环都会从新的caches中获取key
				System.out.println("当前线程中cache个数："+caches.size() + Thread.currentThread().getName());
				String[] keys = caches.keySet().toArray(new String[0]);
				for (int i = 0; i < 20; i++) {
					Random random = new Random();  
					String randomKey = keys[random.nextInt(keys.length)];
					ttl(randomKey);
				}
			}
		}, 1, 2, TimeUnit.SECONDS);
		
	}
	
	/**
	 * 
	 * @Description: 自动清除缓存,自动清除缓存相当于在添加缓存的时候加一个监控器，当缓存过期后会自动调用run方法删除
	 * @date 2018年6月21日 下午2:34:47 
	 * @author zhangb
	 */
//	class CleanWorkerTask extends TimerTask {
//
//        private String key;
//
//        public CleanWorkerTask(String key) {
//            this.key = key;
//        }
//
//        public void run() {
//        	//清除缓存
//        	caches.remove(key);
//        }
//    }
	
	public static void main(String[] args) {
		MemoryCacheService mcm = new MemoryCacheService();
//		for (int i = 0; i < 200; i++) {
//			mcm.hmset("accessToken"+i, ContainerUtils.make("nuid", "nuid"+i).put("ndid", "ndid"+i).put("type", "type"+i).get());
//			if (i%10 == 0) {
//				mcm.expire("accessToken"+i, 6);
//			}
//		}
//		mcm.time();
		
//		Map<String, String> map = mcm.hmget("accessToken");
//		System.out.println(map);
//		List<String> list = mcm.hmget("accessToken", "nuid", "ndid", "type");
//		System.out.println(list);
//		mcm.expire("accessToken", 60*60);
		mcm.hmset("", ContainerUtils.make("nuid", "nuid").put("ndid", "ndid").put("type", "type").get());
		boolean a = mcm.caches.containsKey("");
		System.out.println(a);
//		Map map = mcm.hmget(null);
//		System.out.println(map);
		mcm.del(null);
	}
	
}
