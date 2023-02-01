package ai.yue.library.base.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证互斥关系，多个字段只能其中一个有值
 *
 * @author	ylyue
 * @since	2019年5月8日
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE})
@Constraint(validatedBy = { ExclusionValidator.class })
@Repeatable(Exclusion.List.class)
public @interface Exclusion {

	/**
	 * 互斥关系，多个字段只能其中一个有值
	 */
	String[] value() default {};

	String message() default "{ai.yue.library.base.validation.annotation.Exclusion.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * Defines several {@code @Exclusion} annotations on the same element.
	 */
	@Target({TYPE})
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		Exclusion[] value();
	}

}
