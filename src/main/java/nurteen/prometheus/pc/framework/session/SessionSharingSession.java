package nurteen.prometheus.pc.framework.session;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import nurteen.prometheus.pc.center.server.service.RedisCacheAware;
import nurteen.prometheus.pc.framework.utils.SpringContextHelper;

public class SessionSharingSession implements HttpSession, Serializable {

	private static final long serialVersionUID = 6380981985844783260L;
	
	HttpSession session = null;
	
	Map<String, Object> attrs = new ConcurrentHashMap<String, Object>();
	
	private RedisCacheAware cache;

	public Map<String, Object> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Object> attrs) {
		this.attrs = attrs;
	}

	public SessionSharingSession() {
		super();
	}

	public SessionSharingSession(HttpSession session) {
		super();
		this.session = session;
		//设置原生session失效时间
		session.setMaxInactiveInterval(30);
		//获取cache对象
		cache = (RedisCacheAware) SpringContextHelper.getBean("redisCacheAware");
	}

	/**获取属性**/
	@Override
	public Object getAttribute(String name) {
		return attrs.get(name);
	}
	/**设置属性**/
	@Override
	public void setAttribute(String name, Object value) {
		attrs.put(name, value);
		cache.setSession(name, this);
	}
	/**删除属性**/
	@Override
	public void removeAttribute(String name) {
		attrs.remove(name);
		cache.setSession(name, this);
	}
	
	/**手动使session失效**/
	@Override
	public void invalidate() {
		session.invalidate();
		cache.delSession(session.getId());
	}

	/**TODO 也需要同步操作redis,但非重要属性,也没找到set方法,后面再补充吧  session是否新建的**/
	@Override
	public boolean isNew() {
	   return session.isNew();
	}
	
	/**获取所有属性名**/
	@Override
	public Enumeration<String> getAttributeNames() {
		return session.getAttributeNames();
	}
	
	/**设置session有效期**/
	@Override
	public void setMaxInactiveInterval(int interval) {
		session.setMaxInactiveInterval(interval);
		//同步更新redis
		//TODO
	}
	
	@Override
	public long getCreationTime() {
		return session.getCreationTime();
	}

	@Override
	public String getId() {
		return session.getId();
	}

	/**TODO 也需要同步操作redis,但没找到set方法,暂时返回当前时间吧**/
	@Override
	public long getLastAccessedTime() {
		return System.currentTimeMillis();
	}

	@Override
	public ServletContext getServletContext() {
		return session.getServletContext();
	}

	@Override
	public int getMaxInactiveInterval() {
		return session.getMaxInactiveInterval();
	}

	@Override
	public HttpSessionContext getSessionContext() {
		return session.getSessionContext();
	}

	@Override
	public Object getValue(String name) {
		return session.getValue(name);
	}

	@Override
	public String[] getValueNames() {
		return session.getValueNames();
	}

	@Override
	public void putValue(String name, Object value) {
		session.putValue(name, value);
	}

	@Override
	public void removeValue(String name) {
		session.removeValue(name);
	}

}
