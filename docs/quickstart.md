## 工程结构
```
. yue-library
├── yue-library  父pom
│   ├── yue-library-dependencies  dependencies版本控制
│   ├── yue-library-base          基础库提供了丰富的Java工具包，同时也自动装配了一系列基础Bean等
│   ├── yue-library-base-crypto   基于Hutool实现的加解密模块，提供诸如数据脱敏此类的更多特性
│   ├── yue-library-web           基础库WebMvc实现，用于servlet项目
│   ├── yue-library-webflux       基础库WebFlux实现，用于响应式编程项目（如：SpringCloudGateway）
│   ├── yue-library-data-jdbc     基于SpringJDBC进行二次封装，拥有着强大性能的同时又不失简单、灵活等
│   ├── yue-library-data-redis    基于SpringRedis进行二次封装，更简单灵活，提供全局token与登录相关特性等
│   ├── yue-library-auth-service  基于SpringSecurity进行二次封装，更简单灵活，提供全局token与登录等特性
│   ├── yue-library-auth-client   auth-client为auth-service客户端模块，提供获取当前登录用户状态信息等特性
│   └── yue-library-pay           基于pay-java-parent进行二次封装，让你真正做到一行代码实现支付聚合
├── yue-library-samples  基础库示例
│   ├── yue-library-test                yue-library-web代码测试项目：单元测试、接口测试、代码示例
│   ├── yue-library-test-webflux        yue-library-webflux代码测试项目：单元测试、接口测试、代码示例
│   ├── yue-library-template-simple     yue-library模版：SpringBoot项目模版
│   └── yue-library-template-ssc        yue-library模版：SpringCloud项目模版，SOA共享架构（阿里巴巴中台）
└── yue
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

上面的代码完全保持了SpringBoot的风格，但又提供了更多特性增强，如：HTTP消息转换器对 **Alibaba Fastjson** 的支持，同时不再区分 **query from-data json** 等传参方式，默认也对 **跨域、时间格式、异常、参数校验** 等常见坑点进行了本土化处理与特性增强。

### 版本说明
　　yue-library的版本命名方式，继2.1.0开始采用与 [SpringBoot版本发行](https://github.com/spring-projects/spring-boot/wiki/Supported-Versions) 对应的命名方式。<br>
　　`yue-library-base`为其他模块的基础依赖（简称基础库），所以若需要引入除基础库之外的模块（如：web、webflux、data-jdbc、data-redis），可以不引入`yue-library-base`。

|示例版本号								|版本号区别								|
|--										|--										|
|`j8.2.x`								|基于Java 8的2.x.x版本					|
|`j11.2.x`								|基于Java 11的2.x.x版本					|
|`Finchley.x`、`Greenwich.x`、`2.1.x`	|历史版本，具体区分请查看历史版本文档		|

[👉点击查看pom.xml依赖](https://gitee.com/yl-yue/yue-library/blob/master/pom.xml)

## 配置与文档说明
　　`yue-library`自动装配了一系列的基础Bean与环境配置项，可在 <b>application.yml</b> 文件中配置关闭，所有配置项皆是以`yue.*`开头，如：`yue.cors.allow=false`代表不允许跨域，更多配置项与细节介绍，可查看 [官方文档](https://ylyue.cn) 中各模块的详细说明。<br>
　　[官方文档](https://ylyue.cn) 提供各模块的详细介绍与使用示例，类与方法说明可参阅 [API文档](https://apidoc.gitee.com/yl-yue/yue-library/)，SpringBoot项目基础模版可使用 `yue-library-template-simple`，单元测试代码与标准示例可参考：`yue-library-test`

## 结语
引用此项目作基础依赖，后续也可很好的对于服务架构进行升级（单体架构、集群架构、微服务、SOA微服务）<br>
新开发项目，建议直接以集群架构形式撸代码，不需要一开始就搭建微服务等，可根据业务实际需求出发。（不限于有SOA共享中心的伙伴）