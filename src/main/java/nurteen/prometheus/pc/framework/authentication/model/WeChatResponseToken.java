package nurteen.prometheus.pc.framework.authentication.model;

import java.io.Serializable;

/**
 *  微信登陆的第二步，返回access_token的结果类
 * <p> @Title: WxResponseToken.java</p>  
 * <p> @Description: TODO </p>
 * <p> Copyright(版权): Copyright (c) 2018 </p>
 * <p> Company(公司): nurteen </p> 
 * <p> @date 2018年6月13日 下午2:01:34 </p>
 * @version nurteen.prometheus.pc.center.server
 * @author   weizc
 */
public class WeChatResponseToken extends WeChatErrorCode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String access_token;
	private Integer expires_in;
	private String refresh_token;
	private String openid;
	private String scope;
	private String unionid;
	
	
	
	public WeChatResponseToken() {
	}
	
	public WeChatResponseToken(String access_token, Integer expires_in, String refresh_token, String openid, String scope,
			String unionid) {
		super();
		this.access_token = access_token;
		this.expires_in = expires_in;
		this.refresh_token = refresh_token;
		this.openid = openid;
		this.scope = scope;
		this.unionid = unionid;
		
	}

	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public Integer getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

}
