## 工程结构
yue-library工程区分顶级module与子级module：
- 顶级module即父子模块，迭代维护频繁
- 子级module属于当前版本已成熟或暂不需要频繁迭代维护的模块
- 子级module位于：yue-library-extra目录下
- 区分顶级module与子级module的目的在于，进行多模块打包时，可以加快构建速度并且增强模块独立维护性（子级module未迭代且兼容的情况下，不再随主版本一起发布）
- 顶级module与子级module会在不同版本进行对调

```
. yue-library
├── yue-library                       yue-library顶级模块
│   ├── yue-library-dependencies          dependencies版本控制
│   ├── yue-library-base                  基础核心模块，提供丰富的Java工具类库、接口参数校验、类型转换器等
│   ├── yue-library-web                   WebMvc模块，servlet编程，提供请求与响应参数的包装与解析等
│   ├── yue-library-webflux               WebFlux实现，响应式编程（如：SpringCloudGateway）
│   ├── yue-library-web-grpc              WebFlux实现，响应式编程（如：SpringCloudGateway）
│   └── yue-library-data-jdbc             ORM框架，基于SpringJdbc，拥有着强大性能的同时又不失简单灵活等
├── yue-library-extra                 yue-library子级模块
│   ├── yue-library-base-crypto           加解密模块，提供对称、非对称和摘要算法、密钥交换加解密等
│   ├── yue-library-data-redis            Redis客户端，基于SpringRedis，更简单灵活，提供分布式锁等
│   ├── yue-library-data-es               Elasticsearch Rest与SQL客户端（兼容OpenSearch），提供安全认证等属性配置
│   ├── yue-library-auth-service          OAuth2认证模块，基于SpringSecurity，更简单灵活，提供全局token与登录等
│   ├── yue-library-auth-client           OAuth2客户端模块，提供获取当前登录用户状态信息等
│   └── yue-library-pay                   支付模块，基于pay-java-parent，让你真正做到一行代码实现支付聚合
└── yue-library-samples               yue-library示例项目
    ├── yue-library-test                  web测试项目，提供详细的特性使用示例、接口单元测试
    ├── yue-library-test-webflux          webflux测试项目，提供详细的特性使用示例、接口单元测试
	├── yue-library-test-grpc             grpc测试项目，提供详细的特性使用示例、接口单元测试
    ├── yue-library-template-boot         SpringBoot项目模版，提供快速开发示例
    └── yue-library-template-cloud        SpringCloud项目模版，SOA共享架构（阿里巴巴中台）
```

## 快速开始
### 引入项目依赖
maven项目，在pom.xml文件中添加如下一段代码，并将`${version}`替换为对应版本号：[![Maven Central with version prefix filter](https://img.shields.io/maven-central/v/ai.ylyue/yue-library/j?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/ai.ylyue/yue-library-dependencies)
```xml
<parent>
	<groupId>ai.ylyue</groupId>
	<artifactId>yue-library-dependencies</artifactId>
	<version>${version}</version>
</parent>
```
随后引入所需要的模块，如WebMvc项目引入：`yue-library-web`

依赖说明：`yue-library-base`为基础模块，一般情况下不需要单独引入，如：web、data-jdbc、data-redis等模块皆已默认依赖。
```xml
<dependencies>
	<dependency>
		<groupId>ai.ylyue</groupId>
		<artifactId>yue-library-web</artifactId>
	</dependency>
	...
</dependencies>
```

### 启动项目
新建一个SpringBoot `main`方法启动类：
```java
@SpringBootApplication
public class TestApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(TestApplication.class, args);
	}

}
```

写一个测试接口：
```java
@RestController
@RequestMapping("/quickstart")
public class QuickstartController {

	@GetMapping("/get")
	public Result<?> get(JSONObject paramJson) {
		return R.success(paramJson);
	}
	
}
```

访问接口测试，如：http://localhost:8080/quickstart/get
```json
{
    "code": 200,
    "msg": "成功",
    "flag": true,
    "count": null,
    "data": {}
}
```

上述代码完全保持了SpringBoot的风格，但又使用到了yue-library的增强特性，如：
- HTTP消息转换器支持使用Alibaba Fastjson作为参数接收对象
- 请求参数智能解析，无需再为URL query-string、Body from-data、Body application/json传参方式烦恼
- 错误时会对异常进行统一处理，响应RESTful风格的错误提示
- 支持前端跨域请求

当然除了这些已使用到的特性之外，你还可以尝试如：响应时间类型时自动格式化、请求参数校验、API接口版本控制、反复读取Servlet输入流等。
并且在`yue-library-samples`目录下，存放着不同架构类型的示例模板，你可以根据自身需求选择，从而快速上手。

## 配置与文档
yue-library提供的所有可配置项，皆是以`yue.*`开头，如：`yue.cors.allow=false`代表不允许跨域，你可以根据配置提示，开启与关闭所有的功能特性。

[官网文档](https://ylyue.cn)中提供了各模块的详细介绍与使用示例

[JavaDoc文档](https://apidoc.gitee.com/yl-yue/yue-library/)中提供了类与方法说明

## 结语
引用此项目作基础依赖，后续也可很好的对于服务架构进行升级（单体架构、集群架构、微服务、SOA微服务）<br>
新开发项目，建议直接以集群架构形式撸代码，不需要一开始就搭建微服务等，可根据业务实际需求出发。（不限于有SOA共享中心的伙伴）