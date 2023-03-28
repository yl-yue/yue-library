package ai.yue.library.base.validation.annotation;

import ai.yue.library.base.util.StringUtils;
import cn.hutool.core.lang.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<Username, String> {

	private boolean notNull;
	
	@Override
	public void initialize(Username constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isNotBlank(value)) {
			if (value.length() < 5) {
                return false;
            }
			if (value.contains("@")) {
				return false;
			}

			return !Validator.isMobile(value);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
