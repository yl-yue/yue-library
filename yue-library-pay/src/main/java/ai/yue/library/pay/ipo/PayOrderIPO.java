package ai.yue.library.pay.ipo;

import java.math.BigDecimal;

import com.egzosn.pay.common.bean.TransactionType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 建造者：
 * <p>{@linkplain PayOrderBuilder#builder()}
 * <p>{@linkplain PayOrderBuilder#createPayOrderIPO()}
 * 
 * @author	孙金川
 * @since	2019年8月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayOrderIPO extends com.egzosn.pay.common.bean.PayOrder {

	/**
	 * 配置列表id，用于区分具体配置信息，全局唯一
	 */
    private Integer listId;
    
    PayOrderIPO(Integer listId, String subject, String body, BigDecimal price, String outTradeNo, TransactionType transactionType) {
        super(subject, body, price, outTradeNo, transactionType);
        this.listId = listId;
    }
    
}
