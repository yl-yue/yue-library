package ai.yue.library.base.config.api.version;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import ai.yue.library.base.exception.ApiVersionDeprecatedException;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author	ylyue
 * @since	2020年2月26日
 */
@Data
@AllArgsConstructor
public class ApiVersionRequestCondition implements RequestCondition<ApiVersionRequestCondition> {

	/**
	 * Restful API接口版本路径规则，如下示例：
	 * <p>/v1/
	 * <p>/v1.1/
	 * <p>/v99.9/
	 */
	private static final Pattern VERSION_PATH_PATTERN = Pattern.compile("^/v([1-9][0-9]?)(\\.[1-9])?/");
	
	private ApiVersion apiVersion;
	private ApiVersionProperties apiVersionProperties;
	
	@Override
	public ApiVersionRequestCondition combine(ApiVersionRequestCondition apiVersionRequestCondition) {
		// 最近优先原则：在方法上的 {@link ApiVersion} 可覆盖在类上面的 {@link ApiVersion}
		return new ApiVersionRequestCondition(apiVersionRequestCondition.getApiVersion(), apiVersionRequestCondition.getApiVersionProperties());
	}
	
    @Override
    public ApiVersionRequestCondition getMatchingCondition(HttpServletRequest request) {
    	// 校验请求url中是否包含版本信息
    	String requestURI = request.getRequestURI();
		String versionPath1 = ReUtil.getGroup1(VERSION_PATH_PATTERN, requestURI);
		if (StrUtil.isNotEmpty(versionPath1)) {
			String versionPath2 = ReUtil.get(VERSION_PATH_PATTERN, requestURI, 2);
			double pathVersion;
			if (StrUtil.isNotEmpty(versionPath2)) {
				pathVersion = Double.valueOf(versionPath1 + versionPath2);
			} else {
				pathVersion = Double.valueOf(versionPath1);
			}
			
			// 如果当前url中传递的版本信息高于(或等于)申明(或默认)版本，则用url的版本
			double apiVersionValue = this.getApiVersion().value();
			if (pathVersion >= apiVersionValue) {
				double minimumVersion = apiVersionProperties.getMinimumVersion();
				if ((this.getApiVersion().deprecated() || minimumVersion >= pathVersion) && pathVersion == apiVersionValue) {
					throw new ApiVersionDeprecatedException(StrUtil.format("客户端调用弃用版本API接口，requestURI：{}", requestURI));
				} else if (this.getApiVersion().deprecated()) {
					return null;
				}
                
				return this;
			}
		}
		
		return null;
	}
    
	@Override
	public int compareTo(ApiVersionRequestCondition apiVersionRequestCondition, HttpServletRequest request) {
		// 当出现多个符合匹配条件的ApiVersionCondition，优先匹配版本号较大的
		return NumberUtil.compare(apiVersionRequestCondition.getApiVersion().value(), getApiVersion().value());
	}

}
