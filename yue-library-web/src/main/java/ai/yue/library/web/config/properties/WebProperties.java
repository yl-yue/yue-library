package ai.yue.library.web.config.properties;

import ai.yue.library.web.config.argument.resolver.RepeatedlyReadServletRequestFilter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Web自动配置属性
 * 
 * @author	ylyue
 * @since	2020年4月6日
 */
@Data
@ConfigurationProperties(WebProperties.PREFIX)
public class WebProperties {
	
	/**
	 * Prefix of {@link WebProperties}.
	 */
	public static final String PREFIX = "yue.web";
	
	/**
	 * 启用输入流可反复读取的HttpServletRequest
	 * <p>默认：true
	 */
	private boolean enableRepeatedlyReadServletRequest = true;
	
	/**
	 * {@link RepeatedlyReadServletRequestFilter} 优先级
	 * <p>值越小优先级越高
	 * <p>默认：-1200，比常规过滤器更高的优先级，防止输入流被更早读取
	 */
	private int repeatedlyReadServletRequestFilterOrder = -1200;

	/**
	 * 是否对 multipart 请求包装为可重复读
	 * <p>Servlet 规范限制：multipart 请求的 body 字节缓存与 getParts() 参数解析互斥，二者独占 InputStream 唯一一次可读性
	 * <p>false：multipart 请求跳过包装，让容器正常解析文件上传（@RequestPart 等），但 body 不可重复读
	 * <p>true：multipart 请求纯字节缓存，body 可重复读；此时需排除 Spring Boot MultipartAutoConfiguration，否则 getParts() 解析失败
	 * <p>默认：false（保全主流的 multipart 文件上传场景）
	 */
	private boolean repeatedlyReadMultipart = false;

	/**
	 * 跨域 CorsFilter 优先级
	 * <p>值越小优先级越高
	 * <p>默认：-10000，确保在所有业务 Filter 之前执行，避免认证 Filter 拒绝请求时缺少 CORS 响应头导致浏览器 CORS 错误
	 */
	private int corsFilterOrder = -10000;

	/**
	 * 启用接口幂等拦截
	 * <p>默认：true
	 */
	private boolean enableApiIdempotent = true;

	/**
	 * 启用接口QPS拦截
	 * <p>默认：true
	 */
	private boolean enableApiQpsLimit = true;

	/**
	 * 启用全局接口QPS拦截
	 * <p>默认：false
	 */
	private boolean enableGlobalApiQpsLimit = false;

	/**
	 * 全局接口最大QPS值
	 * <p>默认：3000
	 */
	private int globalApiQps = 3000;

	/**
	 * 启用Jvm空闲优化
	 * - 程序空闲时进行回收内存，解除内存占用，让服务器资源得到充分利用，弹性资源策略
	 * <p>默认：true
	 */
	private boolean enableJvmIdleOptimization = true;

}
