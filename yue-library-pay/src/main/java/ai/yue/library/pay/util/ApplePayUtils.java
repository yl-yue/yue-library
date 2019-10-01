package ai.yue.library.pay.util;

import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.util.ApplicationContextUtils;
import ai.yue.library.base.view.ResultInfo;
import ai.yue.library.pay.dto.ApplePayVerifyResult;

/**
 * 苹果内购支付验证
 * 
 * @author	ylyue
 * @since	2019年8月20日
 */
public class ApplePayUtils {

	private static RestTemplate restTemplate;
	
	/** 沙箱环境验证URI */
	private static final String URI_SANDBOX = "https://sandbox.itunes.apple.com/verifyReceipt";
	/** 正式环境验证URI */
	private static final String URI_VERIFY = "https://buy.itunes.apple.com/verifyReceipt";
	
	// 初始化
	static {
		restTemplate = ApplicationContextUtils.getBean(RestTemplate.class);
	}
	
	/**
	 * 验证
	 * 
	 * @param receiptData 验证数据
	 * @return 验证结果
	 */
	public static ApplePayVerifyResult verify(String receiptData) {
		return verify(URI_VERIFY, receiptData);
	}
	
	/**
	 * 验证
	 * 
	 * @param uri 验证URI（用于区分验证环境）
	 * @param receiptData 验证数据
	 * @return 验证结果
	 */
	private static ApplePayVerifyResult verify(String uri, String receiptData) {
		// 1. 发起请求
		JSONObject requestData = new JSONObject();
		requestData.put("receipt-data", receiptData);
		String response = restTemplate.postForObject(uri, requestData, String.class);
		JSONObject result = JSONObject.parseObject(response);
		Integer status = result.getInteger("status");
		
		// 2. 确认结果
		if (status == 21007) {
			return verify(URI_SANDBOX, receiptData);
		}
		if (status != 0) {
			throw new ResultException(ResultInfo.error(response));
		}
		
		// 3. 获得结果
		JSONObject receipt = result.getJSONObject("receipt");
		JSONObject in_app = receipt.getJSONArray("in_app").getJSONObject(0);
		String transaction_id = in_app.getString("transaction_id");
		String product_id = in_app.getString("product_id");
		Integer quantity = in_app.getInteger("quantity");
		String purchase_date = in_app.getString("purchase_date");
		
		// 4. 返回结果
		return ApplePayVerifyResult.builder()
				.status(status)
				.transaction_id(transaction_id)
				.product_id(product_id)
				.quantity(quantity)
				.purchase_date(purchase_date)
				.build();
	}
	
}
