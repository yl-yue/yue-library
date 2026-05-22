package ai.yue.library.web.util;

import cn.hutool.v7.core.net.NetUtil;
import jakarta.servlet.http.HttpServletRequest;

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
	 * <p>
	 * 注意：此方法依赖 RequestContextHolder ThreadLocal 获取当前请求，在 Servlet Filter 早期阶段可能尚未就绪，应使用 {@link #isInnerIP(HttpServletRequest)} 代替
	 * 
	 * @return 是否为内网IP
	 */
	public static Boolean isInnerIP() {
		return isInnerIP(ServletUtils.getRequest());
    }

	/**
	 * 判定是否为内网IP<br>
	 * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
	 * <p>
	 * 推荐在 Servlet Filter 中使用此重载，直接传入 request 对象，避免依赖 RequestContextHolder ThreadLocal
	 *
	 * @param request 请求对象，用于获取客户端真实IP（考虑反向代理）
	 * @return 是否为内网IP
	 */
	public static Boolean isInnerIP(HttpServletRequest request) {
		// 1. 获取客户端IP地址，考虑反向代理的问题
		String ip = ServletUtils.getClientIP(request);

		// 2. 确认是否为内网IP
		if (ip.equals(LOCAL_IPV6)) {
			return true;
		}
		return isInnerIP(ip);
	}
	
}
