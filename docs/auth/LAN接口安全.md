# LAN接口安全

## 问题背景

`/lan/` 前缀接口定义为"局域网内部接口，仅局域网内调用"，但框架层之前没有提供任何安全拦截实现。业务项目被迫自行拼装安全拦截逻辑，极易踩坑——例如使用 `SaServletFilter + SaHttpBasicUtil.check()` 时未配置凭据，导致所有 `/lan/` 请求返回通用 401，排查者无法从错误响应中区分"配置缺失"和"凭据错误"。

yue-library 现已为 `/lan/` 路径提供**开箱即用的统一安全拦截方案**，业务项目无需自行实现，跟随框架规范即可。

## 快速开始

### 1. 引入依赖

只需引入 `yue-library-web`，无需额外依赖：

```xml
<dependency>
    <groupId>ai.ylyue</groupId>
    <artifactId>yue-library-web</artifactId>
</dependency>
```

### 2. 默认行为（无需任何配置）

默认启用 IP 白名单策略：内网 IP 自动放行，公网 IP 返回 403 + 明确诊断信息。

内网 IP 段默认放行：
- `10.0.0.0/8`
- `172.16.0.0/12`
- `192.168.0.0/16`
- `127.0.0.1`
- `0:0:0:0:0:0:0:1`（IPv6 本地）

## 安全策略

### 默认：IP 白名单

内网 IP 放行，公网 IP 拒绝。适用于大多数内网接口场景。

```yml
yue:
  web:
    lan-security:
      enabled: true
      ip-whitelist-extra:          # 可选：额外 IP 白名单（CIDR 格式）
        - 10.0.0.0/8
        - 203.0.113.0/24
```

### 可选：为公网 IP 开启 Basic Auth

内网 IP 放行 + 公网 IP 需 HTTP Basic Auth 认证。兼顾便捷与安全。

```yml
yue:
  web:
    lan-security:
      enabled: true
      basic-auth:
        enabled: true              # 为公网 IP 开启 Basic Auth
        username: admin
        password: your-secure-password
```

> 未配置密码时，框架自动生成随机密码，**启动时在日志中打印**。请及时从日志中获取并妥善保管。

## 配置项

| 配置项 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `yue.web.lan-security.enabled` | Boolean | true | 是否启用 /lan/ 路径安全拦截 |
| `yue.web.lan-security.ip-whitelist-extra` | List | [] | 额外 IP 白名单（CIDR 格式），内网 IP 段默认放行 |
| `yue.web.lan-security.basic-auth.enabled` | Boolean | false | 是否为公网 IP 开启 Basic Auth 认证 |
| `yue.web.lan-security.basic-auth.username` | String | admin | Basic Auth 用户名 |
| `yue.web.lan-security.basic-auth.password` | String | 随机 | Basic Auth 密码（未配置时随机生成，启动时日志打印） |

## 关闭安全拦截

如需关闭（不推荐）：

```yml
yue:
  web:
    lan-security:
      enabled: false
```

## 异常诊断

框架对 `/lan/` 路径的异常做了分类处理，不再返回通用错误码：

| 场景 | HTTP 状态码 | 响应体示例 | 日志 |
|------|------------|-----------|------|
| 公网 IP + Basic Auth 未开启 | 403 | `{"code":403,"msg":"请求 /lan/v1/xxx 路径来自公网IP 203.0.113.1，被安全策略拒绝"}` | WARN: 【/lan/安全拦截】IP拒绝 |
| 公网 IP + 未提供 Basic Auth 凭据 | 401 | `{"code":401,"msg":"请求 /lan/v1/xxx 路径需要 Basic Auth 认证"}` + `WWW-Authenticate` 头 | WARN: 【/lan/安全拦截】认证失败 |
| 公网 IP + Basic Auth 凭据错误 | 401 | `{"code":401,"msg":"请求 /lan/v1/xxx 路径需要 Basic Auth 认证"}` | WARN: 【/lan/安全拦截】认证失败 |

## 实现原理

本方案使用纯 Servlet Filter（`OncePerRequestFilter`）实现，**不依赖 sa-token**，也不依赖 Spring Security 的认证授权体系。

- `LanSecurityFilter`：匹配 `/lan/**` 请求，执行 IP 白名单检查 + 可选 Basic Auth 认证
- `LanSecurityAutoConfiguration`：注册 Filter Bean，处理密码随机生成逻辑
- `LanSecurityProperties`：配置属性类

业务项目不再需要自行拼装 `SaServletFilter + SaHttpBasicUtil.check()` 来保护 `/lan/` 路径。如果业务项目使用 sa-token 做其他路径的认证（如 `/auth/`），与本方案不冲突。
