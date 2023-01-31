package ai.yue.library.base.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证相互关系<br>
 * <ul>
 * <li>相互关系，多个字段必须有一个有值</li>
 * <li>互斥关系，多个字段只能其中一个有值</li>
 * </ul>
 * 
 * @author	ylyue
 * @since	2019年5月8日
 */
@Documented
@Retention(RUNTIME)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Constraint(validatedBy = { MutualValidator.class })
@Repeatable(Mutual.List.class)
public @interface Mutual {

	/**
	 * 相互关系，多个字段必须有一个有值
	 */
	String[] mutuals() default {};

	/**
	 * 互斥关系，多个字段只能其中一个有值
	 */
	String[] exclusions() default {};

	String message() default "{ai.yue.library.base.validation.annotation.Mutual.message}";

	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};

	/**
	 * Defines several {@code @Birthday} annotations on the same element.
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		Mutual[] value();
	}

}
