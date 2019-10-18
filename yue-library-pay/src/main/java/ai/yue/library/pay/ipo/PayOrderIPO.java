package ai.yue.library.pay.ipo;

import java.math.BigDecimal;

import com.egzosn.pay.common.bean.TransactionType;

import ai.yue.library.base.util.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 支付订单IPO
 * 
 * @author	ylyue
 * @since	2019年8月22日
 */
@Data
@ToString(callSuper = true) 
@EqualsAndHashCode(callSuper = true)
public class PayOrderIPO extends com.egzosn.pay.common.bean.PayOrder {

	/**
	 * 配置列表id，用于区分具体配置信息，全局唯一
	 */
	private Integer listId;
    
	public PayOrderIPO(Integer listId, String subject, String body, BigDecimal price, TransactionType transactionType) {
		super(subject, body, price, UUIDUtils.getOrderNo_19(), transactionType);
		this.listId = listId;
	}
    
	public PayOrderIPO(Integer listId, String subject, String body, BigDecimal price, String outTradeNo, TransactionType transactionType) {
        super(subject, body, price, outTradeNo, transactionType);
        this.listId = listId;
    }
    
}
