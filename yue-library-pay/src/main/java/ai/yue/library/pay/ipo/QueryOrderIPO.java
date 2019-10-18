package ai.yue.library.pay.ipo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 订单辅助接口
 * 
 * @author	ylyue
 * @since	2019年8月23日
 */
@Data
public class QueryOrderIPO {

	/**
	 * 列表id
	 */
	private Integer listId;

	/**
	 * 支付平台订单号
	 */
	private String tradeNo;

	/**
	 * 商户单号
	 */
	private String outTradeNo;

	/**
	 * 退款金额
	 */
	private BigDecimal refundAmount;
	
	/**
	 * 总金额
	 */
	private BigDecimal totalAmount;
	
	/**
	 * 账单时间：具体请查看对应支付平台
	 */
	private Date billDate;
	
	/**
	 * 账单时间：具体请查看对应支付平台
	 */
	private String billType;
	
	/**
	 * 支付平台订单号或者账单日期
	 */
	private Object tradeNoOrBillDate;
	
	/**
	 * 商户单号或者 账单类型
	 */
	private String outTradeNoBillType;
	
}
