package ai.yue.library.base.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import cn.hutool.v7.core.reflect.FieldUtil;

/**
 * 互斥关系校验器
 *
 * @author	ylyue
 * @since	2019年5月8日
 */
public class ExclusionValidator implements ConstraintValidator<Exclusion, Object> {

	private String[] exclusions;

	@Override
	public void initialize(Exclusion constraintAnnotation) {
		this.exclusions = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		// 互斥关系逻辑，多个字段只能其中一个有值
		boolean isExclusionValueExist = false;
		for (String exclusion : exclusions) {
			if (FieldUtil.getFieldValue(value, exclusion) != null) {
				if (isExclusionValueExist == false) {
					isExclusionValueExist = true;
				} else {
					// 不满足互斥关系
					return false;
				}
			}
		}

		return true;
	}

}
