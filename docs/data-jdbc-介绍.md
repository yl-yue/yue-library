## 介绍
　　data-jdbc库基于SpringJDBC进行二次封装，拥有着强大性能的同时又不失简单、灵活。特性如下：
- 比SpringJDBC更方便好用、比SpringJPA更简单灵活
- 支持原生级SQL查询
- 大量经过SQL优化处理的CRUD方法
- 强化原生查询结果（支持：POJO、JSON）
- CRUD预期值判断
- 统一异常处理

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