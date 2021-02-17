package ai.yue.library.data.jdbc.dataobject;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <h2>下划线命名法DO基类</h2><br>
 *
 * <b><code style="color:red">注意子类使用Lombok重写toString()与equals()和hashCode()方法时，callSuper属性需为true，如下：</code></b>
 * <blockquote>
 * 	<p>&#064;ToString(callSuper = true)
 * 	<p>&#064;EqualsAndHashCode(callSuper = true)
 * </blockquote><br>
 *
 * <b><code style="color:red">注意子类使用Lombok生成builder()方法时，需使用@SuperBuilder注解，而非@Builder注解，如下：</code></b>
 * <blockquote>
 * 	<p>&#064;AllArgsConstructor
 * 	<p>&#064;SuperBuilder(toBuilder = true)
 * </blockquote><br>
 *
 * <a href="https://ylyue.cn/#/data/jdbc/介绍?id=do%e5%9f%ba%e7%b1%bb">👉点击查看关于DO基类的详细使用介绍</a>
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
