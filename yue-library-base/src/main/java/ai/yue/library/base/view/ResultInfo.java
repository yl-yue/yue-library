package ai.yue.library.base.view;

/**
 * 便捷返回-结果信息
 * @author 孙金川 2017/7/31
 *
 */
public class ResultInfo {
	
	/* 正确结果 */
	// 200
	
	/**
	 * 成功后调用，返回的data为null
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> success() {
		return ResultUtils.success();
	}
	/**
	 * 成功后调用，返回的data为一个对象
	 * @param <T> 泛型
	 * @param data 数据
	 * @return HTTP请求，最外层响应对象
	 */
	public static <T> Result<T> success(T data) {
		return ResultUtils.success(data);
	}
	/**
	 * 成功后调用，分页查询
	 * @param <T> 泛型
	 * @param data 数据
	 * @param count 总数
	 * @return HTTP请求，最外层响应对象
	 */
	public static <T> Result<T> success(T data, Long count) {
		return ResultUtils.success(data, count);
	}
	/**
	 * 成功后调用，分页查询
	 * @param <T>		泛型
	 * @param code		自定义code（默认200）
	 * @param data		数据
	 * @param count		总数
	 * @return HTTP请求，最外层响应对象
	 */
	public static <T> Result<T> success(Integer code, T data, Long count) {
		return ResultUtils.success(code, data, count);
	}
	
	/* 错误提示 */
	// 100
	
