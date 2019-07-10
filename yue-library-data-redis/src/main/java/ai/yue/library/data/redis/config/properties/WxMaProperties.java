package ai.yue.library.data.redis.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @author  孙金川
 * @version 创建时间：2019年6月18日
 */
@Data
@ConfigurationProperties("yue.wx.miniapp")
public class WxMaProperties {
	
	/**
	 * 是否启用 <code style="color:red">微信小程序</code> 自动配置
	 * <p>
	 * 默认：false
	 */
	private boolean enabled = false;
	
	/**
	 * 配置列表
	 */
    private List<Config> configs;
    
    @Data
    public static class Config {
        /**
         * 设置微信小程序的appid
         */
        private String appid;

        /**
         * 设置微信小程序的Secret
         */
        private String secret;

//        /**
//         * 设置微信小程序消息服务器配置的token
//         */
//        private String token;
//
//        /**
//         * 设置微信小程序消息服务器配置的EncodingAESKey
//         */
//        private String aesKey;
//
//        /**
//         * 消息格式，XML或者JSON
//         */
//        private String msgDataFormat;
    }

}
