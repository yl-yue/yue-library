package ai.yue.library.data.redis.client;

import org.springframework.beans.factory.annotation.Autowired;

import ai.yue.library.base.exception.ParamException;
import ai.yue.library.data.redis.config.config.WxMaConfig;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import lombok.NoArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * 微信小程序用户接口
 * 
 * @author  孙金川
 * @version 创建时间：2019年6月18日
 */
@NoArgsConstructor
public class WxMaUser {

	@Autowired
	WxMaConfig wxMaConfig;
	
	/**
	 * 登陆
	 * @param appid APPID
	 * @param code 授权CODE码
	 * @return
	 */
    public WxMaJscode2SessionResult login(String appid, String code) {
        WxMaService wxService = wxMaConfig.getMaService(appid);
        WxMaJscode2SessionResult wxMaJscode2SessionResult = null;
		try {
			wxMaJscode2SessionResult = wxService.getUserService().getSessionInfo(code);
		} catch (WxErrorException e) {
			e.printStackTrace();
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
     * @return
     */
    public WxMaUserInfo getInfo(String appid, String sessionKey, String signature, 
                       String rawData, String encryptedData, String iv) {
        WxMaService wxService = wxMaConfig.getMaService(appid);
        
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
     * @return
     */
	public WxMaPhoneNumberInfo getPhone(String appid, String sessionKey, String encryptedData, String iv) {
        WxMaService wxService = wxMaConfig.getMaService(appid);
        
        // 解密
        WxMaPhoneNumberInfo wxMaPhoneNumberInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);
        
        return wxMaPhoneNumberInfo;
    }
	
}