	/**
	 * 非法访问-100
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> attack() {
		return ResultUtils.error(ResultEnum.ATTACK.getCode(), ResultEnum.ATTACK.getMsg());
	}
	/**
	 * 非法访问-100
	 * @param <T> 泛型
	 * @param data 异常数据
	 * @return HTTP请求，最外层响应对象
	 */
	public static <T> Result<T> attack(T data) {
		return ResultUtils.error(ResultEnum.ATTACK.getCode(), ResultEnum.ATTACK.getMsg(), data);
	}
	/**
	 * 未登录或登录已失效-101
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> unauthorized() {
		return ResultUtils.error(ResultEnum.UNAUTHORIZED.getCode(), ResultEnum.UNAUTHORIZED.getMsg());
	}
	/**
	 * 会话未注销，无需登录-102
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> logged_in() {
		return ResultUtils.error(ResultEnum.LOGGED_IN.getCode(), ResultEnum.LOGGED_IN.getMsg());
	}
	/**
	 * 无权限-103
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> forbidden() {
		return ResultUtils.error(ResultEnum.FORBIDDEN.getCode(), ResultEnum.FORBIDDEN.getMsg());
	}
	/**
	 * 频繁访问限制，请稍后重试-104
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> frequent_access_restriction() {
		return ResultUtils.error(ResultEnum.FREQUENT_ACCESS_RESTRICTION.getCode(),
				ResultEnum.FREQUENT_ACCESS_RESTRICTION.getMsg());
	}
	
	// 300
	
	/**
	 * 参数解密错误-300
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> decrypt_error() {
		return ResultUtils.error(ResultEnum.DECRYPT_ERROR.getCode(), ResultEnum.DECRYPT_ERROR.getMsg());
	}
	/**
	 * JSON格式字符串解析错误-301
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> json_parse_error() {
		return ResultUtils.error(ResultEnum.JSON_PARSE_ERROR.getCode(), ResultEnum.JSON_PARSE_ERROR.getMsg());
	}
	/**
	 * 文件上传请求错误，获得文件信息为空，同时文件必须有明确的匹配类型（如文本类型：.txt）-302
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?>  file_empty() {
		return ResultUtils.error(ResultEnum.FILE_EMPTY.getCode(), ResultEnum.FILE_EMPTY.getMsg());
	}
	
	// 400
	
	/**
	 * 参数为空-400
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> param_void() {
		return ResultUtils.error(ResultEnum.PARAM_VOID.getCode(), ResultEnum.PARAM_VOID.getMsg());
	}
	/**
	 * 参数校验未通过，请参照API核对后重试-401
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> param_check_not_pass() {
		return ResultUtils.error(ResultEnum.PARAM_CHECK_NOT_PASS.getCode(), ResultEnum.PARAM_CHECK_NOT_PASS.getMsg());
	}
	/**
	 * 参数校验未通过，请参照API核对后重试-401
	 * @param data 数据
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<String> param_check_not_pass(String data) {
		return ResultUtils.error(ResultEnum.PARAM_CHECK_NOT_PASS.getCode(), ResultEnum.PARAM_CHECK_NOT_PASS.getMsg(), data);
	}
	/**
	 * 参数校验未通过，无效的value-402
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> param_value_invalid() {
		return ResultUtils.error(ResultEnum.PARAM_VALUE_INVALID.getCode(), ResultEnum.PARAM_VALUE_INVALID.getMsg());
	}
	/**
	 * 参数校验未通过，无效的value-402
	 * @param data 数据
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<String> param_value_invalid(String data) {
		return ResultUtils.error(ResultEnum.PARAM_VALUE_INVALID.getCode(), ResultEnum.PARAM_VALUE_INVALID.getMsg(), data);
	}
	
	// 500
	
	/**
	 * 请求错误-500
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> error() {
		return ResultUtils.error(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getMsg());
	}
	/**
	 * 请求错误-500
	 * @param <T> 泛型
	 * @param data	异常数据
	 * @return HTTP请求，最外层响应对象
	 */
	public static <T> Result<T> error(T data) {
		return ResultUtils.error(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getMsg(), data);
	}
	/**
	 * 数据结构异常-501
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> data_structure() {
		return ResultUtils.error(ResultEnum.DATA_STRUCTURE.getCode(), ResultEnum.DATA_STRUCTURE.getMsg());
	}
	/**
	 * 数据结构异常-501
	 * <p><i>不正确的结果大小</i>
	 * 
	 * @param expected	预期值
	 * @param actual	实际值
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> data_structure(int expected, int actual) {
		String data = "Incorrect result size: expected " + expected + ", actual " + actual;
		return ResultUtils.error(ResultEnum.DATA_STRUCTURE.getCode(), ResultEnum.DATA_STRUCTURE.getMsg(), data);
	}
	/**
	 * 数据结构异常，请检查相应数据结构一致性-502
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> db_error() {
		return ResultUtils.error(ResultEnum.DB_ERROR.getCode(), ResultEnum.DB_ERROR.getMsg());
	}
	/**
	 * 哎哟喂！网络开小差了，请稍后重试...-503
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?>  client_fallback() {
		return ResultUtils.error(ResultEnum.CLIENT_FALLBACK.getCode(), ResultEnum.CLIENT_FALLBACK.getMsg());
	}
	/**
	 * 哎哟喂！服务都被您挤爆了...-504
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?>  client_fallback_error() {
		return ResultUtils.error(ResultEnum.CLIENT_FALLBACK_ERROR.getCode(), ResultEnum.CLIENT_FALLBACK_ERROR.getMsg());
	}
	
	// 600
	
	/**
	 * 自定义类型提示-msg
	 * @param msg 提示消息
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> dev_defined(String msg) {
		return ResultUtils.error(ResultEnum.DEV_DEFINED.getCode(), msg);
	}
	
	// 610
	
	/**
	 * 用户已存在
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> user_exist() {
		return ResultUtils.error(ResultEnum.USER_EXIST.getCode(), ResultEnum.USER_EXIST.getMsg());
	}
	/**
	 * 用户不存在
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> user_no_exist() {
		return ResultUtils.error(ResultEnum.USER_NO_EXIST.getCode(), ResultEnum.USER_NO_EXIST.getMsg());
	}
	/**
	 * 用户已停用
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> user_stop() {
		return ResultUtils.error(ResultEnum.USER_STOP.getCode(), ResultEnum.USER_STOP.getMsg());
	}
	/**
	 * 无效的用户
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> user_invalid() {
		return ResultUtils.error(ResultEnum.USER_INVALID.getCode(), ResultEnum.USER_INVALID.getMsg());
	}
	/**
	 * 用户名或密码错误
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> user_or_password_error() {
		return ResultUtils.error(ResultEnum.USERNAME_OR_PASSWORD_ERROR.getCode(), ResultEnum.USERNAME_OR_PASSWORD_ERROR.getMsg());
	}
	/**
	 * 原密码错误
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> original_password_error() {
		return ResultUtils.error(ResultEnum.ORIGINAL_PASSWORD_ERROR.getCode(), ResultEnum.ORIGINAL_PASSWORD_ERROR.getMsg());
	}
	/**
	 * 手机号已被注册
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> phone_exist() {
		return ResultUtils.error(ResultEnum.PHONE_EXIST.getCode(), ResultEnum.PHONE_EXIST.getMsg());
	}
	
	// 620
	
	/**
	 * 无效的口令
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> password_invalid() {
		return ResultUtils.error(ResultEnum.PASSWORD_INVALID.getCode(), ResultEnum.PASSWORD_INVALID.getMsg());
	}
	/**
	 * 验证码错误
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> captcha_error() {
		return ResultUtils.error(ResultEnum.CAPTCHA_ERROR.getCode(), ResultEnum.CAPTCHA_ERROR.getMsg());
	}
	/**
	 * 手机号码错误
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> cellphone_error() {
		return ResultUtils.error(ResultEnum.CELLPHONE_ERROR.getCode(), ResultEnum.CELLPHONE_ERROR.getMsg());
	}
	/**
	 * 身份证号码错误
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> id_card_number_error() {
		return ResultUtils.error(ResultEnum.ID_CARD_NUMBER_ERROR.getCode(), ResultEnum.ID_CARD_NUMBER_ERROR.getMsg());
	}
	/**
	 * 邮箱验证错误
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> email_error() {
		return ResultUtils.error(ResultEnum.EMAIL_ERROR.getCode(), ResultEnum.EMAIL_ERROR.getMsg());
	}
	/**
	 * 邀请码无效
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> invite_code_invalid() {
		return ResultUtils.error(ResultEnum.INVITE_CODE_INVALID.getCode(), ResultEnum.INVITE_CODE_INVALID.getMsg());
	}

	// 630

	/**
	 * 帐户已被绑定
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> account_exist_binding() {
		return ResultUtils.error(ResultEnum.ACCOUNT_EXIST_BINDING.getCode(), ResultEnum.ACCOUNT_EXIST_BINDING.getMsg());
	}
	/**
	 * 帐户存在业务，暂时不允许改变状态
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> account_exist_business_not_allow_change() {
		return ResultUtils.error(ResultEnum.ACCOUNT_EXIST_BUSINESS_NOT_ALLOW_CHANGE.getCode(), ResultEnum.ACCOUNT_EXIST_BUSINESS_NOT_ALLOW_CHANGE.getMsg());
	}
	/**
	 * 帐户余额不足
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> account_balance_not_enough() {
		return ResultUtils.error(ResultEnum.ACCOUNT_BALANCE_NOT_ENOUGH.getCode(), ResultEnum.ACCOUNT_BALANCE_NOT_ENOUGH.getMsg());
	}
	
}
