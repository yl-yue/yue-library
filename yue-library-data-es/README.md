## 介绍
　　data-es库简化es sql操作，提供默认配置项。支持更常见的安全配置化：
- 采用SQL实现
- 提供索引创建同步等方案

## 快速开始
### 引入模块
`yue-library-dependencies`作为父项目，在`pom.xml`文件中添加：
``` pom
<dependencies>
	<dependency>
		<groupId>ai.ylyue</groupId>
		<artifactId>yue-library-data-es</artifactId>
	</dependency>
</dependencies>
```
### 配置数据源
`data-es`<b>默认</b>数据源配置如下：
```yml
yue:
  es:
    rest:
      enabled: true
      host-and-port:
      - localhost:9200
      use-ssl: true
      username: admin
      password: admin
    sql:
      enabled: true
      url: https://localhost:9200
      username: admin
      password: admin
```
### 简单使用