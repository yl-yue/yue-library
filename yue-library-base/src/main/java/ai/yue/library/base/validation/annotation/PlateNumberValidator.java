package ai.yue.library.base.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ai.yue.library.base.util.StrUtils;
import cn.hutool.core.lang.Validator;

/**
 * @author	ylyue
 * @since	2019年5月8日
 */
public class PlateNumberValidator implements ConstraintValidator<PlateNumber, String> {

	private boolean notNull;
	
	@Override
	public void initialize(PlateNumber constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StrUtils.isNotBlank(value)) {
			return Validator.isPlateNumber(value);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
