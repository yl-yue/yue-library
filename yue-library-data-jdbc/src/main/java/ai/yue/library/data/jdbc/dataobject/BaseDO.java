package ai.yue.library.data.jdbc.dataobject;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * DO基类
 * @author  孙金川
 * @version 创建时间：2018年7月26日
 */
@Data
public abstract class BaseDO {
	
	protected Long id;// 表自增ID
	protected LocalDateTime create_time;// 数据插入时间
	protected LocalDateTime update_time;// 数据更新时间
	
}
