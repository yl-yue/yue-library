package ai.yue.library.base.validation.annotation;

import ai.yue.library.base.util.StrUtils;
import cn.hutool.v7.core.lang.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author	ylyue
 * @since	2019年5月8日
 */
public class UUIDValidator implements ConstraintValidator<UUID, String> {

	private boolean notNull;
	
	@Override
	public void initialize(UUID constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StrUtils.isNotBlank(value)) {
			return Validator.isUUID(value);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
