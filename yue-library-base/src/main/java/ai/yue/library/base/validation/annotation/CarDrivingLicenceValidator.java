package ai.yue.library.base.validation.annotation;

import ai.yue.library.base.util.StrUtils;
import cn.hutool.core.lang.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 驾驶证格式校验器
 *
 * @author	ylyue
 * @since	2019年5月8日
 */
public class CarDrivingLicenceValidator implements ConstraintValidator<CarDrivingLicence, String> {

	private boolean notNull;
	
	@Override
	public void initialize(CarDrivingLicence constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StrUtils.isNotBlank(value)) {
			return Validator.isCarDrivingLicence(value);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
