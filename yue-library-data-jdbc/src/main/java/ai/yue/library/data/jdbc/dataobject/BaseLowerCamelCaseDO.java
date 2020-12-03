package ai.yue.library.data.jdbc.dataobject;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 小驼峰命名法DO基类
 * 
 * <p><b><code style="color:red">注意：继承 {@link BaseLowerCamelCaseDO} get set ... 采用 &#064;{@link Data} 注解生成时，需加上：</code></b>
 * <blockquote>
 * 	<p>&#064;ToString(callSuper = true)
 * 	<p>&#064;EqualsAndHashCode(callSuper = true)
 * </blockquote>
 *
 * @deprecated 请使用：{@link BaseCamelCaseDO}
 * @author	ylyue
 * @since	2018年7月26日
 */
@Deprecated
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class BaseLowerCamelCaseDO implements Serializable {
	
	private static final long serialVersionUID = 2241197545628586478L;
	
	protected Long id;// 主键ID，单表时自增
	protected Integer sortIdx;// 排序索引
	/**
	 * 删除时间戳
	 * <p>默认值为0 == 未删除
	 * <p>一般不作查询展示
	 */
	protected Long deleteTime;
	protected LocalDateTime createTime;// 数据插入时间
	protected LocalDateTime updateTime;// 数据更新时间
	
}
