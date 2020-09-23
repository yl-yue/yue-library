package ai.yue.library.test.webflux.doc.example.pay;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.R;
import ai.yue.library.pay.dto.ApplePayVerifyResult;
import ai.yue.library.pay.util.ApplePayUtils;
import cn.hutool.core.lang.Console;

/**
 * @author	ylyue
 * @since	2019年8月9日
 */
@RestController
@RequestMapping("/applePayExample")
public class ApplePayExample {

	/**
	 * 验证
	 * 
	 * @param order_id		订单ID
	 * @param receiptData	receipt-data
	 * @return
	 */
	@PostMapping("/verify")
	public Result<?> verify(@RequestParam("order_id") Long order_id, @RequestParam("receipt-data") String receiptData) {
		// 验证
		ApplePayVerifyResult applePayVerifyResult = ApplePayUtils.verify(receiptData);
		
		// 业务逻辑-可重点校验：金额、凭证是否使用、业务订单状态等
		Console.log(applePayVerifyResult);
		
		// 返回结果信息
		return R.success();
	}
	
}
