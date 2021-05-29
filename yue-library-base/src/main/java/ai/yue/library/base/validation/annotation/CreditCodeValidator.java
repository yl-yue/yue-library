package ai.yue.library.base.validation.annotation;

import ai.yue.library.base.util.StringUtils;
import cn.hutool.core.lang.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 统一社会信用代码校验器
 *
 * @author	ylyue
 * @since	2019年5月8日
 */
public class CreditCodeValidator implements ConstraintValidator<CreditCode, String> {

	private boolean notNull;
	
	@Override
	public void initialize(CreditCode constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isNotBlank(value)) {
			return Validator.isCreditCode(value);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
