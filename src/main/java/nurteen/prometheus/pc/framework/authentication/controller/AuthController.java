package nurteen.prometheus.pc.framework.authentication.controller;

import nurteen.prometheus.pc.framework.*;
import nurteen.prometheus.pc.framework.authentication.argument.AccountLoginArgument;
import nurteen.prometheus.pc.framework.authentication.argument.LoginArgument;
import nurteen.prometheus.pc.framework.authentication.argument.PhoneLoginArgument;
import nurteen.prometheus.pc.framework.authentication.argument.WxLoginArgument;
import nurteen.prometheus.pc.framework.authentication.model.WeChatResponseToken;
import nurteen.prometheus.pc.framework.authentication.model.WeChatUserInfos;
import nurteen.prometheus.pc.framework.authentication.model.WeChatLoginArgument;
import nurteen.prometheus.pc.framework.authentication.response.WxLoginResponse;
import nurteen.prometheus.pc.framework.entities.DeviceInfo;
import nurteen.prometheus.pc.framework.entities.DeviceType;
import nurteen.prometheus.pc.framework.entities.ThirdpartyAccountType;
import nurteen.prometheus.pc.framework.entities.UserInfo;
import nurteen.prometheus.pc.framework.exception.InvalidArgumentException;
import nurteen.prometheus.pc.framework.utils.ContainerUtils;
import nurteen.prometheus.pc.framework.utils.CookieUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@CrossOrigin(origins = {"http://localhost:4200", "http://192.168.1.7:4200", "*"}, allowCredentials = "true")
public class AuthController {
	
    @Autowired
    protected ServerConfigProperties configProperties;

    @RequestMapping(path = "/devices/auth/login/account/v1", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody Response accountLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody AccountLoginArgument args) {
        /*
        NaLoginResponse reps = new NaLoginResponse();
        String accessToken = request.getSession().getId();
        CookieUtils.setAccessToken(response, accessToken);
        return Response.ok("ok", reps);
        */
        return Response.ok("ok");
    }

    @RequestMapping(path = "/devices/auth/login/phone/v1", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody Response phoneLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody PhoneLoginArgument argument) throws Exception {
        argument.validate();

        UserInfo userInfo = ObjectFactory.storageAware.fromPhone(argument.getPhone(), argument.getPasswd());
        if (userInfo == null) {
            return Response.failed("手机号或者密码错误");
        }

        DeviceInfo deviceInfo = this.getDevice(userInfo.getNuid(), argument.getDevice());

        return loginSuccess(request, response, userInfo, deviceInfo);
    }

