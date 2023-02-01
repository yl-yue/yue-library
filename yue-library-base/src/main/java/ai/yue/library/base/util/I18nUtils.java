package ai.yue.library.base.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * I18n（国际化）
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
	 */
	public static String get(String msgKey) {
		try {
			return messageSource.getMessage(msgKey, null, LocaleContextHolder.getLocale());
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				e.printStackTrace();
			}
			return msgKey;
		}
	}

}
