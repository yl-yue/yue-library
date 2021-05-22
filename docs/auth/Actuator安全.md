# Actuator安全
## Actuator安全引发介绍
当你使用百度搜索Actuator相关博客时，你会看到清一色的如下配置：
```yml
management:
 endpoints:
   web:
     exposure:
       include: "*"
 endpoint:
   health:
     show-details: always
```
这些博客中并未说明这样的配置会带来重大安全隐患，而大家都是从小白到入门的过程，几乎不会深究上述配置的问题，所以如果你真的在你的应用中添加了上面的配置，并且未加入`spring-boot-starter-security`做安全认证，
那么你的数据库连接信息包括你的用户名与密码等已在内网中裸奔，这个时候如果你的服务暴露给了公网，那么恭喜你`这些信息已在全网敞开`。

重大安全隐患路径（不同版本的SpringBoot端点路径不一样，下面是SpringBoot2.x的路径）：
- `actuator/env`       获取服务运行时的全部环境配置（包括你的数据库连接信息）
- `actuator/heapdump`  下载服务运行时的堆栈信息，可以根据`actuator/env`路径暴露的key，查找到服务内存中的配置信息，如：**密码**

!> [👉扩展阅读：SpringBoot组件安全之Actuator](https://www.istt.org.cn/NewsDetail/2422429.html)<br>
[👉扩展阅读：SpringBoot渗透之Actuator获取数据库密码](https://github.com/xx-zhang/SpringBootVul#%E4%B8%80%E4%BF%A1%E6%81%AF%E6%B3%84%E9%9C%B2)<br>
[👉扩展阅读：Springboot之actuator配置不当的漏洞利用](https://www.freebuf.com/news/193509.html)

## yue-library的处理
```java
@Bean
public SecurityFilterChain endpointRequestSecurityFilterChain(HttpSecurity http) throws Exception {
	http.requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests((requests) ->
			requests.anyRequest().hasRole("ENDPOINT_ADMIN"));
	http.httpBasic();
	return http.build();
}
```

```yml
spring:
  config:
    activate:
      on-profile: yue-library-auth-client
  security:
    user:
      roles: ENDPOINT_ADMIN
```