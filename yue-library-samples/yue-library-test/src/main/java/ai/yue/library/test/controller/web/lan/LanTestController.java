package ai.yue.library.test.controller.web.lan;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lan/v1/test")
public class LanTestController {

	@GetMapping("/ping")
	public Result<?> ping() {
		return R.success("pong");
	}
}
