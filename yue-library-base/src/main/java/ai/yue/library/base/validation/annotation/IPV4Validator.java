package ai.yue.library.base.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ai.yue.library.base.util.StringUtils;
import cn.hutool.core.lang.Validator;

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
		if (StringUtils.isNotBlank(value)) {
			return Validator.isIpv4(value);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
