package ai.yue.library.data.jdbc.constant;

import ai.yue.library.data.jdbc.client.Db;

/**
 * {@linkplain Db} 常量类
 * 
 * @author	ylyue
 * @since	2018年7月18日
 */
public class DbConstant {
	
	// ====================== 关键字段定义 ======================

	/** 关键字段定义-有序主键 */
	public static final String FIELD_DEFINITION_PRIMARY_KEY = "id";

	// ====================== 关键字段定义-默认值定义 ======================

	/**
	 * 默认值定义-delete_time
	 * <p>删除时间戳默认值 0 == 未删除
	 */
	public static final Long FIELD_DEFAULT_VALUE_DELETE_TIME = 0L;

	// ====================== 关键字段定义-命名占位符（具名参数） ======================

	/** 命名占位符-有序主键 **/
	public static final String NAMED_PARAMETER_PRIMARY_KEY = ":id";

	// SQL
	
	/** 分页统计SQL前缀 */
	public static final String PAGE_COUNT_SQL_PREFIX = "SELECT count(*) count ";

	// 方法

	/**
	 * 获得具名参数（命名占位符）
	 *
	 * @param fieldName 字段名
	 * @return 具名参数
	 */
	public static String getNamedParameter(String fieldName) {
		return ":" + fieldName;
	}

}
