package ai.yue.library.base.view;

/**
 * String类型异常处理提示信息-常量类
 * 
 * @author	ylyue
 * @since	2018年7月19日
 */
public class ResultPrompt {
	
	//---------------------预定义结果提示---------------------
	
	public static final String USER_EXIST = "用户已存在";
	public static final String USER_NO_EXIST = "用户不存在";
	public static final String USER_STOP = "用户已停用";
	public static final String USER_INVALID = "无效的用户";
	public static final String USERNAME_OR_PASSWORD_ERROR = "用户名或密码错误";
	public static final String ORIGINAL_PASSWORD_ERROR = "原密码错误";
	public static final String PHONE_EXIST = "手机号已被注册";
	public static final String PASSWORD_INVALID = "无效的口令";
	public static final String CAPTCHA_ERROR = "验证码错误";
	public static final String INVITE_CODE_INVALID = "邀请码无效";
	public static final String ACCOUNT_EXIST_BINDING = "此帐户已被绑定";
	public static final String ACCOUNT_EXIST_BUSINESS_NOT_ALLOW_CHANGE = "帐户存在业务，暂时不允许改变状态";
	public static final String ACCOUNT_BALANCE_NOT_ENOUGH = "帐户余额不足";
    
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
	 * 数据结构异常-不正确的结果
	 * @param expected	预期内容
	 * @param actual	实际内容
	 * @return 提示信息
	 */
	public static String dataStructure(Object expected, Object actual) {
		return ResultEnum.DATA_STRUCTURE.getMsg() + " Incorrect result size: expected " + expected + ", actual " + actual;
	}
	
}
