package ai.yue.library.base.validation.annotation;

import ai.yue.library.base.util.DateUtils;
import ai.yue.library.base.util.StrUtils;
import cn.hutool.v7.core.date.DateUtil;
import cn.hutool.v7.core.lang.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * 生日格式校验器
 *
 * @author	ylyue
 * @since	2019年5月8日
 */
public class BirthdayValidator implements ConstraintValidator<Birthday, Object> {

	private boolean notNull;
	
	@Override
	public void initialize(Birthday constraintAnnotation) {
		this.notNull = constraintAnnotation.notNull();
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		String validValue = null;
		if (value instanceof String) {
			validValue = (String) value;
		} else if (value instanceof Date) {
			validValue = DateUtil.formatDate((Date) value);
		} else if (value instanceof TemporalAccessor) {
			validValue = DateUtils.toDateFormatter((TemporalAccessor) value);
		}
		
		if (StrUtils.isNotBlank(validValue)) {
			return Validator.isBirthday(validValue);
		}
		
		if (notNull) {
			return false;
		}
		
		return true;
	}
	
}
