package ai.yue.library.base.view;

/**
 * @author	孙金川
 * @since	2017年10月8日
 */
class ResultUtils {
	
	/**
	 * 成功后调用
	 * @return
	 */
    static Result<Object> success() {
    	return Result.builder()
    	.code(ResultEnum.SUCCESS.getCode())
    	.msg(ResultEnum.SUCCESS.getMsg())
    	.flag(true)
    	.build();
    }
	
    static <T> Result<T> success(T data) {
        return new Result<T>().toBuilder()
        .code(ResultEnum.SUCCESS.getCode())
        .msg(ResultEnum.SUCCESS.getMsg())
        .flag(true)
        .data(data)
        .build();
    }
    
    static <T> Result<T> success(T data, Long count) {
        Result<T> result = new Result<T>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), true, data, count);
        return result;
    }
    
    static <T> Result<T> success(Integer code, T data, Long count) {
        Result<T> result = new Result<T>(code, ResultEnum.SUCCESS.getMsg(), true, data, count);
        return result;
    }
    
    /**
     * 失败后调用
     *
     * @param code 状态码
     * @param msg  提示消息
     * @return
     */
    static Result<?> error(Integer code, String msg) {
    	return Result.builder().code(code).msg(msg).flag(false).build();
    }
    
    /**
     * 失败后调用
     * @param <T>
     * @param code	状态码
     * @param msg	提示消息
     * @param data	异常数据
     * @return
     */
    static <T> Result<T> error(Integer code, String msg, T data) {
    	return new Result<T>().toBuilder().code(code).msg(msg).flag(false).data(data).build();
    }
    
}
