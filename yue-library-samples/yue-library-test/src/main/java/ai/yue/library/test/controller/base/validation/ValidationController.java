package ai.yue.library.test.controller.base.validation;

import ai.yue.library.base.ipo.ValidationGroups;
import ai.yue.library.base.util.ParamUtils;
import ai.yue.library.base.validation.Validator;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.ipo.ValidationAnnotationInIPO;
import ai.yue.library.test.ipo.ValidationGroupIPO;
import ai.yue.library.test.ipo.ValidationIPO;
import ai.yue.library.test.ipo.ValidationMutualIPO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;

/**
 * @author  ylyue
 * @version 创建时间：2019年6月25日
 */
@RestController
@RequestMapping("/validation")
public class ValidationController {

	@Autowired
	private Validator validator;
	
	@PostMapping("/valid")
	public Result<?> valid(@Valid ValidationIPO validationIPO) {
		System.out.println(validationIPO);
		return R.success(validationIPO);
	}

	@PostMapping("/validMutual")
	public Result<?> validMutual(@Valid ValidationMutualIPO validationMutualIPO) {
		System.out.println(validationMutualIPO);
		return R.success(validationMutualIPO);
	}

	@PostMapping("/validatorValid")
	public Result<?> validatorValid(ValidationIPO validationIPO) {
		validator.valid(validationIPO);
		System.out.println(validationIPO);
		return R.success(validationIPO);
	}
	
	@PostMapping("/validatorParam")
	public Result<?> validatorParam(ValidationIPO validationIPO) {
		// 参数
		String name = validationIPO.getName();
		String email = validationIPO.getEmail();
		String cellphone = validationIPO.getCellphone();
		int age = validationIPO.getAge();
		LocalDate birthday = validationIPO.getBirthday();
		
		// 单个参数校验
		validator.param(email).email("email");
		validator.param(cellphone).cellphone("cellphone");
		validator.param(name).notNull("name").chinese("name").length(1, 30, "name");
		
		// 单个参数校验-通过param()连写（连写直接切换校验对象）
		validator.param(name).notNull("name").param(email).length(5, 25, "email").param(age).min(20, "age").max(60, "age");
		
		// POJO对象校验-通过调用validator.valid()方法
		validator.valid(validationIPO);
		// 同样支持连写
		validator.valid(validationIPO).param(birthday).birthday("birthday");

		// 获得参数校验器-静态方法
		Validator.getValidatorAndSetParam(email).email("email");

		// 返回结果
		return R.success(validationIPO);
	}

	/**
	 * 将`@Valid`注解添加到POJO类上
	 *
	 * @param validationAnnotationInIPO 校验请求参数
	 * @return 结果
	 */
	@PostMapping("/validAnnotationInPOJO")
	public Result<?> validAnnotationInPOJO(ValidationAnnotationInIPO validationAnnotationInIPO) {
		System.out.println(validationAnnotationInIPO);
		return R.success(validationAnnotationInIPO);
	}

	/**
	 * 分组校验-Create
	 *
	 * @param validationGroupIPO 分组校验请求参数
	 * @return 结果
	 */
	@PostMapping("/validationGroupCreate")
	public Result<?> validationGroupCreate(@Validated(ValidationGroups.Create.class) ValidationGroupIPO validationGroupIPO) {
		System.out.println(validationGroupIPO);
		return R.success(validationGroupIPO);
	}

	/**
	 * 分组校验-Update
	 *
	 * @param validationGroupIPO 分组校验请求参数
	 * @return 结果
	 */
	@PostMapping("/validationGroupUpdate")
	public Result<?> validationGroupUpdate(@Validated(ValidationGroups.Update.class) ValidationGroupIPO validationGroupIPO) {
		System.out.println(validationGroupIPO);
		return R.success(validationGroupIPO);
	}

	@PostMapping("/validJson")
	public Result<?> validJson(JSONObject paramJson) {
		String[] mustContainKeys = {"name", "birthday", "idcard"};
		String[] canContainKeys = {"age", "cellphone"};
		ParamUtils.paramValidate(paramJson, mustContainKeys, canContainKeys);
		return R.success(paramJson);
	}

}
