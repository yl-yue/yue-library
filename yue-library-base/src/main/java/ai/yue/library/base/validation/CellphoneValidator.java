package ai.yue.library.base.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ai.yue.library.base.util.StringUtils;
import cn.hutool.core.lang.Validator;

/**
 * @author  孙金川
 * @version 创建时间：2019年5月8日
 */
public class CellphoneValidator implements ConstraintValidator<Cellphone, String> {

	private boolean notNull;
	
	@Override
	public void initialize(Cellphone constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isNotBlank(value)) {
			return Validator.isMobile(value);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
