package ai.yue.library.pay.ipo;

import java.math.BigDecimal;

import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.wx.bean.WxTransactionType;

import ai.yue.library.base.util.UUIDUtils;
import lombok.Builder;
import lombok.Data;

/**
 * @author	孙金川
 * @since	创建时间：2018年8月16日
 */
@Data
@Builder
public class PayOrderBuilder {
	
	/**
	 * 配置列表id，用于区分具体配置信息，全局唯一
	 */
	private Integer listId;
	
	/**
	 * 商品名称
	 */
	private String subject;
	
	/**
	 * 商品描述
	 */
	private String body;
	
	/**
	 * 商品价格
	 */
	private BigDecimal price;
	
	/**
	 * 交易类型
	 * <p>如： {@linkplain WxTransactionType#JSAPI} 代表公众号与小程序支付
	 */
	private TransactionType transactionType;
	
	/**
	 * 创建支付订单信息
	 * 
	 * @return 支付订单
	 */
	public PayOrderIPO createPayOrderIPO() {
		return createPayOrderIPO(UUIDUtils.getOrderNo_19());
	}
	
	/**
	 * 创建支付订单信息
	 * 
	 * @param outTradeNo 商户订单号
	 * @return 支付订单
	 */
	public PayOrderIPO createPayOrderIPO(String outTradeNo) {
		// 支付订单基础信息
        return new PayOrderIPO(listId, subject, body, price, outTradeNo, transactionType);
	}
	
}
