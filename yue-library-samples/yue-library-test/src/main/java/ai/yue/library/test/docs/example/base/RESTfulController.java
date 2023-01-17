package ai.yue.library.test.docs.example.base;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.ipo.ValidationIPO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author	ylyue
 * @since	2019年10月12日
 */
@RestController
@RequestMapping("/restful")
public class RESTfulController {

	/**
	 * valid
	 * 
	 * @param validationIPO
	 * @return
	 */
	@PostMapping("/valid")
	public Result<?> valid(@Valid ValidationIPO validationIPO) {
		System.out.println(validationIPO);
		return R.success(validationIPO);
	}
	
}
