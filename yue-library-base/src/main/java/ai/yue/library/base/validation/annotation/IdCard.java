package ai.yue.library.base.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证是否为身份证号码（18位中国）<br>
 * 出生日期只支持到到2999年
 * 
 * @author	ylyue
 * @since	2019年5月8日
 */
@Documented
@Retention(RUNTIME)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Constraint(validatedBy = { IdCardValidator.class })
@Repeatable(IdCard.List.class)
public @interface IdCard {
	
	/**
	 * 是否不允许为空 {@linkplain NotNull}
	 * @return 默认：true
	 */
	boolean notNull() default true;
	
	String message() default "{ai.yue.library.base.validation.annotation.IdCard.message}";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};

	/**
	 * Defines several {@code @IdCard} annotations on the same element.
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		IdCard[] value();
	}

}
