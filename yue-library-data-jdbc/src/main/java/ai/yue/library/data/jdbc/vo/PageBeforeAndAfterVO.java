package ai.yue.library.data.jdbc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页上下条数据VO
 * 
 * @author	ylyue
 * @since	2018年10月10日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageBeforeAndAfterVO {

	/** 上一条数据ID */
	Long before_id;
	/** 下一条数据ID */
	Long after_id;

}
