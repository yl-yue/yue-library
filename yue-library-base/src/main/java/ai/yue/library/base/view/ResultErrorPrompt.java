package ai.yue.library.base.view;

/**
 * String类型异常处理提示信息-常量类
 * 
 * @author	孙金川
 * @since	2018年7月19日
 */
public class ResultErrorPrompt {
	
	//---------------------直接定义异常---------------------
	
	/** 超出最大limit限制 */
	public static final String MAX_LIMIT = "超出最大limit限制";
	
	/** 分布式锁 */
	public static final String DISTRIBUTED_LOCK = "分布式锁...";
	
	/** 多行插入错误 */
	public static final String INSERT_BATCH_ERROR = "执行多行插入命令失败，可能原因是：数据结构异常或无ID主键。请立即检查数据的一致性、唯一性。";
	
	/** 单行删除错误 */
	public static final String DELETE_ERROR = "执行单行删除命令失败，可能原因是：数据结构异常或无ID主键。请立即检查数据的一致性、唯一性。";
	
	/** 批次删除错误 */
	public static final String DELETE_BATCH_ERROR = "执行批次删除命令失败，可能原因是：数据结构异常或无ID主键。请立即检查数据的一致性、唯一性。";
	
	/** 批次更新错误 */
	public static final String UPDATE_BATCH_ERROR = "执行批次更新命令失败，可能原因是：数据结构异常或无ID主键。请立即检查数据的一致性、唯一性。";
	
	//---------------------引用定义异常---------------------
	
	/**
	 * 非法访问异常提示
	 */
	public static final String ATTACK = "code: " + ResultEnum.ATTACK.getCode() + ", message: " + ResultEnum.ATTACK.getMsg();
	
	/**
	 * 参数为空异常提示
	 */
	public static final String PARAM_VOID = "code: " + ResultEnum.PARAM_VOID.getCode() + ", message: " + ResultEnum.PARAM_VOID.getMsg();
	
	/**
	 * 参数校验未通过异常提示
	 */
	public static final String PARAM_CHECK_NOT_PASS = "code: " + ResultEnum.PARAM_CHECK_NOT_PASS.getCode() + ", message: " + ResultEnum.PARAM_CHECK_NOT_PASS.getMsg();
	
	/**
	 * 服务降级异常提示
	 */
	public static final String CLIENT_FALLBACK = "code: " + ResultEnum.CLIENT_FALLBACK.getCode() + ", message: " + ResultEnum.CLIENT_FALLBACK.getMsg();
	
	/**
	 * 数据结构异常提示
	 */
	public static final String DB_ERROR = "code: " + ResultEnum.DB_ERROR.getCode() + ", message: " + ResultEnum.DB_ERROR.getMsg();
	
	/**
	 * 未登录或登录已失效异常提示
	 */
	public static final String UNAUTHORIZED = "code: " + ResultEnum.UNAUTHORIZED.getCode() + ", message: " + ResultEnum.UNAUTHORIZED.getMsg();
	
	/**
	 * 无权限异常提示
	 */
	public static final String FORBIDDEN = "code: " + ResultEnum.FORBIDDEN.getCode() + ", message: " + ResultEnum.FORBIDDEN.getMsg();
	
	/**
	 * 数据结构异常-不正确的结果
	 * @param expected	预期内容
	 * @param actual	实际内容
	 * @return 提示信息
	 */
	public static String dataStructure(Object expected, Object actual) {
		return ResultEnum.DATA_STRUCTURE.getMsg() + " Incorrect result size: expected " + expected + ", actual " + actual;
	}
	
}
