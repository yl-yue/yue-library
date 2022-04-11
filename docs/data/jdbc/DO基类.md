# DO基类介绍
在 [POJO规范](https://ylyue.cn/#/规约/后端规约说明?id=pojo) 中，DO（Data Object）为数据对象，一般情况下与数据库表结构一一对应，通过 DAO 层向上传输数据源对象。

## DO基础数据对象
DO基类将表中的必备字段进行了规范整理，用户可以根据不同的命名规范选择所需类进行继承：
- BaseCamelCaseDO：驼峰命名法DO基类
- BaseSnakeCaseDO：下划线命名法DO基类

BaseCamelCaseDO类源码速览（其它DO基类只是命名法不同）：
```java
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
```

## lombok使用
**`@SuperBuilder`与`@Builder`注解使用注意：**

`@Builder`注解不会构建父类属性，故**DO基类**默认已加上`@SuperBuilder`注解，子类需要使用建造者模式时，同样加上`@SuperBuilder(toBuilder = true)`注解即可，如下：
```java
@Data
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Subclass extends BaseCamelCaseDO {
```