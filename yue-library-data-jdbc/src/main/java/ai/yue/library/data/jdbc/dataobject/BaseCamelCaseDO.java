package ai.yue.library.data.jdbc.dataobject;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <h2>驼峰命名法DO基类</h2><br>
 *
 * <b><code style="color:red">注意子类使用Lombok重写toString()与equals()和hashCode()方法时，callSuper属性需为true，如下：</code></b>
 * <blockquote>
 * 	<p>&#064;ToString(callSuper = true)
 * 	<p>&#064;EqualsAndHashCode(callSuper = true)
 * </blockquote><br>
 *
 * <b><code style="color:red">注意子类使用Lombok生成builder()方法时，需使用@SuperBuilder注解，而非@Builder注解，如下：</code></b>
 * <blockquote>
 * 	<p>&#064;NoArgsConstructor
 * 	<p>&#064;AllArgsConstructor
 * 	<p>&#064;SuperBuilder(toBuilder = true)
 * </blockquote><br>
 *
 * <a href="https://ylyue.cn/#/data/jdbc/DO基类">👉点击查看关于DO基类的详细使用介绍</a>
 *
 * @author	ylyue
 * @since	2018年7月26日
 */
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class BaseCamelCaseDO implements Serializable {
	
	private static final long serialVersionUID = 2241197545628586478L;

	/**
	 * 有序主键：单表时数据库自增、分布式时雪花自增
	 */
	protected Long id;

	/**
	 * 无序主键：uuid5无符号
	 */
	protected String uuid;

	/**
	 * 排序索引
	 */
	protected Integer sortIdx;

	/**
	 * 创建人：用户名、昵称、人名
	 */
	protected String createUser;

	/**
	 * 创建人：用户uuid
	 */
	protected String createUserUuid;

	/**
	 * 创建时间
	 */
	protected LocalDateTime createTime;

	/**
	 * 更新人：用户名、昵称、人名
	 */
	protected String updateUser;

	/**
	 * 更新人：用户uuid
	 */
	protected String updateUserUuid;

	/**
	 * 更新时间
	 */
	protected LocalDateTime updateTime;

	/**
	 * 删除人：用户名、昵称、人名
	 */
	protected String deleteUser;

	/**
	 * 删除人：用户uuid
	 */
	protected String deleteUserUuid;

	/**
	 * 删除时间戳：默认0（未删除）
	 * <p>一般不作查询展示
	 */
	protected Long deleteTime;

}
