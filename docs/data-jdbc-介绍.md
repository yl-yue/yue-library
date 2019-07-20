## 介绍
　　data-jdbc库基于SpringJDBC进行二次封装，拥有着强大性能的同时又不失简单、灵活。特性如下：
- 比SpringJDBC更方便好用、比SpringJPA更简单灵活
- **无侵入**：data-jdbc 在 SpringJDBC 的基础上进行扩展，只做增强不做改变，简化`CRUD`操作
- **依赖管理**：引入即可启动项目，关联druid实现SQL全监控
- **预防Sql注入**：内置Sql注入剥离器，有效预防Sql注入攻击
- **损耗小**：原生级CURD操作，性能基本无损耗，直接面向对象操作，同时还有大量经过SQL优化处理的CRUD方法
- **通用CRUD操作**：内置通用 DAO，通过继承方式即可实现单表大部分 CRUD 操作
- **更科学的分页**：分页参数自动解析，写分页等同于写基本List查询。更有优化型分页SQL检查
- **内置性能分析插件**：可输出Sql语句以及其执行时间，建议开发测试时启用该功能，能有效解决慢查询
- **类型强化**：支持原生级SQL查询，并强化原生查询结果，简单便捷 + 可维护组合（支持全JSON或全DO）
- **查询校验**：CRUD预期值判断
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
```yml
spring:
  datasource: 
    druid: 
      url: jdbc:mysql://localhost:3306/database?characterEncoding=utf-8&useSSL=false
      username: root
      password: 123456
```
### 简单使用
`data-jdbc`所有的CRUD方法都在`DB`类里面，所以使用时只需要直接注入即可，推荐采用继承`DBDAO 或 DBTDAO`方式。<br>
<font color=red>注意：sql数据表中主键的DDL最好同下面一样。</font>
```ddl
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '表自增ID'
```
主键ID：bigint类型、无符号、自动递增、不能为NULL
> 其实这样做也符合了“阿里巴巴Java开发手册”MySQL 数据库-建表规约第九条：
> 9. 【强制】表必备三字段：id, gmt_create, gmt_modified。
说明：其中 id 必为主键，类型为 bigint unsigned、单表时自增、步长为 1。gmt_create,
gmt_modified 的类型均为 datetime 类型，前者现在时表示主动创建，后者过去分词表示被动更新。

**DBDAO：**
```java
@Repository
public class DataJdbcExampleDAO extends DBDAO {

	@Override
	protected String tableName() {
		return "tableName";
	}
	
}
```

**DBTDAO：**
```java
@Repository
public class DataJdbcExampleTDAO extends DBTDAO<UserDO> {

	@Override
	protected String tableName() {
		return "user";
	}
	
}
```

## DAO
`DBDAO`为 JSON 对象提供服务

`DBTDAO`为 DO 对象提供服务（DO 转 JSON 工具类：ObjectUtils.toJSONObject(Object)）

DAO内置实现：
```java
// 插入数据
insert(JSONObject)
// 插入数据-批量
insertBatch(JSONObject[])
// 删除
delete(Long)
// 更新-ById
updateById(JSONObject)
// 单个
get(Long)
// 列表-全部
listAll()
// 分页
page(PageIPO)
// 分页-降序
pageDESC(PageIPO)
```