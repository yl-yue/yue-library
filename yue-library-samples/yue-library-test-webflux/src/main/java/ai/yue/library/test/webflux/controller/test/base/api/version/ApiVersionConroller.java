package ai.yue.library.test.webflux.controller.test.base.api.version;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.base.annotation.api.version.ApiVersion;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;

/**
 * @author	ylyue
 * @since	2019年8月2日
 */
@ApiVersion(2)
@RestController
@RequestMapping("/{version}/apiVersion")
public class ApiVersionConroller {

	/**
	 * get
	 * <p>弃用API接口版本演示
	 * 
	 * @param version
	 * @return
	 */
	@ApiVersion(deprecated = true)
	@GetMapping("/get")
	public Result<?> get(@PathVariable String version) {
		return ResultInfo.success("get：" + version);
	}
	
	/**
	 * get2
	 * 
	 * @param version
	 * @return
	 */
	@ApiVersion(value = 2, deprecated = true)
	@GetMapping("/get")
	public Result<?> get2(@PathVariable String version) {
		return ResultInfo.success("get2：" + version);
	}
	
	/**
	 * get3
	 * 
	 * @param version
	 * @return
	 */
	@ApiVersion(3.1)
	@GetMapping("/get")
	public Result<?> get3(@PathVariable String version) {
		return ResultInfo.success("get3：" + version);
	}
	
	/**
	 * get4
	 * 
	 * @param version
	 * @return
	 */
	@ApiVersion(4)
	@GetMapping("/get")
	public Result<?> get4(@PathVariable String version) {
		return ResultInfo.success("get4：" + version);
	}
	
}
