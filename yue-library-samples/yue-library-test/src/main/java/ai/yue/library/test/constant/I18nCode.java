package ai.yue.library.test.constant;

import ai.yue.library.base.view.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Result HTTP 状态码枚举
 * <p>参考{@linkplain HttpStatus}
 * 
 * @author	ylyue
 * @since	2017年10月8日
 */
@Getter
@AllArgsConstructor
public enum I18nCode implements ResultCode {
	
	// 200 - 正确结果
	
//	TEST_MSG(200, "ai.yue.library.test.constant.I18nCode.TEST_MSG");
	TEST_MSG(601, "ai.yue.library.test.constant.I18nCode.TEST_MSG"),
	TEST_MSG_ARGS(602, "ai.yue.library.test.constant.I18nCode.TEST_MSG_ARGS");

	private Integer code;
	private String msg;

}
