package ai.yue.library.web.config.exception;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Sa-Token 异常处理自动配置
 * <p>仅在 classpath 存在 Sa-Token 时激活，注册 {@link SaTokenExceptionHandler}。
 * <p>使用字符串类名避免配置类自身触发 Sa-Token 类加载（sa-token 不存在时本配置直接跳过）。
 *
 * @author	ylyue
 * @since	2026年6月27日
 */
@Configuration
@ConditionalOnClass(name = "cn.dev33.satoken.exception.NotLoginException")
@Import(SaTokenExceptionHandler.class)
public class SaTokenExceptionAutoConfig {

}
