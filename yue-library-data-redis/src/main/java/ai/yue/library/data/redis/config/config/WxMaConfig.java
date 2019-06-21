package ai.yue.library.data.redis.config.config;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;

import ai.yue.library.base.util.ListUtils;
import ai.yue.library.data.redis.config.properties.WxMaProperties;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;

/**
 * @author  孙金川
 * @version 创建时间：2019年6月18日
 */
@Configuration
@EnableConfigurationProperties(WxMaProperties.class)
@ConditionalOnProperty(prefix = "yue.wx.miniapp", name = "enabled", havingValue = "true")
public class WxMaConfig {
	
	@Autowired
	private WxMaProperties wxMaProperties;
    private Map<String, WxMaService> maServices = Maps.newHashMap();
    
    public WxMaService getMaService(String appid) {
        WxMaService wxService = maServices.get(appid);
        if (wxService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        return wxService;
    }
    
    @PostConstruct
    private void init() {
        List<WxMaProperties.Config> configs = wxMaProperties.getConfigs();
        if (ListUtils.isEmpty(configs)) {
        	throw new RuntimeException("无效的小程序配置...");
        }
        
        maServices = configs.stream()
            .map(a -> {
                WxMaInMemoryConfig config = new WxMaInMemoryConfig();
                config.setAppid(a.getAppid());
                config.setSecret(a.getSecret());
//                config.setToken(a.getToken());
//                config.setAesKey(a.getAesKey());
//                config.setMsgDataFormat(a.getMsgDataFormat());

                WxMaService service = new WxMaServiceImpl();
                service.setWxMaConfig(config);
                return service;
            }).collect(Collectors.toMap(s -> s.getWxMaConfig().getAppid(), a -> a));
    }
    
}
