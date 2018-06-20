package nurteen.prometheus.pc.framework.authentication.model;

import java.io.Serializable;
import java.util.List;

/**
 * 调取微信接口获取的用户信息类
 * <p> @Title: WxUserInfos.java</p>  
 * <p> @Description: TODO </p>
 * <p> Copyright(版权): Copyright (c) 2018 </p>
 * <p> Company(公司): nurteen </p> 
 * <p> @date 2018年6月13日 下午4:25:01 </p>
 * @version nurteen.prometheus.pc.center.server
 * @author   weizc
 */
public class WeChatUserInfos extends WeChatErrorCode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String openid;			//普通用户的标识，对当前开发者帐号唯一
	private String nickname;		//普通用户昵称
	private String sex;				//普通用户性别，1为男性，2为女性
	private String province;		//普通用户个人资料填写的省份
	private String city;			//普通用户个人资料填写的城市
	private String country;			//国家，如中国为CN
	private String headimgurl;		//用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
	private List<String> privilege;	//用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
	private String unionid;			//用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
	private String language;		//在开放平台并没有提及此字段，但是返回结果中却有这东西
	
	public WeChatUserInfos() {
	}
	public WeChatUserInfos(String openid, String nickname, String sex, String province, String city, String country,
			String headimgurl, List<String> privilege, String unionid, String language) {
		super();
		this.openid = openid;
		this.nickname = nickname;
		this.sex = sex;
		this.province = province;
		this.city = city;
		this.country = country;
		this.headimgurl = headimgurl;
		this.privilege = privilege;
		this.unionid = unionid;
		this.language = language;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public List<String> getPrivilege() {
		return privilege;
	}
	public void setPrivilege(List<String> privilege) {
		this.privilege = privilege;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	@Override
	public String toString() {
		return "WeChatUserInfos [openid=" + openid + ", nickname=" + nickname + ", sex=" + sex + ", province="
				+ province + ", city=" + city + ", country=" + country + ", headimgurl=" + headimgurl + ", privilege="
				+ privilege + ", unionid=" + unionid + ", language=" + language + "]";
	}
}
