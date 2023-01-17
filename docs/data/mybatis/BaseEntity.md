# BaseEntity
在 [POJO规范](https://ylyue.cn/#/规约/后端规约说明?id=pojo) 中，`Entity` 为数据对象，一般情况下与数据库表结构一一对应，通过 `Mapper` 层向上传输数据源对象。

## 使用示例
BaseEntity将表中的必备字段进行了规范整理，选择继承即可遵守yue-library数据库规约。

```java
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TableExample extends BaseEntity {

	private static final long serialVersionUID = 6404495051119680239L;

	@TableField(fill = FieldFill.INSERT)
	String tenantSys;
	String tenantCo;
	String fieldOne;
	String fieldTwo;
	String fieldThree;

}
```

## lombok使用
**`@SuperBuilder`与`@Builder`注解使用注意：**

`@Builder`注解不会构建父类属性，故**BaseEntity**默认已加上`@SuperBuilder`注解，子类需要使用建造者模式时，同样加上`@SuperBuilder(toBuilder = true)`注解即可，如下：
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Subclass extends BaseEntity {
```

## BaseEntity源码速览
```java
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 2241197545628586478L;

	/**
	 * 有序主键：单表时数据库自增、分布式时雪花自增
	 */
	@TableId
	protected Long id;

	/**
	 * 排序索引
	 */
	protected Integer sortIdx;

	/**
	 * 创建人：用户名、昵称、人名
	 */
	@TableField(fill = FieldFill.INSERT)
	protected String createUser;

	/**
	 * 创建人：用户id
	 */
	@TableField(fill = FieldFill.INSERT)
	protected String createUserId;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	protected LocalDateTime createTime;

	/**
	 * 更新人：用户名、昵称、人名
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	protected String updateUser;

	/**
	 * 更新人：用户id
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	protected String updateUserId;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	protected LocalDateTime updateTime;

	/**
	 * 删除时间：默认0（未删除）
	 * <p>一般不作查询展示
	 */
	@TableLogic(delval = "now()")
	protected Long deleteTime;

}
```
