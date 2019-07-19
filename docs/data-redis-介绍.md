## 介绍
　　data-redis库基于SpringRedis进行二次封装，更简单灵活，提供全局token与登录等特性：
- 简化使用并拥有Redis原生常用命令所对应的方法
- 保留SpringRedis所有常用特性：分布式缓存
- 提供分布式token、分布式锁
- 封装大量第三方登录特性，使登录更简单易于维护
- 封装常用的登录判断操作与redis-token解析

## 快速开始
### 引入模块
`yue-library-dependencies`作为父项目，在`pom.xml`文件中添加：
``` pom
	<dependencies>
		<dependency>
			<groupId>ai.ylyue</groupId>
			<artifactId>yue-library-data-redis</artifactId>
		</dependency>
	</dependencies>
```
### 配置数据源
`data-redis`就是SpringRedis的封装，<b>默认</b>数据源配置如下：
```yml
spring:
  redis:
    host: localhost
    port: 6379
```
### 简单使用
`data-redis`所有的CRUD方法都在`Redis`类里面，所以使用时只需要直接注入即可。<br>

简单的插入一条数据：
```java
@Repository
public class DataRedisExampleDAO {

	@Autowired
	Redis redis;// 直接注入即可
	
	/**
	 * 示例
	 */
	public void example() {
		String key = "key";
		String value = "value";
		String lockKey = "lockKey";
		long lockTimeout = 3600L;
		
		// 设置值
		redis.set(key, value);
		// 获得值
		redis.get(key);
		// 删除值
		redis.del(key);
		// 分布式锁-加锁
		redis.lock(lockKey, lockTimeout);
		// 分布式锁-解锁
		redis.unlock(lockKey, lockTimeout);
	}
	
}
```
更多方法请参阅API文档...