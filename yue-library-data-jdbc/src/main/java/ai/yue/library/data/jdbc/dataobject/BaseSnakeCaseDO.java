package ai.yue.library.data.jdbc.dataobject;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 下划线命名法DO基类
 * 
 * <p><b><code style="color:red">注意：继承 {@link BaseSnakeCaseDO} get set ... 采用 &#064;{@link Data} 注解生成时，需加上：</code></b>
 * <blockquote>
 * 	<p>&#064;ToString(callSuper = true)
 * 	<p>&#064;EqualsAndHashCode(callSuper = true)
 * </blockquote>
 * 
 * @author	ylyue
 * @since	2018年7月26日
 */
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class BaseSnakeCaseDO implements Serializable {
	
	private static final long serialVersionUID = 3601450189220851200L;

	/** 表自增ID */
	protected Long id;
	/** 排序索引 */
	protected Integer sort_idx;
	/**
	 * 删除时间戳
	 * <p>默认值为0 == 未删除
	 * <p>一般不作查询展示
	 */
	protected Long delete_time;
	/** 数据插入时间 */
	protected LocalDateTime create_time;
	/** 数据更新时间 */
	protected LocalDateTime update_time;

}
