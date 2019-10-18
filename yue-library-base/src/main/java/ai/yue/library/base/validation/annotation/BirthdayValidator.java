package ai.yue.library.base.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ai.yue.library.base.util.StringUtils;
import cn.hutool.core.lang.Validator;

/**
 * @author	ylyue
 * @since	2019年5月8日
 */
public class BirthdayValidator implements ConstraintValidator<Birthday, String> {

	private boolean notNull;
	
	@Override
	public void initialize(Birthday constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isNotBlank(value)) {
			return Validator.isBirthday(value);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
