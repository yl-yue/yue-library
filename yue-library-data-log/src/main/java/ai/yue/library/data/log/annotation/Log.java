package ai.yue.library.data.log.annotation;

import ai.yue.library.data.log.constant.LogTypeEnum;
import ai.yue.library.data.mybatis.constant.DbCrudEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志注解
 * <p>标注在Controller方法上，自动记录操作日志</p>
 *
 * @author ylyue
 * @since 2025/5/13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 日志类型（默认操作日志）
     */
    LogTypeEnum type() default LogTypeEnum.OPER;

    /**
     * 操作标题（必填）
     */
    String title();

    /**
     * 操作描述（可选）
     */
    String description() default "";

    /**
     * 操作类型（可选，默认从 HTTP Method 推断）
     * <p>POST→C, GET→R, PUT/PATCH→U, DELETE→D</p>
     */
    DbCrudEnum operType() default DbCrudEnum.R;

    /**
     * 业务分类（可选，如 IMPORT/EXPORT 等特殊操作）
     */
    String bizType() default "";

}
