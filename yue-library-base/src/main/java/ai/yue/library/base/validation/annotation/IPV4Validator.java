package ai.yue.library.base.validation.annotation;

import ai.yue.library.base.util.StrUtils;
import cn.hutool.v7.core.lang.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author	ylyue
 * @since	2019年5月8日
 */
public class IPV4Validator implements ConstraintValidator<IPV4, String> {

	private boolean notNull;
	
	@Override
	public void initialize(IPV4 constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StrUtils.isNotBlank(value)) {
			return Validator.isIpv4(value);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
