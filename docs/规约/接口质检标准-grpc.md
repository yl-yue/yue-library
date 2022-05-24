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
- 【强制】`proto`文件名与`service`类名保持一致，一个proto一个service
- 【强制】`package`包名全小写与工程目录结构保持一致
- 【强制】`service`类名遵循大驼峰命名法，命名时不用带无意义的`Service`后缀
- 【强制】`rpc`方法名遵循大驼峰命名法，前缀遵循rpc方法命名前缀规约，后缀可带数字表示版本号
- 【强制】`message`消息体名遵循大驼峰命名法，请求体定义使用`Request`后缀，响应体定义使用`Response`后缀，非rpc方法直接引用的消息体可不带后缀
- 【强制】`message`字段名遵循下划线命名法，对重复字段使用复数名称
- 【强制】`enum`枚举名遵循大驼峰命名法
- 【强制】`enum`字段名遵循大写字母下划线命名法，相同规则用前缀表示
- 【推荐】使用`string`类型代替`enum`类型，以约定的方式将枚举值说明写在注释中，但程序中必须定义枚举来解析此字段，并处理相应逻辑。
  - 说明：因为在`proto`中定义的枚举类型只能用来做类型与值约束，并不能添加相应的逻辑处理，使得在程序中反倒成为一种负担。
- 【推荐】使用`string`类型代替`Timestamp`类型，并使用`yyyy-MM-dd HH:mm:ss`格式化时间格式字符串
- 【推荐】使用`string`类型代替`duration`类型，传输时直接使用序列化值，需要时再做相应的反序列化

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
|删			|`Delete`		|物理删除、逻辑删除									|`DeleteUser`																|
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
中台proto工程（无中台可忽略）
```
. proto-sc
├── proto-common              公共message
│   ├── common-message            公共message（中台与业务共用）
│   ├── common-mdp                微服务开发平台（仅自身internet、lan、self可用）
│   └── common-ssc                共享服务中心（仅自身internet、lan、self可用）
├── proto-internet            前端接口
│   ├── internet-mdp              微服务开发平台
│   └── internet-ssc              共享服务中心
├── proto-lan            	  后端接口
│   ├── lan-mdp                   微服务开发平台
│   └── lan-ssc                   共享服务中心
└── proto-self            	  后端私域业务接口
    ├── self-mdp              	  微服务开发平台
    └── self-ssc              	  共享服务中心
```

业务proto工程
```
. proto-bf
├── proto-common         	  公共message
│   ├── common-message            公共message（业务共用）
│   ├── common-auth               认证服务（仅自身internet、lan、self可用）
│   └── common-user               用户服务（仅自身internet、lan、self可用）
├── proto-internet            前端接口
│   ├── internet-auth             认证服务
│   └── internet-user             用户服务
├── proto-lan            	  后端接口
│   ├── lan-auth                  认证服务
│   └── lan-user                  用户服务
└── proto-self            	  后端私域业务接口
    ├── self-auth                 认证服务
    └── self-user                 用户服务
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

!> `proto-common`工程组中的`common-message`模块才属于真正的公共模块，所有`common-*`子工程皆可依赖，但同样需要遵守自身的程序解耦规约与上下通信原则。

### 基础message规约
- 【强制】proto中不允许定义基础message，因为`protobuf message`不支持继承，定义后无论是消息转换还是理解起来，都是繁琐且不优雅的，和封不封装工具类无关。
  - 基础message一但定义便属于一种规范，所有需要的地方都需要引用，嵌套起来无疑是糟糕的，如数据库表中的基础字段。
  - 公共message用于定义`internet、lan、self`之间的公共message。
- 【强制】程序开发中应提供`proto、json、POJO`之间的相互转换工具类，[👉参见：Convert]()实现随意拉平或嵌套映射。
- 【强制】`internet、lan、self`三大工程组通用的message应定义在`common-message`工程中，但需要注意的是中台的`common-message`对于业务前台来说是可见的，但业务前台的`common-message`对于中台来说是不可见的。
- 【强制】虽然不可以定义基础message，但我们可以约束常用字段的类型定义与命名规范，并且这些常用字段应该放在最前面

**常用字段约定：**

```protobuf
/**
 * 虽然不可以定义基础message，但我们可以约束常用字段的类型定义与命名规范，并且这些常用字段应该放在最前面
 */
message CommonFieldProtocol {

  /**
   * 数据库基础字段约定
   */
  int64 id = 1;                  // 有序主键：单表时数据库自增、分布式时雪花自增
  string uuid = 2;               // 无序主键：UUID5无符号
  int32 sort_idx = 3;            // 排序索引
  string create_user = 4;        // 创建人：用户名、昵称、人名
  string create_user_uuid = 5;   // 创建人：用户uuid
  string create_time = 6;        // 创建时间
  string update_user = 7;        // 更新人：用户名、昵称、人名
  string update_user_uuid = 8;   // 更新人：用户uuid
  string update_time = 9;        // 更新时间
  string delete_user = 10;       // 删除人：用户名、昵称、人名
  string delete_user_uuid = 11;  // 删除人：用户uuid
  int64 delete_time = 12;        // 删除时间戳：默认0（未删除）

  /**
   * 接口常用字段约定-分页
   */
  int32 page = 13;     // 当前页
  int32 limit = 14;    // 每页显示条数
  int64 count = 15;    // 分页统计条数

  /**
   * 接口常用header约定
   */
  string tenant_sys = 16;    // 系统租户：一级租户（dict_tenant_sys）
  string tenant_co = 17;     // 企业租户：二级租户
  string client_id = 18;     // 客户端区分标识

}
```

### proto规范示例
**OpenUser：**`sc/proto/proto-lan/lan-ssc/ssc-md/user/OpenUser.proto`
```protobuf
syntax = "proto3";

package sc.proto.lan.ssc.md.user;

service OpenUser {
  rpc ActUserLogin (Request) returns (Response);
  rpc ActUserRegister (Request) returns (Response);
  rpc ActUserPasswordRest (Request) returns (Response);
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
  rpc DeleteUser (Request) returns (Response);
  rpc UpdateUserPassword2 (Request) returns (Response);
  rpc UpdateUserPassword3 (Request) returns (Response);
  rpc GetUserById (Request) returns (Response);
  rpc ListUser32 (Request) returns (Response);
  rpc ListUser33 (Request) returns (Response);
  rpc PageUserLikeUsername (Request) returns (Response);
}

message Request {
  string param = 1;
}

message Response {
  string result = 1;
}
```
