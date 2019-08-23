package ai.yue.library.pay.config;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.egzosn.pay.ali.api.AliPayConfigStorage;
import com.egzosn.pay.ali.api.AliPayService;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.wx.api.WxPayConfigStorage;
import com.egzosn.pay.wx.api.WxPayService;

import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.ObjectUtils;
import ai.yue.library.pay.client.Pay;
import ai.yue.library.pay.config.properties.AliPayProperties;
import ai.yue.library.pay.config.properties.AliPayProperties.AliPayConfig;
import ai.yue.library.pay.config.properties.WxPayProperties;
import ai.yue.library.pay.config.properties.WxPayProperties.WxPayConfig;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付配置
 * 
 * @author	孙金川
 * @since	2019年8月22日
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({ AliPayProperties.class, WxPayProperties.class })
public class PayAutoConfig {
	
	@Autowired
	WxPayProperties wxPayProperties;
	@Autowired
	AliPayProperties aliPayProperties;
	@SuppressWarnings("rawtypes")
	Map<Integer, PayService> payServiceMap = MapUtils.newHashMap();
	
	@Bean
	@Primary
	public Pay pay() {
		// 微信
		if (wxPayProperties.isEnabled()) {
			if (!ClassLoaderUtil.isPresent("com.egzosn.pay.wx.api.WxPayService")) {
				log.error("【支付配置】未引入依赖模块：pay-java-wx");
			}
			wxPay();
		}
		
		// 支付宝
		if (aliPayProperties.isEnabled()) {
			if (!ClassLoaderUtil.isPresent("com.egzosn.pay.ali.api.AliPayService")) {
				log.error("【支付配置】未引入依赖模块：pay-java-ali");
			}
			aliPay();
		}
		
		// Bean
        return new Pay(payServiceMap);
    }
	
	private void wxPay() {
		// 1. 获得配置列表
        List<WxPayConfig> configList = wxPayProperties.getConfigList();
        if (ListUtils.isEmpty(configList)) {
        	throw new RuntimeException("【支付配置】无效的微信支付配置...");
        }
        
        configList.forEach(config -> {
        	// 2. 支付配置
    		WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
            wxPayConfigStorage.setMchId(config.getMchId());
            wxPayConfigStorage.setAppid(config.getAppId());
            wxPayConfigStorage.setKeyPublic(config.getKeyPublic());// 转账公钥，转账时必填
            wxPayConfigStorage.setSecretKey(config.getSecretKey());
            wxPayConfigStorage.setNotifyUrl(config.getNotifyUrl());
            wxPayConfigStorage.setSignType(config.getSignType().name());
			wxPayConfigStorage.setInputCharset("utf-8");
            
            // 3. 网络请求配置
            HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
//            // ssl 退款证书相关 不使用可注释
//            if(!"ssl 退款证书".equals(KEYSTORE)){
//                httpConfigStorage.setKeystore(KEYSTORE);
//                httpConfigStorage.setStorePassword(STORE_PASSWORD);
//                httpConfigStorage.setPath(true);
//            }
            // 请求连接池配置
            // 最大连接数
            httpConfigStorage.setMaxTotal(20);
            // 默认的每个路由的最大连接数
            httpConfigStorage.setDefaultMaxPerRoute(10);
            
            // 4. 配置支付服务
			WxPayService wxPayService = new WxPayService(wxPayConfigStorage, httpConfigStorage);
            Integer listId = config.getListId();
            if (ObjectUtils.isNotNull(payServiceMap.get(listId))) {
            	throw new RuntimeException(StrUtil.format("【支付配置】出现非全局唯一 listId：{}", listId));
            }
            payServiceMap.put(listId, wxPayService);
        });
	}
	
	private void aliPay() {
		// 1. 获得配置列表
        List<AliPayConfig> configList = aliPayProperties.getConfigList();
        if (ListUtils.isEmpty(configList)) {
        	throw new RuntimeException("【支付配置】无效的支付宝支付配置...");
        }
        
        configList.forEach(config -> {
			// 2. 支付配置
	        AliPayConfigStorage aliPayConfigStorage = new AliPayConfigStorage();
	        aliPayConfigStorage.setPid(config.getPid());
	        aliPayConfigStorage.setAppid(config.getAppId());
	        aliPayConfigStorage.setKeyPublic(config.getKeyPublic());
	        aliPayConfigStorage.setKeyPrivate(config.getKeyPrivate());
	        aliPayConfigStorage.setNotifyUrl(config.getNotifyUrl());
	        aliPayConfigStorage.setSignType(config.getSignType().name());
	        aliPayConfigStorage.setSeller(config.getSeller());
	        aliPayConfigStorage.setInputCharset(config.getInputCharset());
	        // 是否为测试账号，沙箱环境
	        aliPayConfigStorage.setTest(config.getTest());
	        
	        // 3. 网络请求配置
	        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
	        // 请求连接池配置
	        // 最大连接数
	        httpConfigStorage.setMaxTotal(20);
	        // 默认的每个路由的最大连接数
	        httpConfigStorage.setDefaultMaxPerRoute(10);
	        
            // 4. 配置支付服务
	        AliPayService aliPayService =  new AliPayService(aliPayConfigStorage, httpConfigStorage);
            Integer listId = config.getListId();
            if (ObjectUtils.isNotNull(payServiceMap.get(listId))) {
            	throw new RuntimeException(StrUtil.format("【支付配置】出现非全局唯一 listId：{}", listId));
            }
            payServiceMap.put(listId, aliPayService);
        });
	}
	
}
