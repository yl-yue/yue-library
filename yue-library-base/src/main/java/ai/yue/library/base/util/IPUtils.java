package ai.yue.library.base.util;

import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.net.NetUtil;

/**
 * @deprecated 请使用 {@linkplain NetUtils}
 * @author  ylyue
 * @version 创建时间：2018年4月4日
 */
@Deprecated
public class IPUtils {
	
	/**
	 * 判定是否为内网IP<br>
	 * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
	 * 
	 * @return 是否为内网IP
	 */
	public static Boolean isInnerIP() {
		// 1. 获取客户端IP地址，考虑反向代理的问题
		HttpServletRequest request = HttpUtils.getRequest();
		String ip = request.getHeader("x-forwarded-for");
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
				return true;
			}
		}
		
		// 2. 确认是否为内网IP
		return NetUtil.isInnerIP(ip);
    }
	
}
