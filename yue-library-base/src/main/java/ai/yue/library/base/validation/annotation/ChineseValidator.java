package ai.yue.library.base.validation.annotation;

import ai.yue.library.base.util.StrUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import cn.hutool.v7.core.lang.Validator;
import cn.hutool.v7.core.text.CharUtil;
import cn.hutool.v7.core.text.StrUtil;

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
