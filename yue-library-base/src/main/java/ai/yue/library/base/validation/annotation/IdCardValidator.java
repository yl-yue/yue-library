package ai.yue.library.base.validation.annotation;

import ai.yue.library.base.util.StrUtils;
import cn.hutool.v7.core.lang.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author	ylyue
 * @since	2019年5月8日
 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {

	private boolean notNull;
	
	@Override
	public void initialize(IdCard constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StrUtils.isNotBlank(value)) {
			return Validator.isCitizenId(value);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
