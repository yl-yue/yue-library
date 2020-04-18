## 介绍
　　auth-service库基于SpringSecurity进行二次封装，更简单灵活，提供全局token与登录等特性：
- 提供分布式token
- 封装大量第三方登录特性，使登录更简单易于维护
- 封装常用的登录判断操作与redis-token解析

## 快速开始
### 引入模块
`yue-library-dependencies`作为父项目，在`pom.xml`文件中添加：
``` pom
<dependencies>
	<dependency>
		<groupId>ai.ylyue</groupId>
		<artifactId>yue-library-auth-service</artifactId>
	</dependency>
</dependencies>
```