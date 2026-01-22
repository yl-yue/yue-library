package ai.yue.library.test.controller.web.qps.limit;

import ai.yue.library.base.annotation.QpsLimit;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import cn.hutool.v7.core.thread.ThreadUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web/qpsLimit")
public class QpsLimitConroller {

	@QpsLimit(qps = 5)
	@GetMapping("/get")
	public Result<?> get() {
		ThreadUtil.sleep(500);
		return R.success();
	}
	
}
