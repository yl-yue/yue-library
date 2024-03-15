package ai.yue.library.base.validation.annotation;

import ai.yue.library.base.util.StrUtils;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author	ylyue
 * @since	2019年5月8日
 */
public class ChineseValidator implements ConstraintValidator<Chinese, Object> {

	private boolean notNull;
	
	@Override
	public void initialize(Chinese constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		String validValue = null;
		if ((value != null && CharUtil.isChar(value) && !CharUtil.isBlankChar((char) value))
				|| (value instanceof String && StrUtil.isNotBlank((String) value))) {
			validValue = StrUtil.toString(value);
		}
		
		if (StrUtils.isNotBlank(validValue)) {
			return Validator.isChinese(validValue);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
