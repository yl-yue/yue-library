## 介绍
　　auth-client为auth-service客户端模块，提供获取当前登录用户状态信息等特性：
- 提供分布式token获取
- 向认证服务获取登录状态并解析redis-token

## 快速开始
### 引入模块
`yue-library-dependencies`作为父项目，在`pom.xml`文件中添加：
``` pom
<dependencies>
	<dependency>
		<groupId>ai.ylyue</groupId>
		<artifactId>yue-library-auth-client</artifactId>
	</dependency>
</dependencies>
```