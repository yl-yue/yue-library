package ai.yue.library.data.jdbc.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库字段名
 * 
 * @author	ylyue
 * @since	2020年2月24日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface FieldName {

	/**
	 * <b>数据库字段名，用于绑定关系映射</b>
	 * <p>成员变量名称：deleted
	 * <p>数据库字段名称：is_deleted
	 * <p>示例代码如下：
	 * <p><code>
	 * 	    @FieldName("is_deleted")<br>
    		private boolean deleted;
	 * </code>
	 */
	String value() default "";
	
}
