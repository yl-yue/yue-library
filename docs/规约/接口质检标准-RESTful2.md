# 接口质检标准
yue-library已提供 [RESTful](https://ylyue.cn/#/base/RESTful) 支持

## 为什么要有接口质检标准，接口质检标准是什么？
　　接口质检标准是检验API是否规范的一种标准，可帮助我们统一接口管理。一个比较好的实践方案，应该做到语义简洁明了，看到 URL 就知道是做什么的，就如同 RESTful 中所提到的那样。

## 接口文档规范
- <font color=red>（必须）</font>做到业务逻辑自测
- <font color=red>（必须）</font>提供标准的请求示例与响应示例
- <font color=red>（必须）</font>请求示例配备字段说明，并做好字段规则排序
- <font color=red>（重要）</font>接口字段命名与数据库保持一致
- <font color=red>（重要）</font>响应示例：复杂业务接口提供详细的字段注释
- （推荐）响应示例：日常业务接口提供简单注释

## 接口命名规范
　　URI命名风格采用增强 RESTful API 接口形式，接口命名需遵守API接口版本控制规则，并且以URL前缀风格做一定的安全认证区分。
- GET 查询接口（用于查询数据）
- POST 提交接口（用于插入数据、提交安全加密数据等）
- PUT 更新接口（用于更新数据，具有破坏性）
- DELETE 删除接口（用于删除数据，具有破坏性）

### API版本控制
API的版本号统一放入URL，如：`https://api.example.com/v{n}/`，n代表版本号，分为整形和浮点型
- **整形版本号**：大功能版本发布形式；具有当前版本状态下的所有API接口，例如：v1,v2
- **浮点型**：为小版本号，只具备补充api的功能，其他api都默认调用大版本号的API，例如v1.1,v1.2

### 接口命名前缀
|前缀	|作用										|示例							|
|--		|--											|--								|
|`open`	|互联网公开接口，无需登录						|`/open/v1.2/user/接口方法名`	|
|`auth`	|互联网认证接口，需要登录						|`/auth/v1.2/user/接口方法名`	|
|`lan`	|局域网内部接口，仅局域网内调用					|`/lan/v1.2/user/接口方法名`		|
|`self`	|局域网私有接口，仅局域网内私有领域服务调用		|`/self/v1.2/user/接口方法名`	|
|`sp`	|三方接口，由服务提供商（三方系统）提供实现		|`/sp/v1.2/user/接口方法名`		|

> **自身（私域）业务指：**<br>
> 一般在大型项目中（如：行业解决方案），业务众多繁杂且串联性较强，按微服务划分之后，此时可能会出现上百个独立微服务模块，为了方便管理维护，一般会对这些微服务根据业务特性进行分层与分类，
> 不同业务之间的微服务按照业务领域或产品特性等原则进行归类划分，甚至会划分出多个独立的产品，串联出一条业务线。<br>
> 而自身（私域）业务便是指代，这些独立产品之间的解耦区分，因此`self`接口只可被自身产品内部的微服务相互调用。

### 接口方法名规范
|HTTP方法	|接口动作类型		|接口方法名前缀	|接口动作解释										|示例																			|
|--			|--				|--				|--												|--																				|
|POST		|增				|`insert`		|新增、插入、添加									|`POST   /open/v1.2/user/insert`												|
|DELETE		|删				|`delete`		|物理删除、逻辑删除								|`DELETE /open/v1.2/user/delete`												|
|PUT		|改				|`update`		|修改、更新										|`PUT    /open/v1.2/user/updatePassword`										|
|GET		|查				|`get`			|单条（一个结果）									|`GET    /open/v1.2/user/getById`												|
|GET		|查				|`list`			|列表（多个结果）									|`GET    /open/v1.2/user/list`													|
|GET		|查				|`page`			|分页											|`GET    /open/v1.2/user/pageLikeUsername`										
|POST		|动作			|`act`			|登录、注册、上传、下载<br>重置、提交、搜索、支付	|`POST   /open/v1.2/user/actLogin`<br>`POST   /open/v1.2/user/actRegister`<br>`POST   /open/v1.2/user/actPasswordRest`<br>`POST   /open/v1.2/user/actSearch`|

### RESTful URL路径参数
**前端传参：**以apizza示例，apizza支持RESTful URL，如果在URL填写:key，然后在Query参数中填写对应 key，发送请求的时候插件将会替换URL中的 :key

![前端传参](接口质检标准_files/1.png)

**后端取参：**
```java
@PostMapping("/{opt}")
public Result<?> pathVariable(@PathVariable String opt) {
	return ResultInfo.success(opt);
}
```

## API接口规范
### 协议
- API与用户的通信协议，总是使用HTTPS协议，确保交互数据的传输安全。
- 应该尽量将API部署在专用域名之下

### 安全
为了保证接口接收到的数据不是被篡改以及防止信息泄露造成损失，对敏感数据进行加密及签名，并且以 [接口命名前缀](#接口命名前缀) 做一定的安全认证区分。

**数据加密**：api接口请求参数一律采用RSA进行加解密，在客户端使用公钥对请求参数进行加密，在服务端使用对数私钥据进行解密，防止信息泄露。<br>
**签名**：为了防止请求数据在网络传输过程中被恶意篡改，对所有非查询接口增加数字签名，签名原串为对请求参数进行自然排序，通过私钥加签后放入sign参数中。<br>
**时间戳**：api接口中增加时间戳timestamp字段，作用：固定时间范围内，减少同一请求被暴力调用的次数。

### 请求定义
**常见请求header约定**

|参数名称	|参数类型	|最大长度	|描述									|示例				|
|--			|--			|--			|--										|--					|
|`tenantId`	|String		|20			|租户ID，用于系统多租户区分				|taobao、tmall		|
|`clientId`	|String		|20			|客户端请求来源APP WAP PC				|APP				|
|`timestamp`|String		|17			|发送请求的时间,格式:yyyyMMddHHmmssSSS	|20180505121212222	|

**常见请求参数约定**

|参数名称		|参数类型	|最大长度	|描述							|示例									|
|--				|--			|--			|--								|--										|
|`page`			|Int		|5			|当前页							|1										|
|`limit`		|Int		|6			|每页显示条数					|20										|
|`signType`		|String		|10			|生成签名字符串所使用的算法类型	|RSA									|
|`sign`			|String		|344		|请求参数签名串					|djdu7dusufiusgfu						|
|`accessToken`	|String		|36			|访问令牌，UUID5				|65dbec7a-1df5-5413-bf41-9d4e41ee4ba7	|
|`sortFields`	|String		|50			|查询排序字段					|id,name,age							|
|`sortTypes`	|String		|50			|查询排序类型					|ASC,DESC,ASC							|

**常见参数约定**

|参数名称	|参数类型	|最大长度	|描述									|示例								|
|--			|--			|--			|--										|--									|
|`count`	|Long		|20			|分页统计条数							|100								|
|`tenantSys`|String		|36			|系统租户：一级租户（dict_tenant_sys）	|sc									|
|`tenantCo`	|String		|36			|企业租户：二级租户						|27b106951b964851b73e5d2864e9257b	|

### 响应定义
**响应体约定**

|参数名称	|参数类型	|最大长度	|描述													|示例																	|
|--			|--			|--			|--														|--																		|
|`code`		|Int		|3			|响应状态码（同步HTTP状态码）							|200																	|
|`msg`		|String		|30			|响应提示（除状态码600外，此msg皆表示给开发者的提示）	|成功																	|
|`flag`		|Boolean	|			|响应状态												|true																	|
|`traceId`	|String		|			|链路追踪码												|1cc00a1d8be14acc98457b23b8f5ab9f										|
|`data`		|Object		|			|业务数据												|【钉钉】通知结果：{\"errcode\":0,\"success\":true,\"errmsg\":\"ok\"}	|

msg提示约定：
- 除状态码600外，此msg皆表示服务端给客户端（即开发者）的请求提示
- 一般情况其它错误提示，如：500，服务器内部错误等，需前端结合各自业务情况统一拦截处理，转换为优化的用户提示，如：`网络开小差了，请稍后重试...`
- 优好的用户提示，甚至可到页面步骤级别，不同步骤错误基于不同的友好提示。

响应示例：
```json
{
    "code": 200,
    "msg": "成功",
    "flag": true,
    "traceId": "1cc00a1d8be14acc98457b23b8f5ab9f",
    "data": "【钉钉】通知结果：{\"errcode\":0,\"success\":true,\"errmsg\":\"ok\"}"
}
```

**code定义：**
```java
public enum ResultEnum {
	
	// 200 - 正确结果
	SUCCESS(200, "成功"),
	LOGGED_IN(210, "会话未注销，无需登录"),
	
    // 300 - 资源、重定向、定位等提示
	RESOURCE_ALREADY_INVALID(300, "资源已失效"),
	MOVED_PERMANENTLY(301, "Moved Permanently"),
	FILE_EMPTY(310, "文件上传请求错误，获得文件信息为空，求先使用测试工具（如：postman）校验，同时文件必须有明确的匹配类型（如文本类型：.txt）"),
    
    // 400 - 客户端错误
	BAD_REQUEST(400, "错误的请求，参数校验未通过，请参照API核对后重试"),
	UNAUTHORIZED(401, "未登录或登录已失效（Unauthorized）"),
	ATTACK(402, "非法访问"),
	FORBIDDEN(403, "无权限（Forbidden）"),
	NOT_FOUND(404, "Not Found"),
	METHOD_NOT_ALLOWED(405, "方法不允许（Method Not Allowed），客户端使用服务端不支持的 Http Request Method 进行接口调用。"),
	GONE(410, "当前API接口版本已弃用，请客户端更新接口调用方式"),
	UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
	TOO_MANY_REQUESTS(429, "频繁请求限流，请稍后重试（Too Many Requests）"),
	PARAM_VOID(432, "参数为空"),
	PARAM_CHECK_NOT_PASS(433, "参数校验未通过，请参照API核对后重试"),
	PARAM_VALUE_INVALID(434, "参数校验未通过，无效的value"),
	PARAM_DECRYPT_ERROR(435, "参数解密错误"),
	
    // 500 - 服务器错误
	INTERNAL_SERVER_ERROR(500, "服务器内部错误（Internal Server Error）"),
	REQUEST_ERROR(501, "请求错误"),
	BAD_GATEWAY(502, "Bad Gateway"),
	SERVICE_UNAVAILABLE(503, "Service Unavailable"),
	GATEWAY_TIMEOUT(504, "Gateway Timeout"),
	DATA_STRUCTURE(505, "数据结构异常"),
	DB_ERROR(506, "数据结构异常，请检查相应数据结构一致性"),
    CLIENT_FALLBACK(507, "网络开小差了，请稍后重试..."),
    CLIENT_FALLBACK_ERROR(508, "当前阶段服务压力过大，请稍后重试..."),
    TYPE_CONVERT_ERROR(509, "类型转换错误"),
    
	// 600 - 自定义错误提示
    ERROR_PROMPT(600, "错误提示，请使用具体的错误提示信息覆盖此msg");
	
}
```

### 非RESTful API需求
由于实际业务开展过程中，可能会出现各种的api不是简单的restful规范能实现的，因此需要一些api突破restful规范原则。

#### 页面级API
把当前页面中需要用到的所有数据通过一个接口一次性返回全部数据。

例子：`api/v1/get-home-data` 返回首页用到的所有数据，但此类API存在缺陷，只要业务需求变动，该api就需要跟着变更。

#### 自定义组合API
把当前用户需要在第一时间内容加载的多个接口合并成一个请求发送到服务端，服务端根据请求内容，一次性把所有数据合并返回，相比于页面级API，具备更高的灵活性，同时又能很容易实现页面级API功能。

地址:api/v1/testApi

传入参数：
```json
{
    "data": [
        {
            "url": "api1",
            "type": "get",
            "data": {}
        },
        {
            "url": "api2",
            "type": "get",
            "data": {}
        },
        {
            "url": "api3",
            "type": "get",
            "data": {}
        }
    ]
}
```

返回数据：
```json
{
  "code": 200,
  "flag": true,
  "msg": "成功",
  "data": [
      {"code": 200,"msg": "","data": []},
      {"code": 200,"msg": "","data": []},
      {"code": 200,"msg": "","data": []}
  ]
}
```

### 接口协作管理工具
使用 [apizza](https://apizza.net/pro/#/) 代替postman、传统API接口文档编写、接口流程测试、Mock服务