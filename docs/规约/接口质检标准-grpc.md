# 接口质检标准
yue-library正计划提供 [grpc]() 支持

## 为什么要有接口质检标准，接口质检标准是什么？
　　接口质检标准是检验API是否规范的一种标准，可帮助我们统一接口管理。一个比较好的实践方案，应该做到语义简洁明了，看到 URL 就知道是做什么的。

## 接口文档规范
- <font color=red>（必须）</font>做到业务逻辑自测
- <font color=red>（必须）</font>提供标准的请求示例与响应示例
- <font color=red>（必须）</font>请求示例配备字段说明，并做好字段规则排序
- <font color=red>（重要）</font>接口字段命名与数据库保持一致
- <font color=red>（重要）</font>响应示例：复杂业务接口提供详细的字段注释
- （推荐）响应示例：日常业务接口提供简单注释

## proto规约
遵循 [谷歌 Protobuf Style Guide](https://developers.google.com/protocol-buffers/docs/style)，参考 [Google Cloud API 设计指南](https://cloud.google.com/apis/design)，并对以下几点规约进行强调与更改：
- `proto`文件名与`service`类名保持一直，一个proto一个service
- `package`包名全小写与工程目录结构保持一致
- `service`类名遵循大驼峰命名法，命名时不用带无意义的`Service`后缀
- `rpc`方法名遵循大驼峰命名法，前缀遵循rpc方法命名前缀规约，后缀可带数字表示版本号
- `message`消息体名遵循大驼峰命名法，请求体定义使用`Request`后缀，响应体定义使用`Response`后缀，非rpc方法直接引用的消息体可不带后缀
- `message`字段名遵循下划线命名法，对重复字段使用复数名称
- `enum`枚举名遵循大驼峰命名法
- `enum`字段名遵循大写字母下划线命名法，相同规则用前缀表示

### service类名规范
|service类名前缀|作用										|示例		|
|--				|--											|--			|
|`Open`			|公开接口，无需登录							|`OpenUser`	|
|`Auth`			|认证接口，需要登录							|`AuthUser`	|
|`Self`			|自有接口，仅自身内部服务可调用				|`SelfUser`	|
|`Sp`			|三方接口，由服务提供商（三方系统）提供实现	|`SpUser`	|

### rpc方法名规范
|rpc动作类型|rpc方法名前缀	|rpc动作解释										|示例（<font color=red>后缀数字为版本号</font>）							|
|--			|--				|--													|--																			|
|增			|`Insert`		|新增、插入、添加									|`InsertUser1`																|
|删			|`Del`			|物理删除、逻辑删除									|`DelUser`																	|
|改			|`Update`		|修改、更新											|`UpdateUserPassword3`														|
|查			|`Get`			|单条（一个结果）									|`GetUserById`																|
|查			|`List`			|列表（多个结果）									|`ListUser33`																|
|查			|`Page`			|分页												|`PageUserLikeUsername`														|
|动作		|`Act`			|登录、注册、上传、下载<br>重置、提交、搜索、支付	|`ActUserLogin`、`ActUserRegister`<br>`ActUserPasswordRest`、`ActUserSearch`|

### 工程结构规范
|工程模块		|前端（局域网外）调用	|后端（局域网内）调用									|工具模拟访问										|工程公开性				|
|--				|--						|--														|--													|--						|
|前端`internet`	|自身前端业务可调用		|后端不可调用											|工具可模拟访问（不锁定访问者IP）					|只对前端可见			|
|后端`lan`		|前端不可调用			|其他后端业务调用，自身不可调							|局域网内工具可模拟访问（确认访问者IP）				|只对后端可见			|
|自身`self`		|前端不可调用			|只可被自身内部服务调用（一个内部业务被拆分多个微服务）	|局域网内授权IP使用工具可模拟访问（锁定访问者IP）	|只对自身内部服务可见	|

### 工程依赖规约
```
. proto-common              中台proto工程-共享依赖
├── common-csm              中台proto工程-共享依赖-通用服务模块
├── common-mdp              中台proto工程-共享依赖-微服务开发平台
└── common-ssc              中台proto工程-共享依赖-共享服务中心
```

- 在`proto-common`工程组下定义公共message，并编译为独立模块（同谷歌`google/protobuf/wrappers.proto`编译为独立模块引用）
- 工程模块为`common-ssc`，工程路径为`sc/proto-common/common-ssc/msg`

```protobuf
package sc.proto.common.ssc.msg;

message CommonMessageRquest {
  string name = 1;
}
```

- 业务依赖方将`common-ssc`独立模块，引入至依赖工程，proto文件采用相对路径引用，故引用路径为`common-ssc/msg`

```protobuf
import "google/protobuf/wrappers.proto";
import "common-ssc/msg/CommonMessage.proto";

service AuthCommonMessage {
  rpc ActRegister(sc.proto.common.ssc.msg.CommonMessageRquest) returns (google.protobuf.BoolValue);
}
```

!> 所有工程都应该遵守自身的程序解耦规约与上下通信原则，在`common-*`子工程之间不应做相互依赖，因为`common`工程主要是为`internet、lan、self`工程提供公共服务，所以在定义自身模块的`message`时应采用冗余定义的方式。

!> `proto-common`工程组中的`common-csm`与`common-message`模块才属于真正的公共模块，所有`common-*`子工程皆可依赖，但同样需要遵守自身的程序解耦规约与上下通信原则。

### 公共message规约
- proto中允许定义公共message，但必须向后兼容（提供版本），并且程序中必须提供POJO转换工具（可实现随意拉平或嵌套映射）
- 公共message应定义在`common-csm`与`common-message`工程中，需要注意的是common-message为业务前台定义的公共message模块，所以在中台中是不可见的
- 使用公共message嵌套后，就不应该在外层定义相同名称的字段，因为我们需要实现message的随意拉平与嵌套映射

### proto规范示例
**OpenUser：**`sc/proto/proto-lan/lan-ssc/ssc-md/user/OpenUser.proto`
```protobuf
syntax = "proto3";

package sc.proto.lan.ssc.md.user;

service OpenUser {
  rpc ActUserLogin (Request) returns (Response) {}
  rpc ActUserRegister (Request) returns (Response) {}
  rpc ActUserPasswordRest (Request) returns (Response) {}
}

message Request {
  string param = 1;
}

message Response {
  string result = 1;
}
```

**AuthUser：**`sc/proto/proto-self/self-ssc/ssc-md/user/AuthUser.proto`
```protobuf
syntax = "proto3";

package sc.proto.self.ssc.md.user;

service AuthUser {
  rpc DelUser (Request) returns (Response) {}
  rpc UpdateUserPassword2 (Request) returns (Response) {}
  rpc UpdateUserPassword3 (Request) returns (Response) {}
  rpc GetUserById (Request) returns (Response) {}
  rpc ListUser32 (Request) returns (Response) {}
  rpc ListUser33 (Request) returns (Response) {}
  rpc PageUserLikeUsername (Request) returns (Response) {}
}

message Request {
  string param = 1;
}

message Response {
  string result = 1;
}
```
