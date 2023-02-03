package ai.yue.library.base.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * <b>I18n（国际化）</b>
 * <p>资源包定义示例：</p>
 * <pre class="code">
 *     messages.properties
 *     messages_zh_CN.properties
 *     messages_en.properties
 * </pre>
 *
 * @author yl-yue
 * @since  2023/1/31
 */
@Slf4j
@Component
public class I18nUtils {

	private static MessageSource messageSource;

	public I18nUtils(MessageSource messageSource) {
		I18nUtils.messageSource = messageSource;
	}

	/**
	 * 获取单个国际化翻译值
	 *
	 * @param code 资源包中定义的key
	 * @param args 资源包中定义的{value}占位符参数，顺序匹配（可空参数）
	 * @return 动态匹配资源包中定义的翻译value
	 */
	public static String get(String code, @Nullable Object... args) {
		try {
			return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				e.printStackTrace();
			}
			return code;
		}
	}

}
