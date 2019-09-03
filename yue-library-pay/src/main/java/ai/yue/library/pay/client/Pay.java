package ai.yue.library.pay.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.imageio.ImageIO;

import com.egzosn.pay.common.api.PayMessageInterceptor;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.common.bean.TransferOrder;

import ai.yue.library.pay.ipo.PayOrderIPO;
import ai.yue.library.pay.ipo.QueryOrderIPO;
import lombok.AllArgsConstructor;

/**
 * @author	孙金川
 * @since	2019年8月22日
 */
@AllArgsConstructor
public class Pay {
	
	@SuppressWarnings("rawtypes")
	private Map<Integer, PayService> payServiceMap;
	
    private PayService<?> getPayService(Integer listId) {
        PayService<?> payService = payServiceMap.get(listId);
        if (payService == null) {
            throw new IllegalArgumentException(String.format("未找到对应listId=[%s]的配置，请核实！", listId));
        }
        
        return payService;
    }
    
    /**
     * 跳到支付页面
     * 针对实时支付,即时付款
     *
     * @param payOrderIPO 商户支付订单信息
     * @return 跳到支付页面
     */
	public String toPay(PayOrderIPO payOrderIPO) {
		PayService<?> payService = getPayService(payOrderIPO.getListId());
		Map<String, Object> orderInfo = payService.orderInfo(payOrderIPO);
		return payService.buildRequest(orderInfo, MethodType.POST);
	}
    
    /**
     * 获取支付预订单信息
     *
     * @param payOrderIPO 商户支付订单信息
     * @return 支付预订单信息
     */
	public Map<String, Object> getOrderInfo(PayOrderIPO payOrderIPO) {
		return getPayService(payOrderIPO.getListId()).orderInfo(payOrderIPO);
	}
	
    /**
     * 刷卡付,pos主动扫码付款(条码付)
     *
     * @param payOrderIPO 商户支付订单信息
     * @return 支付结果
     */
    public Map<String, Object> microPay(PayOrderIPO payOrderIPO) {
    	PayService<?> payService = getPayService(payOrderIPO.getListId());
        //支付结果
        return payService.microPay(payOrderIPO);
    }
    
    /**
     * 获取二维码图像
     * 二维码支付
     *
     * @param payOrderIPO 商户支付订单信息
     * @return 二维码图像
     * @throws IOException IOException
     */
    public byte[] toQrPay(PayOrderIPO payOrderIPO) throws IOException {
        // 获取对应的支付账户操作工具（可根据账户id）
    	PayService<?> payService = getPayService(payOrderIPO.getListId());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ImageIO.write(payService.genQrPay(payOrderIPO), "JPEG", baos);
        return baos.toByteArray();
    }
    
    /**
     * 获取二维码信息
     * 二维码支付
     *
     * @param payOrderIPO 商户支付订单信息
     * @return 二维码信息
     */
    public String getQrPay(PayOrderIPO payOrderIPO) {
        return getPayService(payOrderIPO.getListId()).getQrPay(payOrderIPO);
    }
    
    /**
     * 支付回调地址
     * 方式二
     * 
     * @param listId		列表id
     * @param parameterMap	请求参数
     * @param is			请求流
     * @return 支付是否成功
     * @throws IOException IOException
     *                     拦截器相关增加， 详情查看{@link com.egzosn.pay.common.api.PayService#addPayMessageInterceptor(PayMessageInterceptor)}
     *                     <p>
     *                     业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看{@link com.egzosn.pay.common.api.PayService#setPayMessageHandler(com.egzosn.pay.common.api.PayMessageHandler)}
     *                     </p>
     *                     如果未设置 {@link com.egzosn.pay.common.api.PayMessageHandler} 那么会使用默认的 {@link com.egzosn.pay.common.api.DefaultPayMessageHandler}
     */
    public String payBack(Integer listId, Map<String, String[]> parameterMap, InputStream is) throws IOException {
        // 业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看com.egzosn.pay.common.api.PayService.setPayMessageHandler()
        return getPayService(listId).payBack(parameterMap, is).toMessage();
    }
    
    /**
     * 查询
     *
     * @param queryOrderIPO 订单的请求体
     * @return 返回查询回来的结果集，支付方原值返回
     */
    public Map<String, Object> query(QueryOrderIPO queryOrderIPO) {
        return getPayService(queryOrderIPO.getListId()).query(queryOrderIPO.getTradeNo(), queryOrderIPO.getOutTradeNo());
    }
    
    /**
     * 交易关闭接口
     *
     * @param queryOrderIPO 订单的请求体
     * @return 返回支付方交易关闭后的结果
     */
    public Map<String, Object> close(QueryOrderIPO queryOrderIPO) {
        return getPayService(queryOrderIPO.getListId()).close(queryOrderIPO.getTradeNo(), queryOrderIPO.getOutTradeNo());
    }
    
    /**
     * 申请退款接口
     *
     * @param listId 列表id
     * @param order     订单的请求体
     * @return 返回支付方申请退款后的结果
     */
    public Map<String, Object> refund(Integer listId, RefundOrder order) {
        return getPayService(listId).refund(order);
    }
    
    /**
     * 查询退款
     *
     * @param listId 列表id
     * @param refundOrder 订单的请求体
     * @return 返回支付方查询退款后的结果
     */
    public Map<String, Object> refundquery(Integer listId, RefundOrder refundOrder) {
        return getPayService(listId).refundquery(refundOrder);
    }
    
    /**
     * 下载对账单
     *
     * @param queryOrderIPO 订单的请求体
     * @return 返回支付方下载对账单的结果
     */
    public Object downloadbill(QueryOrderIPO queryOrderIPO) {
        return getPayService(queryOrderIPO.getListId()).downloadbill(queryOrderIPO.getBillDate(), queryOrderIPO.getBillType());
    }
    
    /**
     * 通用查询接口，根据 TransactionType 类型进行实现,此接口不包括退款
     *
     * @param queryOrderIPO 订单的请求体
     * @param transactionType 交易类型
     * @return 返回支付方对应接口的结果
     */
    public Map<String, Object> secondaryInterface(QueryOrderIPO queryOrderIPO, TransactionType transactionType) {
        return getPayService(queryOrderIPO.getListId()).secondaryInterface(queryOrderIPO.getTradeNoOrBillDate(), queryOrderIPO.getOutTradeNoBillType(), transactionType);
    }
    
    /**
     * 转账
     *
     * @param listId 列表id
     * @param transferOrder 转账订单
     * @return 对应的转账结果
     */
    public Map<String, Object> transfer(Integer listId, TransferOrder transferOrder) {
        return getPayService(listId).transfer(transferOrder);
    }
    
    /**
     * 转账查询
     *
     * @param listId 列表id
     * @param outNo     商户转账订单号
     * @param tradeNo   支付平台转账订单号
     * @return 对应的转账订单
     */
    public Map<String, Object> transferQuery(Integer listId, String outNo, String tradeNo) {
        return getPayService(listId).transferQuery(outNo, tradeNo);
    }

}
