package ai.yue.library.base.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.lang.Console;

/**
 * @author	孙金川
 * @version 创建时间：2018年12月18日
 */
public class HttpUtils {
	
	public static final String HTTP_TCP_NAME = "http://";
	public static final String HTTPS_TCP_NAME = "https://";
	
	/**
	 * 获得当前请求的服务器的URL地址
	 * <p>
	 * 示例一：http://localhost:8080<br>
	 * 示例二：http://localhost:8080/projectName<br>
	 * @param request
	 * @return
	 */
	public static String getServerURL(HttpServletRequest request) {
		String serverName = request.getServerName();// 服务器地址
		int serverPort = request.getServerPort();// 端口号
		String contextPath = request.getContextPath();// 项目名称
		return HTTP_TCP_NAME + serverName + ":" + serverPort + contextPath;
	}
	
	/**
	 * 打印请求报文
	 * <p>
	 * 注意：打印不包括：异步请求内容、数据流
	 * @param request
	 */
	public static void printRequest(HttpServletRequest request) {
		// 1. 打印请求信息
		Console.error("========开始-打印请求报文========");
		Console.log();
		Console.log("打印请求信息：");
		Console.log("RemoteAddr：{}", request.getRemoteAddr());
		Console.log("Method：{}", request.getMethod());
		Console.log("AuthType：{}", request.getAuthType());
		
		// 2. 打印服务器信息
		Console.log();
		Console.log("打印服务器信息：");
		Console.log("ServerURL：{}", getServerURL(request));
		Console.log("RequestURL：{}", request.getRequestURL());
		Console.log("RequestedSessionId：{}", request.getRequestedSessionId());
		
		// 3. 打印请求参数
		Console.log();
		Console.log("打印请求参数：");
		Console.log("QueryString：{}", request.getQueryString());
		Console.log("ParameterMap：{}", JSONObject.toJSONString(request.getParameterMap()));
		
		// 4. 打印请求头
		Console.log();
		Console.log("打印请求头：");
		Console.log("Headers：");
		request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
			StringBuilder headerValues = new StringBuilder();
			request.getHeaders(headerName).asIterator().forEachRemaining(headerValue -> {
				headerValues.append(headerValue);
			});;
			Console.log("　　{}：{}", headerName, headerValues);
		});;
		
		// 5. 打印Cookie
		Console.log();
		Console.log("Cookies：");
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				Console.log("　　{}：{}", cookie.getName(), cookie.getValue());
			}
		}
		Console.error("========结束-打印请求报文========");
	}
	
}
