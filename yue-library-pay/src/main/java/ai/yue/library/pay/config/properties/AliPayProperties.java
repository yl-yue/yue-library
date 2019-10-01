package ai.yue.library.pay.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.egzosn.pay.common.util.sign.SignUtils;

import lombok.Data;

/**
 * @author	ylyue
 * @since	创建时间：2018年7月13日
 */
@Data
@ConfigurationProperties("yue.pay.ali")
public class AliPayProperties {

	/**
	 * 是否启用微信支付自动配置
	 * <p>
	 * 默认：false
	 */
	private boolean enabled = false;
	
	/**
	 * 配置列表
	 */
    private List<AliPayConfig> configList;
    
	@Data
	public static class AliPayConfig {
	
		/**
		 * 配置列表id，用于区分具体配置信息，全局唯一
		 */
		private Integer listId;
		/**
		 * 商户应用id
		 */
		private String appId;
		/**
		 * 商户签约拿到的pid,partner_id的简称，合作伙伴身份等同于 partner
		 */
		private String pid;
		/**
		 * 商户收款账号
		 */
		private String seller;
		/**
		 * 应用私钥，rsa_private pkcs8格式 生成签名时使用
		 */
		private String keyPrivate;
		/**
		 * 支付平台公钥(签名校验使用)
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
		/**
		 * 是否为沙箱环境，默认为正式环境
		 * <p>默认：false
		 */
		private Boolean test = false;
		/**
		 * 字符类型：utf-8
		 */
		private final String inputCharset = "utf-8";
		
	}
	
}
