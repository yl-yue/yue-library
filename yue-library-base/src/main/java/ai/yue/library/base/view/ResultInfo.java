package ai.yue.library.base.view;

/**
 * 便捷返回 - {@code Restful} 风格API结果
 * 
 * @author	ylyue
 * @since	2017年7月31日
 */
public class ResultInfo {
	
	// ------ Result error builder ------
	
    /**
     * 失败后调用
     *
     * @param code 状态码
     * @param msg  提示消息
     * @return
     */
    private static Result<?> error(Integer code, String msg) {
    	return Result.builder().code(code).msg(msg).flag(false).build();
    }
    
    /**
     * 失败后调用
     * 
     * @param <T>
     * @param code	状态码
     * @param msg	提示消息
     * @param data	异常数据
     * @return
     */
    private static <T> Result<T> error(Integer code, String msg, T data) {
    	return new Result<T>().toBuilder().code(code).msg(msg).flag(false).data(data).build();
    }
    
	// 100 - 访问限制
	
	/**
	 * 非法访问-100
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> attack() {
		return error(ResultEnum.ATTACK.getCode(), ResultEnum.ATTACK.getMsg());
	}
	/**
	 * 非法访问-100
	 * @param <T> 泛型
	 * @param data 异常数据
	 * @return HTTP请求，最外层响应对象
	 */
	public static <T> Result<T> attack(T data) {
		return error(ResultEnum.ATTACK.getCode(), ResultEnum.ATTACK.getMsg(), data);
	}
	/**
	 * 未登录或登录已失效-101
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> unauthorized() {
		return error(ResultEnum.UNAUTHORIZED.getCode(), ResultEnum.UNAUTHORIZED.getMsg());
	}
	/**
	 * 会话未注销，无需登录-102
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> logged_in() {
		return error(ResultEnum.LOGGED_IN.getCode(), ResultEnum.LOGGED_IN.getMsg());
	}
	/**
	 * 无权限-103
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> forbidden() {
		return error(ResultEnum.FORBIDDEN.getCode(), ResultEnum.FORBIDDEN.getMsg());
	}
	/**
	 * 频繁访问限制，请稍后重试-104
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> frequent_access_restriction() {
		return error(ResultEnum.FREQUENT_ACCESS_RESTRICTION.getCode(),
				ResultEnum.FREQUENT_ACCESS_RESTRICTION.getMsg());
	}
	
	// 200 - 正确结果
	
	/**
	 * 成功后调用，返回的data为null
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> success() {
    	return Result.builder()
    	.code(ResultEnum.SUCCESS.getCode())
    	.msg(ResultEnum.SUCCESS.getMsg())
    	.flag(true)
    	.build();
	}
	/**
	 * 成功后调用，返回的data为一个对象
	 * @param <T> 泛型
	 * @param data 数据
	 * @return HTTP请求，最外层响应对象
	 */
	public static <T> Result<T> success(T data) {
        return new Result<T>().toBuilder()
        .code(ResultEnum.SUCCESS.getCode())
        .msg(ResultEnum.SUCCESS.getMsg())
        .flag(true)
        .data(data)
        .build();
	}
	/**
	 * 成功后调用，分页查询
	 * @param <T> 泛型
	 * @param data 数据
	 * @param count 总数
	 * @return HTTP请求，最外层响应对象
	 */
	public static <T> Result<T> success(T data, Long count) {
        Result<T> result = new Result<T>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), true, data, count);
        return result;
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
        Result<T> result = new Result<T>(code, ResultEnum.SUCCESS.getMsg(), true, data, count);
        return result;
	}
	
    // 300 - 资源、重定向、定位等提示
	
	/**
	 * 资源已失效-300
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> resource_already_invalid() {
		return error(ResultEnum.RESOURCE_ALREADY_INVALID.getCode(), ResultEnum.RESOURCE_ALREADY_INVALID.getMsg());
	}
	/**
	 * 文件上传请求错误，获得文件信息为空，同时文件必须有明确的匹配类型（如文本类型：.txt）-301
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?>  file_empty() {
		return error(ResultEnum.FILE_EMPTY.getCode(), ResultEnum.FILE_EMPTY.getMsg());
	}
	/**
	 * 类型转换错误-302
	 * 
	 * @param data	提示信息
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> type_convert_error(String data) {
		return error(ResultEnum.TYPE_CONVERT_ERROR.getCode(), ResultEnum.TYPE_CONVERT_ERROR.getMsg(), data);
	}
	
    // 400 - 客户端错误
	
	/**
	 * 参数为空-400
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> param_void() {
		return error(ResultEnum.PARAM_VOID.getCode(), ResultEnum.PARAM_VOID.getMsg());
	}
	/**
	 * 参数校验未通过，请参照API核对后重试-401
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> param_check_not_pass() {
		return error(ResultEnum.PARAM_CHECK_NOT_PASS.getCode(), ResultEnum.PARAM_CHECK_NOT_PASS.getMsg());
	}
	/**
	 * 参数校验未通过，请参照API核对后重试-401
	 * @param data 数据
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<String> param_check_not_pass(String data) {
		return error(ResultEnum.PARAM_CHECK_NOT_PASS.getCode(), ResultEnum.PARAM_CHECK_NOT_PASS.getMsg(), data);
	}
	/**
	 * 参数校验未通过，无效的value-402
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> param_value_invalid() {
		return error(ResultEnum.PARAM_VALUE_INVALID.getCode(), ResultEnum.PARAM_VALUE_INVALID.getMsg());
	}
	/**
	 * 参数校验未通过，无效的value-402
	 * @param data 数据
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<String> param_value_invalid(String data) {
		return error(ResultEnum.PARAM_VALUE_INVALID.getCode(), ResultEnum.PARAM_VALUE_INVALID.getMsg(), data);
	}
	/**
	 * 参数解密错误-403
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> param_decrypt_error() {
		return error(ResultEnum.PARAM_DECRYPT_ERROR.getCode(), ResultEnum.PARAM_DECRYPT_ERROR.getMsg());
	}
	
    // 500 - 服务器错误
	
	/**
	 * 请求错误-500
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> error() {
		return error(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getMsg());
	}
	/**
	 * 请求错误-500
	 * @param <T> 泛型
	 * @param data	异常数据
	 * @return HTTP请求，最外层响应对象
	 */
	public static <T> Result<T> error(T data) {
		return error(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getMsg(), data);
	}
	/**
	 * 数据结构异常-501
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> data_structure() {
		return error(ResultEnum.DATA_STRUCTURE.getCode(), ResultEnum.DATA_STRUCTURE.getMsg());
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
		return error(ResultEnum.DATA_STRUCTURE.getCode(), ResultEnum.DATA_STRUCTURE.getMsg(), data);
	}
	/**
	 * 数据结构异常，请检查相应数据结构一致性-502
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> db_error() {
		return error(ResultEnum.DB_ERROR.getCode(), ResultEnum.DB_ERROR.getMsg());
	}
	/**
	 * 哎哟喂！网络开小差了，请稍后重试...-503
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?>  client_fallback() {
		return error(ResultEnum.CLIENT_FALLBACK.getCode(), ResultEnum.CLIENT_FALLBACK.getMsg());
	}
	/**
	 * 哎哟喂！服务都被您挤爆了...-504
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?>  client_fallback_error() {
		return error(ResultEnum.CLIENT_FALLBACK_ERROR.getCode(), ResultEnum.CLIENT_FALLBACK_ERROR.getMsg());
	}
	
	// 600 - 自定义错误提示
	
	/**
	 * 自定义类型提示-msg
	 * @param msg 提示消息
	 * @return HTTP请求，最外层响应对象
	 */
	public static Result<?> dev_defined(String msg) {
		return error(ResultEnum.DEV_DEFINED.getCode(), msg);
	}
	
}
