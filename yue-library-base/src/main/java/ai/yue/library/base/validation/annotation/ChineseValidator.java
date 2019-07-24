package ai.yue.library.base.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ai.yue.library.base.util.StringUtils;
import cn.hutool.core.lang.Validator;

/**
 * @author	孙金川
 * @since	2019年5月8日
 */
public class ChineseValidator implements ConstraintValidator<Chinese, String> {

	private boolean notNull;
	
	@Override
	public void initialize(Chinese constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isNotBlank(value)) {
			return Validator.isChinese(value);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
