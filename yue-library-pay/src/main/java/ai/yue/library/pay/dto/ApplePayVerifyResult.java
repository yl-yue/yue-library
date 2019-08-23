package ai.yue.library.pay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@link #status} 状态码对照：
 * <p>0 正常
 * <p>21000 App Store不能读取你提供的JSON对象
 * <p>21002 receipt-data域的数据有问题
 * <p>21003 receipt无法通过验证
 * <p>21004 提供的shared secret不匹配你账号中的shared secret
 * <p>21005 receipt服务器当前不可用
 * <p>21006 receipt合法，但是订阅已过期。服务器接收到这个状态码时，receipt数据仍然会解码并一起发送
 * <p>21007 receipt是Sandbox receipt，但却发送至生产系统的验证服务
 * <p>21008 receipt是生产receipt，但却发送至Sandbox环境的验证服务
 * 
 * @author	孙金川
 * @since	2019年8月20日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplePayVerifyResult {

	/**
	 * 状态码，参照类说明
	 */
	private Integer status;
	
	/**
	 * 交易ID，由苹果方生成的交易单号
	 */
	private String transaction_id;
	
	/**
	 * 产品ID，可用于支付金额确认
	 */
	private String product_id;
	
	/**
	 * 交易数量
	 */
	private Integer quantity;
	
	/**
	 * 交易日期
	 */
	private String purchase_date;
	
}
