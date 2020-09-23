package ai.yue.library.test.doc.example.pay;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.egzosn.pay.wx.bean.WxTransactionType;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.R;
import ai.yue.library.pay.client.Pay;
import ai.yue.library.pay.ipo.PayOrderIPO;

/**
 * @author	ylyue
 * @since	2019年8月9日
 */
@RestController
@RequestMapping("/payExample/{listId}")
public class PayExample {

	@Autowired
	Pay pay;
	
    /**
     * 公众号（小程序）支付
     * 
     * @param listId 列表ID
     * @param order_id 订单ID
     * @param openid openid
     * @return 返回jsapi所需参数
     */
	@Transactional
	@PostMapping("/jsapi")
	public Result<?> jsapi(@PathVariable Integer listId, @RequestParam("order_id") Long order_id,
			@RequestParam("openid") String openid) {
		String subject = "商品名称";
		String body = "商品描述";
		BigDecimal price = new BigDecimal(0.01);
		PayOrderIPO payOrderIPO = new PayOrderIPO(listId, subject, body, price, WxTransactionType.JSAPI);
		payOrderIPO.setOpenid(openid);
		
		Map<String, Object> orderInfo = pay.getOrderInfo(payOrderIPO);
		return R.success(orderInfo);
	}
	
}
