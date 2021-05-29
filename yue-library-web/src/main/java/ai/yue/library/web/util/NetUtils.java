package ai.yue.library.web.util;

import cn.hutool.core.net.NetUtil;

/**
 * 网络相关工具
 * 
 * @author	ylyue
 * @since	2018年4月4日
 */
public class NetUtils extends NetUtil {

	public final static String LOCAL_IPV6 = "0:0:0:0:0:0:0:1";

	/**
	 * 判定是否为内网IP<br>
	 * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
	 * 
	 * @return 是否为内网IP
	 */
	public static Boolean isInnerIP() {
		// 1. 获取客户端IP地址，考虑反向代理的问题
		String ip = ServletUtils.getClientIP(ServletUtils.getRequest());
		
		// 2. 确认是否为内网IP
		if (ip.equals(LOCAL_IPV6)) {
			return true;
		}
		return isInnerIP(ip);
    }
	
}
