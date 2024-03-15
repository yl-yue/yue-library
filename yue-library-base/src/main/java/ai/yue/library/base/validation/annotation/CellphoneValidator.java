package ai.yue.library.base.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ai.yue.library.base.util.StrUtils;
import cn.hutool.core.lang.Validator;

/**
 * @author	ylyue
 * @since	2019年5月8日
 */
public class CellphoneValidator implements ConstraintValidator<Cellphone, String> {

	private boolean notNull;
	
	@Override
	public void initialize(Cellphone constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StrUtils.isNotBlank(value)) {
			return Validator.isMobile(value);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
