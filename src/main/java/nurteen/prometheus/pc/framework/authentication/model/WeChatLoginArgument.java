package nurteen.prometheus.pc.framework.authentication.model;


import javax.validation.constraints.NotNull;

import nurteen.prometheus.pc.framework.Argument;
/**
 * 微信登陆第二步的参数验证
 * <p> @Title: WxLoginAllArgument.java</p>  
 * <p> @Description: TODO </p>
 * <p> Copyright(版权): Copyright (c) 2018 </p>
 * <p> Company(公司): nurteen </p> 
 * <p> @date 2018年6月20日 上午9:19:32 </p>
 * @version server
 * @author   weizc
 */
public class WeChatLoginArgument extends Argument{
	@NotNull(message = "appid不能为空")
	private String appid;
	@NotNull(message = "secret值不能为空")
	private String secret;
	@NotNull(message = "code值不能为空")
	private String code;
	
	public WeChatLoginArgument(String appid, String secret, String code) {
		super();
		this.appid = appid;
		this.secret = secret;
		this.code = code;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
