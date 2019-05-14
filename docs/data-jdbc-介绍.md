## 介绍
　　data-jdbc库基于SpringJDBC进行二次封装，拥有着强大性能的同时又不失简单、灵活。特性如下：
- 比SpringJDBC更方便好用、比SpringJPA更简单灵活
- Mybatis 增强工具包 - 只做增强不做改变，简化`CRUD`操作
- 支持原生级SQL查询
- 大量经过SQL优化处理的CRUD方法
- 强化原生查询结果（支持：POJO、JSON）
- CRUD预期值判断
- 统一异常处理
- **内置分页插件**：基于Mybatis物理分页，开发者无需关心具体操作，配置好插件之后，写分页等同于写基本List查询
- **内置性能分析插件**：可输出Sql语句以及其执行时间，建议开发测试时启用该功能，能有效解决慢查询
- **内置全局拦截插件**：提供全表 delete 、 update 操作智能分析阻断，预防误操作
- **预防Sql注入**：内置Sql注入剥离器，有效预防Sql注入攻击
- **无侵入**：Mybatis-Plus 在 Mybatis 的基础上进行扩展，只做增强不做改变，引入 Mybatis-Plus 不会对您现有的 Mybatis 构架产生任何影响，而且 MP 支持所有 Mybatis 原生的特性
- **依赖少**：仅仅依赖 Mybatis 以及 Mybatis-Spring
- **损耗小**：启动即会自动注入基本CURD，性能基本无损耗，直接面向对象操作
- **通用CRUD操作**：内置通用 Mapper、通用 Service，仅仅通过少量配置即可实现单表大部分 CRUD 操作，更有强大的条件构造器，满足各类使用需求
- **支持ActiveRecord**：支持 ActiveRecord 形式调用，实体类只需继承 Model 类即可实现基本 CRUD 操作

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
```yml
spring:
  datasource: 
    druid: 
      url: jdbc:mysql://localhost:3306/database?characterEncoding=utf-8&useSSL=false
      username: root
      password: 123456
```
### 简单使用
`data-jdbc`所有的CRUD方法都在`DB`类里面，所以使用时只需要直接注入即可。<br>
<font color=red>注意：sql数据表中主键的DDL最好同下面一样。</font>
```ddl
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '表自增ID'
```
主键ID：bigint类型、无符号、自动递增、不能为NULL
> 其实这样做也符合了“阿里巴巴Java开发手册”MySQL 数据库-建表规约第九条：
> 9. 【强制】表必备三字段：id, gmt_create, gmt_modified。
说明：其中 id 必为主键，类型为 bigint unsigned、单表时自增、步长为 1。gmt_create,
gmt_modified 的类型均为 datetime 类型，前者现在时表示主动创建，后者过去分词表示被动更新。

简单的插入一条数据：
```java
@Repository
public class DataJdbcExampleDAO {

	@Autowired
	DB db;
	
	/**
	 * 插入数据
	 * @param paramJson
	 * @return
	 */
	public Long insert(JSONObject paramJson) {
		return db.insert("tableName", paramJson);
	}
	
}
```