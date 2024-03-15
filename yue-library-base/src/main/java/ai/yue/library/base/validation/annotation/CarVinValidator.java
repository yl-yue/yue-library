package ai.yue.library.base.validation.annotation;

import ai.yue.library.base.util.StrUtils;
import cn.hutool.core.lang.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 车架号校验器
 *
 * @author	ylyue
 * @since	2019年5月8日
 */
public class CarVinValidator implements ConstraintValidator<CarVin, String> {

	private boolean notNull;
	
	@Override
	public void initialize(CarVin constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StrUtils.isNotBlank(value)) {
			return Validator.isCarVin(value);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
