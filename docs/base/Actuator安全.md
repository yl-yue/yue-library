## Actuator 安全 <!-- {docsify-ignore} -->
> 安全规范详见开发文档：《SpringBoot 安全-应用监控 Actuator 安全隐患（严重）》

1. `actuator` 端点改为 `actuator-security` 端点
2. 健康检查地址改为：`actuator-security/health`
3. 使用 **spring-boot-starter-security** 做 **httpBasic** 安全认证
    - `spring.security.user.name`
    - `spring.security.user.password`
    - `spring.security.user.roles`
4. 未认证只可获取最小化服务健康信息
