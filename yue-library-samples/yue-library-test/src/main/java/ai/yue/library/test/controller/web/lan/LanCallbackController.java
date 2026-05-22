package ai.yue.library.test.controller.web.lan;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lan/v1/callback")
public class LanCallbackController {

	@GetMapping("/emqx/{topic}")
	public Result<?> emqxCallback(@PathVariable String topic) {
		return R.success("emqx-callback-" + topic);
	}
}
