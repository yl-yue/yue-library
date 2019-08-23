package ai.yue.library.pay.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.egzosn.pay.common.util.sign.SignUtils;

import lombok.Data;

/**
 * @author 孙金川
 * @version 创建时间：2018年7月13日
 */
@Data
@ConfigurationProperties("yue.pay.wx")
public class WxPayProperties {
	
	/**
	 * 是否启用微信支付自动配置
	 * <p>
	 * 默认：false
	 */
	private boolean enabled = false;
	
	/**
	 * 配置列表
	 */
    private List<WxPayConfig> configList;
    
	@Data
	public static class WxPayConfig {
		
		/**
		 * 配置列表id，用于区分具体配置信息，全局唯一
		 */
		private Integer listId;
		/**
		 * 商户应用id
		 */
		private String appId;
		/**
		 * 商户号 合作者id
		 */
		private String mchId;
		/**
		 * 密钥
		 */
		private String secretKey;
		/**
		 * 转账公钥，转账时必填
		 */
		private String keyPublic;
		/**
		 * 异步回调地址
		 */
		private String notifyUrl;
		/**
		 * 签名加密类型
		 */
		private SignUtils signType;
		
	}
	
}
