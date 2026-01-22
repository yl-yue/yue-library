package ai.yue.library.base.validation.annotation;

import ai.yue.library.base.util.StrUtils;
import cn.hutool.v7.core.lang.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * MAC地址校验器
 *
 * @author	ylyue
 * @since	2019年5月8日
 */
public class MacAddressValidator implements ConstraintValidator<MacAddress, String> {

	private boolean notNull;
	
	@Override
	public void initialize(MacAddress constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StrUtils.isNotBlank(value)) {
			return Validator.isMac(value);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
