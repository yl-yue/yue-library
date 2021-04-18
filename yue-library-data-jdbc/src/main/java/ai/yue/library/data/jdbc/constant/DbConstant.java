package ai.yue.library.data.jdbc.constant;

import ai.yue.library.data.jdbc.client.Db;

/**
 * {@linkplain Db} 常量类
 * 
 * @author	ylyue
 * @since	2018年7月18日
 */
public interface DbConstant {
	
	// ====================== 关键字段定义 ======================

	/** 关键字段定义-主键 */
	String PRIMARY_KEY = "id";
	/** 关键字段定义-数据删除标识 */
	String FIELD_DEFINITION_DELETE_TIME = "delete_time";
	/** 关键字段定义-排序 */
	String FIELD_DEFINITION_SORT_IDX = "sort_idx";

	// ====================== 关键字段定义-默认值定义 ======================

	/**
	 * 默认值定义-delete_time
	 * <p>删除时间戳默认值 0 == 未删除
	 */
	Long FIELD_DEFAULT_VALUE_DELETE_TIME = 0L;

	// ====================== 关键字段定义-命名占位符（具名参数） ======================

	/** 命名占位符-主键 **/
	String NAMED_PARAMETER_PRIMARY_KEY = ":id";
	/** 命名占位符-数据删除标识 */
	String NAMED_PARAMETER_DELETE_TIME = ":delete_time";
	/** 命名占位符-数据删除标识 */
	String NAMED_PARAMETER_SORT_IDX = ":sort_idx";

	// SQL
	
	/** 分页统计SQL前缀 */
	String PAGE_COUNT_SQL_PREFIX = "SELECT count(*) count ";
	
}
