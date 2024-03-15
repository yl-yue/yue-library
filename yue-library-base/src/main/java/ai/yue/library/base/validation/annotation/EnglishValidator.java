package ai.yue.library.base.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ai.yue.library.base.util.StrUtils;
import cn.hutool.core.lang.Validator;

/**
 * @author	ylyue
 * @since	2019年5月8日
 */
public class EnglishValidator implements ConstraintValidator<English, String> {

	private boolean notNull;
	
	@Override
	public void initialize(English constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StrUtils.isNotBlank(value)) {
			return Validator.isWord(value);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
