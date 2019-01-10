package ai.yue.library.data.jdbc.constant;

import ai.yue.library.data.jdbc.client.DB;

/**
 * {@linkplain DB}常量类
 * @author  孙金川
 * @version 创建时间：2018年7月18日
 */
public interface DBConstant {
	
	// SQL
	
	/** 分页统计SQL前缀 */
	public static final String PAGE_COUNT_SQL_PREFIX = "SELECT count(*) count ";
	
}
