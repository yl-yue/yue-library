package ai.yue.library.data.redis.client;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;

import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.view.ResultInfo;
import ai.yue.library.data.redis.config.properties.WxMaProperties;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * 微信小程序用户接口
 * 
 * @author	孙金川
 * @since	2019年6月18日
 */
@Configuration
@EnableConfigurationProperties(WxMaProperties.class)
@ConditionalOnProperty(prefix = "yue.redis.wx.miniapp", name = "enabled", havingValue = "true")
public class WxMaUser {

	@Autowired
	private WxMaProperties wxMaProperties;
    private Map<String, WxMaService> maServices = Maps.newHashMap();
    
    @PostConstruct
    private void init() {
        List<WxMaProperties.Config> configs = wxMaProperties.getConfigs();
        if (ListUtils.isEmpty(configs)) {
        	throw new RuntimeException("无效的小程序配置...");
        }
        
        maServices = configs.stream()
            .map(a -> {
                WxMaInMemoryConfig config = new WxMaInMemoryConfig();
                config.setAppid(a.getAppid());
                config.setSecret(a.getSecret());
                WxMaService service = new WxMaServiceImpl();
                service.setWxMaConfig(config);
                return service;
            }).collect(Collectors.toMap(s -> s.getWxMaConfig().getAppid(), a -> a));
    }
    
    private WxMaService getMaService(String appid) {
        WxMaService wxService = maServices.get(appid);
        if (wxService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        return wxService;
    }
    
	/**
	 * 获取登录后的session信息
	 * 
	 * @param appid APPID
	 * @param code 授权CODE码
	 * @return {@linkplain WxMaJscode2SessionResult} <code style="color:red">unionid 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 <a href="https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html">UnionID 机制说明</a> 。</code>
	 */
    public WxMaJscode2SessionResult getSessionInfo(String appid, String code) {
        WxMaService wxService = getMaService(appid);
        WxMaJscode2SessionResult wxMaJscode2SessionResult = null;
		try {
			wxMaJscode2SessionResult = wxService.getUserService().getSessionInfo(code);
		} catch (WxErrorException e) {
			String msg = e.getMessage();
			throw new ResultException(ResultInfo.dev_defined(msg));
		}
		
		return wxMaJscode2SessionResult;
    }
    
    /**
     * <pre>
     * 获取用户信息
     * </pre>
     * 
     * @param appid	APPID
     * @param sessionKey 会话密钥
     * @param signature 数据签名
     * @param rawData 微信用户基本信息
     * @param encryptedData 消息密文
     * @param iv 加密算法的初始向量
     * @return {@linkplain WxMaUserInfo} 微信小程序用户信息
     */
    public WxMaUserInfo getUserInfo(String appid, String sessionKey, String signature, 
                       String rawData, String encryptedData, String iv) {
        WxMaService wxService = getMaService(appid);
        
        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
        	throw new ParamException("user check failed");
        }

        // 解密用户信息
        WxMaUserInfo wxMaUserInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);

        return wxMaUserInfo;
    }
    
    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     * 
     * @param appid	APPID
     * @param sessionKey 会话密钥
     * @param encryptedData 消息密文
     * @param iv 加密算法的初始向量
     * @return {@linkplain WxMaPhoneNumberInfo} 微信小程序手机号信息
     */
	public WxMaPhoneNumberInfo getCellphone(String appid, String sessionKey, String encryptedData, String iv) {
        WxMaService wxService = getMaService(appid);
        
        // 解密
        WxMaPhoneNumberInfo wxMaPhoneNumberInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);
        
        return wxMaPhoneNumberInfo;
    }
	
}