    @RequestMapping(path = "/devices/auth/login/wx/v1", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody Response wxLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody WxLoginArgument argument) throws Exception {
        argument.validate();

        // 使用WX接口进行认证
        String appid = "";			//数据库获取
        String product = "";		//数据库获取
        try {
    		WeChatLoginArgument wx = new WeChatLoginArgument(appid, product, argument.getCode());
    		WeChatUserInfos infos = wxLonginObtainUserInfos(appid, wxAuthenticationStepTwo(wx));
    		System.out.println(infos.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        
        // 判断是否为首次登录
        UserInfo userInfo = ObjectFactory.storageAware.fromThirdpartyAccount(ThirdpartyAccountType.WX, argument.getProduct());
        if (userInfo == null) {
            userInfo = new UserInfo("", "", "");
            ObjectFactory.storageAware.insertNewFromThirdparty(userInfo, ThirdpartyAccountType.WX, argument.getProduct());
        }

        DeviceInfo deviceInfo = this.getDevice(userInfo.getNuid(), argument.getDevice());

        return loginSuccess(request, response, userInfo, deviceInfo);
    }

    private Response loginSuccess(HttpServletRequest request, HttpServletResponse response, UserInfo userInfo, DeviceInfo deviceInfo) {
        String accessToken = ObjectFactory.cacheAware.genAccessToken(request);
        ObjectFactory.cacheAware.updateAccessToken(accessToken, userInfo.getNuid(), deviceInfo.getNdid(), deviceInfo.getDeviceType(), Constants.DEFAULT_SESSION_TIMEOUT);

        WxLoginResponse loginResponse = new WxLoginResponse(userInfo.getNuid(), deviceInfo.getNdid(), accessToken, userInfo.getNickname(), userInfo.getHeadimg());
        CookieUtils.setAccessToken(response, accessToken);
        return Response.ok("ok", loginResponse);
    }

    private DeviceInfo getDevice(String nuid, LoginArgument.Device device) throws Exception {
        if (device.getType() == DeviceType.App_Browser.getValue()) {
            return new DeviceInfo(DeviceType.App_Browser, device.getDevicePlatform(), device.getHid());
        } else {
            DeviceInfo deviceInfo = ObjectFactory.storageAware.fromHid(device.getHid());
            if (deviceInfo != null) {
                ObjectFactory.storageAware.insertNew(nuid, deviceInfo.getNdid(), device.getName());
            } else {
                deviceInfo = new DeviceInfo(device.getDeviceType(), device.getDevicePlatform(), device.getHid());
                ObjectFactory.storageAware.insertNew(nuid, device.getName(), deviceInfo);
            }
            return deviceInfo;
        }
    }
    //测试微信登陆部分
    public static void main(String[] args) {
    	String appid = "wxa6cf35400c5e6e22";
    	String product = "f46a2c7c53d6e8ecfdaa0a236eecdf39";
    	String code = "081N7MfI1uKgE70rqlfI10sMfI1N7Mfz";
    	try {
    		WeChatLoginArgument wx = new WeChatLoginArgument(appid, product, code);
    		WeChatUserInfos infos = wxLonginObtainUserInfos(appid, wxAuthenticationStepTwo(wx));
    		System.out.println(infos.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
   
    /**
     * 
     * @Description: 微信登陆第二步，获取到code后去得到access_token
     * @param @param appid		
     * @param @param secret		
     * @param @param code		
     * @param @return
     * @return HttpEntity
     * @throws InvalidArgumentException 
     * @throws IOException 
     * @throws UnsupportedEncodingException 
     * @throws ParseException 
     * @date 2018年6月15日 上午9:41:14 
     * @throws 
     * @author weizc
     */
    private static WeChatResponseToken wxAuthenticationStepTwo(WeChatLoginArgument argument) throws InvalidArgumentException{
    	argument.validate();
    	WeChatResponseToken stepTwoResult = null;	//	返回结果
        String grant_type = "authorization_code";	//authorization_code
        List<BasicNameValuePair> pirs = ContainerUtils.makeArrayList(new BasicNameValuePair("appid", argument.getAppid()))
        							.add(new BasicNameValuePair("secret", argument.getSecret()))
        							.add(new BasicNameValuePair("code", argument.getCode()))
        							.add(new BasicNameValuePair("grant_type", grant_type))
        							.get();
    	//构造get请求地址url
    	//官方示例：https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
		try {
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token"+"?"+EntityUtils.toString(new UrlEncodedFormEntity(pirs,"utf-8"));
			stepTwoResult = (WeChatResponseToken) remoting(url, WeChatResponseToken.class);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return stepTwoResult;
    }
   
    /**
     * 
     * @Description: 得到access_token后去获取用户信息
     * @param @param stepTwoResult
     * @param @return
     * @return WxUserInfos
     * @date 2018年6月15日 上午11:10:51 
     * @throws 
     * @author weizc
     */
    private static WeChatUserInfos wxLonginObtainUserInfos(String appid, WeChatResponseToken stepTwoResult){
    	WeChatUserInfos wxUserInfo = null;
    	//不为空，但是也有可能是错误信息
    	if(stepTwoResult != null && stepTwoResult.getErrcode() == null){
			//修改token时间,必须要,是为了容错机制的处理，如果用户调用了第二步，但是两小时内未进行其他操作，但是之后又进行其他操作（获取用户信息），则会失败
			WeChatResponseToken tokenUpdateResult = refreshAccessToken(appid,stepTwoResult.getRefresh_token());
			if(tokenUpdateResult != null && tokenUpdateResult.getErrcode() == null ){
				//调取用户信息
				WeChatUserInfos wxUserInfoResult = obtainUserInfos(tokenUpdateResult.getAccess_token(),tokenUpdateResult.getOpenid());
				if(wxUserInfoResult != null && wxUserInfoResult.getErrcode() == null){
					wxUserInfo = wxUserInfoResult;
				}else{
					throw new RuntimeException("获取微信用户信息失败！");
				}
			}else{
				//更新token时长失败了
				throw new RuntimeException("更新access_token时长失败,请重试！");
			}
        }else{
        	//调用微信认证第二部的时候失败了
        	throw new RuntimeException("微信认证失败,请刷新重试！");
        }
    	return wxUserInfo;
    }
    
    //刷新access_token有效期(access_token有效期只有两个小时，刷新后能变为30天)，不刷新的话2小时过后又要重新认证
    private static WeChatResponseToken refreshAccessToken(String appid, String refresh_token){
    	/*
    	 * appid：应用唯一标识
    	 * grant_type：填refresh_token
    	 * refresh_token：填写通过access_token获取到的refresh_token参数
    	 */
    	WeChatResponseToken resultEntity = null;
    	String grant_type = "refresh_token";
    	if((appid !=null && !"".equals(appid)) && (refresh_token != null && !"".equals(refresh_token))){
    		List<BasicNameValuePair> pirs = ContainerUtils.makeArrayList(new BasicNameValuePair("appid", appid))
    									.add(new BasicNameValuePair("grant_type", grant_type))
    									.add(new BasicNameValuePair("refresh_token", refresh_token))
    									.get();
			try {
				//官方示例：https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
				String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token"+"?"+EntityUtils.toString(new UrlEncodedFormEntity(pirs,"utf-8"));
				resultEntity = (WeChatResponseToken) remoting(url, WeChatResponseToken.class);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	return resultEntity;
    }
    
    //刷新access_token后获取用户信息(刷不刷新后面具体情况再看)
    private static WeChatUserInfos obtainUserInfos(String access_token, String openid){
    	WeChatUserInfos entity = null;
    	List<BasicNameValuePair> pirs = ContainerUtils.makeArrayList(new BasicNameValuePair("access_token", access_token)).add(new BasicNameValuePair("openid", openid)).get();
    	try {
    		//官方示例：https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
			String url = "https://api.weixin.qq.com/sns/userinfo"+"?"+EntityUtils.toString(new UrlEncodedFormEntity(pirs,"utf-8"));
			entity = (WeChatUserInfos) remoting(url, WeChatUserInfos.class);
    	} catch (ParseException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return entity;
    }
    
    //调用之后，直接返回的是对象，而之前的调用是存在问题的
    private static Object remoting(String url, Class<?> clz){
    	Optional.of(url);
    	Object result = null;
    	if(url != null && !"".equals(url)){
    		HttpGet request = new HttpGet(url);
    		//设置期望服务器返回的编码格式（防止中文乱码）
    		request.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
    		request.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
    		try (CloseableHttpClient client = HttpClients.createDefault();
    			CloseableHttpResponse response = client.execute(request)) {
    			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
    				request.abort();
    				throw new RuntimeException("请求出现错误");
    			}else{
    				HttpEntity entity = response.getEntity();
    				String str = EntityUtils.toString(entity, "utf-8");
    				if(str.startsWith("[") && str.endsWith("]")){
    					//如果不是{ }开头结尾的数据，而是[ ]这种形式的，则需要其他处理，由于微信的是{ }格式的，直接用下面的就行
    				}else{
    					//微信的直接用就行了，返回的是标准的东西
    					return new ObjectMapper().readValue(str, clz);
    				}
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	return result;
    }
}
