package ai.yue.library.data.jdbc.constant;

import ai.yue.library.data.jdbc.client.Db;

/**
 * {@linkplain Db} 常量类
 * 
 * @author	ylyue
 * @since	2018年7月18日
 */
public interface DBConstant {
	
	// 字段
	
	/**
	 * 主键
	 */
	String PRIMARY_KEY = "id";
	
	// SQL
	
	/** 分页统计SQL前缀 */
	String PAGE_COUNT_SQL_PREFIX = "SELECT count(*) count ";
	
}
