package nurteen.prometheus.pc.framework.authentication.model;
/**
 * 微信登陆所有流程中可能出现的错误信息
 * <p> @Title: WeChatErrorCode.java</p>  
 * <p> @Description: TODO </p>
 * <p> Copyright(版权): Copyright (c) 2018 </p>
 * <p> Company(公司): nurteen </p> 
 * <p> @date 2018年6月19日 下午2:33:20 </p>
 * @version server
 * @author   weizc
 */
public class WeChatErrorCode {
	private Integer errcode;	//错误码
	private String errmsg;		//错误信息
	public Integer getErrcode() {
		return errcode;
	}
	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
}
