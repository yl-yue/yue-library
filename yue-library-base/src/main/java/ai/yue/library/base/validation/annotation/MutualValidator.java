package ai.yue.library.base.validation.annotation;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * 相互关系校验器
 *
 * @author	ylyue
 * @since	2019年5月8日
 */
public class MutualValidator implements ConstraintValidator<Mutual, Object> {

	private String[] mutuals;
	private String[] exclusions;

	@Override
	public void initialize(Mutual constraintAnnotation) {
		this.mutuals = constraintAnnotation.mutuals();
		this.exclusions = constraintAnnotation.exclusions();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		// 相互关系逻辑，多个字段必须有一个有值
		boolean isMutualValueExist = false;
		for (String mutual : mutuals) {
			if (ReflectUtil.getFieldValue(value, mutual) != null) {
				isMutualValueExist = true;
			}
		}
		if (isMutualValueExist == false) {
			// 不满足相互关系
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(StrUtil.format("不满足相互关系，字段{}必须有一个有值。", Arrays.toString(mutuals))).addConstraintViolation();
			return false;
		}

		// 互斥关系逻辑，多个字段只能其中一个有值
		boolean isExclusionValueExist = false;
		for (String exclusion : exclusions) {
			if (ReflectUtil.getFieldValue(value, exclusion) != null) {
				if (isExclusionValueExist == false) {
					isExclusionValueExist = true;
				} else {
					// 不满足互斥关系
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate(StrUtil.format("不满足互斥关系，多个字段{}只能其中一个有值。", Arrays.toString(exclusions))).addConstraintViolation();
					return false;
				}
			}
		}

		return true;
	}
	
}
