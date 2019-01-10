package ai.yue.library.base.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.util.NetUtil;

/**
 * @author  孙金川
 * @version 创建时间：2018年4月4日
 */
public class IPUtils {
	
	/**
	 * 判定是否为内网IP<br>
	 * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
	 * 
	 * @param request
	 * @return 是否为内网IP
	 */
	public static Boolean isInnerIP(HttpServletRequest request) {
		// 1. 获取客户端IP地址，考虑反向代理的问题
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
	
	@Deprecated
	public static Boolean isOuterNet(HttpServletRequest request) {
        // 获取客户端IP地址，考虑反向代理的问题
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
                InetAddress inet = null;
	            try {
					inet = InetAddress.getLocalHost();
					ip = inet.getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
            }
        }
        if (!StringUtils.isEmpty(ip) && ip.length() > 15) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        /*
         *  判断客户单IP地址是否为内网地址
         *  内网IP网段：
         *  10.0.0.0-10.255.255.255
         *  172.16.0.0-172.31.255.255
         *  192.168.0.0-192.168.255.255
         */
        String regex = "^(192\\.168|172\\.(1[6-9]|2\\d|3[0,1]))(\\.(2[0-4]\\d|25[0-5]|[0,1]?\\d?\\d)){2}$|^10(\\.([2][0-4]\\d|25[0-5]|[0,1]?\\d?\\d)){3}$";
        return !ip.matches(regex);
    }
	
}
