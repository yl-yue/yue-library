package ai.yue.library.test.controller.base.datetime.format;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.ipo.UserIPO;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author	ylyue
 * @since	2020年2月29日
 */
@RestController
@RequestMapping("/dateTimeFormat")
public class DateTimeFormatController {

	/**
	 * 插入数据
	 * 
	 * @param userIPO
	 * @return
	 */
	@PostMapping("/insert")
	public Result<?> insert(@Valid UserIPO userIPO) {
		System.out.println(userIPO);
		System.out.println(JSONObject.toJSONString(userIPO));
		return R.success(userIPO);
	}
	
}
