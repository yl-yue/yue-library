package ai.yue.library.template.controller.validation;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.yue.library.base.validation.Validator;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import ai.yue.library.template.ipo.ValidationIPO;

/**
 * @author  孙金川
 * @version 创建时间：2019年6月25日
 */
@RestController
@RequestMapping("/validation")
public class ValidationController {

	@Autowired
	private Validator validator;
	
	/**
	 * valid
	 * @param validationIPO
	 * @return
	 */
	@PostMapping("/valid")
	public Result<?> valid(@Valid ValidationIPO validationIPO) {
		System.out.println(validationIPO);
		return ResultInfo.success(validationIPO);
	}
	
	/**
	 * validatorValid
	 * @param validationIPO
	 * @return
	 */
	@PostMapping("/validatorValid")
	public Result<?> validatorValid(ValidationIPO validationIPO) {
		validator.valid(validationIPO);
		System.out.println(validationIPO);
		return ResultInfo.success(validationIPO);
	}
	
	/**
	 * validatorParam
	 * @param validationIPO
	 * @return
	 */
	@PostMapping("/validatorParam")
	public Result<?> validatorParam(ValidationIPO validationIPO) {
		// 参数
		String name = validationIPO.getName();
		String email = validationIPO.getEmail();
		String cellphone = validationIPO.getCellphone();
		
		// 单个参数校验
		validator.param(email).email();
		validator.param(cellphone).cellphone();
		validator.param(name).notNull().chinese().length(30, 1);
		
		// 单个参数校验-通过param()连写（连写直接切换校验对象）
		validator.param("a").notNull().param("test").length(20, 4).param(50).min(20).max(60);
		
		// 单个参数校验-自定义错误信息
		validator.param("test").length(20, 4, "最大长度不能超过20个字，最小长度不能少于4个字");
		return ResultInfo.success(validationIPO);
	}
	
}
