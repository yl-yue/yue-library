package ai.yue.library.data.jdbc.dataobject;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * DO基类
 * 
 * <p><b><code style="color:red">注意：继承 {@link DBDO} get set ... 采用 &#064;{@link Data} 注解生成时，需加上：</code></b>
 * <blockquote>
 * 	<p>&#064;EqualsAndHashCode(callSuper = true)
 * 	<p>&#064;ToString(callSuper = true)
 * </blockquote>
 * 
 * @author	ylyue
 * @since	2018年7月26日
 */
@Data
public abstract class DBDO {
	
	protected Long id;// 表自增ID
	protected LocalDateTime create_time;// 数据插入时间
	protected LocalDateTime update_time;// 数据更新时间
	
}
