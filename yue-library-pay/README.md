## 介绍
　　pay库基于pay-java-parent进行二次封装，**让你真正做到一行代码实现支付聚合**，让你可以不用理解支付怎么对接，只需要专注你的业务

## 快速开始
### 引入模块
1. `yue-library-dependencies`作为父项目，在`pom.xml`文件中添加：
``` pom
<dependencies>
	<dependency>
		<groupId>ai.ylyue</groupId>
		<artifactId>yue-library-pay</artifactId>
	</dependency>
</dependencies>
```

2. 引入 你需要对接的基于`pay-java-parent`支付开发包,具体支付模块 "{module-name}" 为具体的支付渠道的模块名 pay-java-ali，pay-java-wx等
```xml
<dependency>
	<groupId>com.egzosn</groupId>
	<artifactId>{module-name}</artifactId>
</dependency>
```

### 使用
注入Bean `Pay`
```java
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
		return ResultInfo.success(orderInfo);
	}
	
}
```

## 苹果支付验证
```java
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
	return ResultInfo.success();
}
```
更多方法请参阅API文档...