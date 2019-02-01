package ai.yue.library.base.util;

import cn.hutool.core.util.PageUtil;

/**
 * @author  孙金川
 * @version 创建时间：2019年2月1日
 */
public class PageUtils extends PageUtil {
	
	/**
	 * 分页实际总数
	 * <p>根据索引总数同当前查询结果实际数据大小，计算实际分页总数
	 * <p>应用场景：数据库实际数据与搜索引擎保存的索引不同步时使用
	 * @param total 索引总数
	 * @param size 当前查询结果实际数据大小
	 * @param page 第几页
	 * @param limit 每页限制条数
	 * @return
	 */
	public static long pageActualCount(long total, long size, long page, long limit) {
		long count = 0;
		if (size == limit) {
			count = total;
		} else {
			count = (page - 1) * limit + size;
		}
		
		return count;
	}
	
}
