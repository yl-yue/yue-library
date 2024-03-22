package ai.yue.library.web.config.api.version;

import ai.yue.library.base.annotation.api.version.ApiVersion;
import ai.yue.library.base.annotation.api.version.ApiVersionProperties;
import ai.yue.library.base.exception.ApiDeprecatedException;
import ai.yue.library.base.util.StrUtils;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;

/**
 * 优雅的接口版本控制，URL匹配器
 *
 * @author	ylyue
 * @since	2020年2月26日
 */
@Data
@AllArgsConstructor
public class ApiVersionRequestCondition implements RequestCondition<ApiVersionRequestCondition> {

	private ApiVersion apiVersion;
	private ApiVersionProperties apiVersionProperties;
	/**
	 * {@link RequestMapping} 版本占位符索引
	 */
	private Integer versionPlaceholderIndex;
	
	@Override
	public ApiVersionRequestCondition combine(ApiVersionRequestCondition apiVersionRequestCondition) {
		// 最近优先原则：在方法上的 {@link ApiVersion} 可覆盖在类上面的 {@link ApiVersion}
		return new ApiVersionRequestCondition(apiVersionRequestCondition.getApiVersion(), apiVersionRequestCondition.getApiVersionProperties(), apiVersionRequestCondition.getVersionPlaceholderIndex());
	}
	
    @Override
    public ApiVersionRequestCondition getMatchingCondition(HttpServletRequest request) {
    	// 校验请求url中是否包含版本信息
    	String requestURI = request.getRequestURI();
    	String[] versionPaths = StrUtils.splitToArray(requestURI, "/");
    	double pathVersion = Double.valueOf(versionPaths[versionPlaceholderIndex].substring(1));
		
		// pathVersion的值大于等于apiVersionValue皆可匹配，除非ApiVersion的deprecated值已被标注为true
		double apiVersionValue = this.getApiVersion().value();
		if (pathVersion >= apiVersionValue) {
			double minimumVersion = apiVersionProperties.getMinimumVersion();
			if ((this.getApiVersion().deprecated() || minimumVersion > pathVersion) && NumberUtil.equals(pathVersion, apiVersionValue)) {
				// 匹配到弃用版本接口
				throw new ApiDeprecatedException(StrUtil.format("客户端调用弃用版本API接口，requestURI：{}", requestURI));
			} else if (this.getApiVersion().deprecated()) {
				// 继续匹配
				return null;
			}

			// 匹配成功
			return this;
		}

		// 继续匹配
		return null;
	}
    
	@Override
	public int compareTo(ApiVersionRequestCondition apiVersionRequestCondition, HttpServletRequest request) {
		// 当出现多个符合匹配条件的ApiVersionCondition，优先匹配版本号较大的
		return NumberUtil.compare(apiVersionRequestCondition.getApiVersion().value(), getApiVersion().value());
	}

}
