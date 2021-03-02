## 介绍
　　data-jdbc库基于SpringJDBC进行二次封装，拥有着强大性能的同时又不失简单、灵活。特性如下：
- 比SpringJDBC更方便好用、比SpringJPA更简单灵活
- **无侵入**：data-jdbc 在 SpringJDBC 的基础上进行扩展，只做增强不做改变，简化`CRUD`操作
- **依赖管理**：引入即可启动项目，关联druid实现SQL全监控
- **预防Sql注入**：内置Sql注入剥离器，有效预防Sql注入攻击
- **损耗小**：封装大量经过SQL优化处理的CRUD方法，直接面向对象操作，对比原生级CRUD处理，性能基本无损耗甚至更优
- **通用CRUD操作**：内置通用 DAO，通过继承方式即可实现单表大部分 CRUD 操作
- **更科学的分页**：分页参数自动解析，写分页等同于写基本List查询。更有优化型分页SQL检查
- **内置性能分析插件**：可输出Sql语句以及其执行时间，建议开发测试时启用该功能，能有效解决慢查询
- **类型强化**：支持原生级SQL查询，并强化原生查询结果，简单便捷 + 可维护组合（支持全JSON或全DO）
- **CRUD校验**：CRUD操作是否符合预期，更好的避免脏数据的产生与违规操作
- **全局异常处理**：CRUD操作相关异常统一处理，定位更精准，提示更友好，实现全局Restful风格

## 快速开始
### 引入模块
`yue-library-dependencies`作为父项目，在`pom.xml`文件中添加：
``` pom
<dependencies>
	<dependency>
		<groupId>ai.ylyue</groupId>
		<artifactId>yue-library-data-jdbc</artifactId>
	</dependency>
</dependencies>
```

### 配置数据源
`data-jdbc`就是SpringJDBC的封装，数据源配置如下：
```yaml
spring:
  datasource: 
    druid: 
      url: jdbc:mysql://localhost:3306/database?characterEncoding=utf-8&useSSL=false
      username: root
      password: 123456
```

### 简单使用
`data-jdbc`所有的CRUD方法都在`Db`类里面，所以使用时只需要直接注入即可，推荐采用继承`AbstractDAO 或 AbstractRepository`方式。<br>
<font color=red>注意：sql数据表中主键的DDL最好同下面一样。</font>
```ddl
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '表自增ID'
```
主键ID：bigint类型、无符号、自动递增、不能为NULL
> 其实这样做也符合了《Java开发手册》MySQL数据库-建表规约第九条：<br>
> ![建表规约第九条](https://ylyue.cn/data/jdbc/%E4%BB%8B%E7%BB%8D_files/%E5%BB%BA%E8%A1%A8%E8%A7%84%E7%BA%A6%E7%AC%AC%E4%B9%9D%E6%9D%A1.png)

**AbstractDAO：**
```java
@Repository
public class DataJdbcExampleDAO extends AbstractDAO {

	@Override
	protected String tableName() {
		return "table_example";
	}
	
}
```

**AbstractRepository：**
```java
@Repository
public class DataJdbcExampleRepositoryDAO extends AbstractRepository<UserDO> {

	@Override
	protected String tableName() {
		return "user";
	}
	
}
```

### <font color=red>DAO内置实现：</font>
`AbstractDAO`为 JSON 对象提供服务

`AbstractRepository`为 DO 对象提供服务，字段映射支持下划线与驼峰自动识别转换

实际中可能会遇到类型转换问题，可使用 `Convert` 类进行转换，支持DO、Json、List等相互转换

```java
// 插入数据
public Long insert(JSONObject paramJson) {
// 插入数据-实体
public Long insert(Object paramIPO) {
// 插入数据-实体
public Long insert(Object paramIPO, FieldNamingStrategyEnum fieldNamingStrategyEnum) {
// 插入数据-批量
public void insertBatch(JSONObject[] paramJsons) {
// 删除
public void delete(Long id) {
// 删除-通过表业务键
public void deleteByBusinessUk(String businessUkValue) {
// 删除-逻辑的
public void deleteLogicByBusinessUk(String businessUkValue) {
// 更新-ById
public void updateById(JSONObject paramJson) {
// 更新-By业务键
public void updateByBusinessUk(JSONObject paramJson) {
// 单个-通过表ID查询
public T get(Long id) {
// 单个-通过表业务键查询
public T getByBusinessUk(String businessUkValue) {
// 列表-全部
public List<T> listAll() {
// 分页
public PageTVO<T> page(PageIPO pageIPO) {
// 分页-降序
public PageTVO<T> pageDESC(PageIPO pageIPO) {
```

## 其他
### DO基类
[POJO规范](https://ylyue.cn/#/规约/后端规约说明?id=pojo) 中 DO（Data Object）为数据对象，一般情况下与数据库表结构一一对应，通过 DAO 层向上传输数据源对象。<br>
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

	/** 主键ID，单表时自增 */
	protected Long id;
	/** 排序索引 */
	protected Integer sortIdx;
	/**
	 * 删除时间戳
	 * <p>默认值为0 == 未删除
	 * <p>一般不作查询展示
	 */
	protected Long deleteTime;
	/** 数据插入时间 */
	protected LocalDateTime createTime;
	/** 数据更新时间 */
	protected LocalDateTime updateTime;
	
}
```

**`@SuperBuilder`与`@Builder`使用注意：**

`@Builder`注解不会构建父类属性，故**DO基类**默认已加上`@SuperBuilder`注解，子类需要使用建造者模式时，同样加上`@SuperBuilder(toBuilder = true)`注解即可，如下：
```java
@Data
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Subclass extends BaseCamelCaseDO {
```

### 绝对条件查询参数whereSql化
```java
paramToWhereSql(JSONObject)
```

### 预期值判断
```java
// ------ 预期值判断 ------
// 更新所影响的行数
int updateRowsNumber = 1;
// 预期值
int expectedValue = 1;

// 有返回值-布尔
// 判断更新所影响的行数是否 <b>等于</b> 预期值
db.isUpdateAndExpectedEqual(updateRowsNumber, expectedValue);
// 判断更新所影响的行数是否 <b>大于等于</b> 预期值
db.isUpdateAndExpectedGreaterThanEqual(updateRowsNumber, expectedValue);

// 无返回值-DBException异常
// 判断更新所影响的行数是否 <b>等于</b> 预期值
db.updateAndExpectedEqual(updateRowsNumber, expectedValue);
// 判断更新所影响的行数是否 <b>大于等于</b> 预期值
db.updateAndExpectedGreaterThanEqual(updateRowsNumber, expectedValue);
// 确认批量更新每组参数所影响的行数，是否 <b>全部都等于</b> 同一个预期值
int[] updateRowsNumberArray = {1, 1};
db.updateBatchAndExpectedEqual(updateRowsNumberArray, expectedValue);
```

### 查询结果转换
由于原生`SpringJDBC`查询单条数据，若为空将抛出异常。所以一般会采用查询多条数据方式，然后再转换成单条数据对象：
```java
listResultToGetResult(List<T> list)
```