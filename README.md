<p align="center">
	<a target="_blank" href="https://ylyue.cn/">
		<img src="https://dcloud.ylyue.cn/yue-library/_images/logo.png" width="400">
	</a>
</p>
<p align="center">
	<strong>一个基于SpringBoot封装的增强库</strong>
</p>
<p align="center">
	<a target="_blank" href="https://ylyue.cn/">
		<img src="https://img.shields.io/badge/文档-yue-blue.svg?style=flat-square" alt="yue-library官网">
	</a>
	<a target="_blank" href="https://apidoc.gitee.com/yl-yue/yue-library/">
		<img src="https://img.shields.io/badge/文档-javadoc-blue.svg?style=flat-square" alt="yue-library JavaDoc">
	</a>
	<a target="_blank" href="https://maven-badges.herokuapp.com/maven-central/ai.ylyue/yue-library-dependencies">
		<img alt="Maven Central with version prefix filter" src="https://img.shields.io/maven-central/v/ai.ylyue/yue-library/j?style=flat-square">
	</a>
	<a target="_blank" href="https://www.apache.org/licenses/LICENSE-2.0.html">
		<img alt="GitHub" src="https://img.shields.io/github/license/yl-yue/yue-library?style=flat-square">
	</a>
	<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html">
		<img src="https://img.shields.io/badge/Java-8+11+-green?style=flat-square" >
	</a>
	<a target="_blank" href="https://spring.io/projects/spring-boot">
		<img src="https://img.shields.io/badge/Spring%20Boot-2.1+2.2+-green?style=flat-square" alt="Spring%20Boot Version">
	</a>
	<a target="_blank" href="https://spring.io/projects/spring-cloud">
		<img src="https://img.shields.io/badge/Spring%20Cloud-Greenwich+Hoxton+-green?style=flat-square" alt="Spring%20Cloud Version">
	</a>
	<a target="_blank" href="https://gitter.im/yl-yue/yue-library?utm_source=share-link&utm_medium=link&utm_campaign=share-link">
		<img src="https://badges.gitter.im/yl-yue/yue-library.svg" >
	</a>
	<a target="_blank" href="https://gitee.com/yl-yue/yue-library">
		<img src='https://gitee.com/yl-yue/yue-library/badge/star.svg?theme=dark' alt='gitee star'>
	</a>
	<a target="_blank" href='https://github.com/yl-yue/yue-library'>
		<img alt="GitHub Repo stars" src="https://img.shields.io/github/stars/yl-yue/yue-library?style=social">
	</a>
	<a target="_blank" href="https://github.com/yl-yue/yue-library/issues">
		<img alt="GitHub issues" src="https://img.shields.io/github/issues/yl-yue/yue-library?style=flat-square">
	</a>
</p>
<p align="center">
	-- 主页：<a href="https://ylyue.cn">https://ylyue.cn/</a> --
</p>
<p align="center">
	-- QQ群：<a href="https://jq.qq.com/?_wv=1027&k=5WI2Vbb">883630899</a> --
</p>

-------------------------------------------------------------------------------

## 介绍
　　yue-library是一个基于SpringBoot封装的增强库，提供丰富的Java工具类库、优越的ORM框架、优雅的业务封装、优化的Spring环境配置、完善的规约限制、配套的代码生成平台、安稳贴切的开源架构方案等，只为打造更好的JavaWeb开发环境，提升大家的开发质量与效率，降低企业研发成本。

　　适用于企业快速构建属于自己的切合架构，不为技术负累，不盲目跟风，不原地踏步，从**单体与集群**到**分布式与微服务**，企业结合自身业务所处阶段，灵活选择逐步跟进升级。

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
│   ├── yue-library-base                  基础核心模块，提供丰富的Java工具类库、接口参数校验、类型转换器等
│   ├── yue-library-web                   WebMvc模块，servlet编程，提供请求与响应参数的包装与解析等
│   └── yue-library-data-jdbc             ORM框架，基于SpringJdbc，拥有着强大性能的同时又不失简单灵活等
├── yue-library-extra                 yue-library子级模块
│   ├── yue-library-base-crypto           加解密模块，提供对称、非对称和摘要算法、密钥交换加解密等
│   ├── yue-library-webflux               WebFlux模块，响应式编程（如：SpringCloudGateway）
│   ├── yue-library-web-grpc              gRPC模块，RPC编程，Protobuf协议定义接口与序列化数据
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
	<artifactId>yue-library</artifactId>
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
    "traceId": "a1bde0ba625de731",
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

## 核心模块说明
### yue-library-base（必备）
　　base模块提供了丰富的Java工具类库，它能够帮助我们简化每一行代码（增强[Hutool](https://hutool.cn)工具包）。<br>
　　同时提供优越的Spring本土化环境配置，可在 <b>application.yml</b> 文件中配置关闭，所有配置项皆是以`yue.*`开头，如：`yue.cors.allow=false`代表不允许跨域。
- 丰富的Java基础工具类，对文件、流、加密解密、转码、正则、线程、XML等JDK方法进行封装
- 完善的RESTful支持，使用HTTP响应包装对象`Result`，友好返回接口响应内容
- 完善的异常处理机制，RESTful状态码与HTTP状态码自动同步，异常提示简单易懂
- 出色的服务端校验框架`validator`，支持多种校验方式，简单易用，校验规则丰富，更贴切国内校验场景
- 强大的类型转换器`Convert`，优越的性能，超强的容错能力，妈妈再也不用担心我找不到好用的“BeanUtils”了
- 安全的异步线程池，完美解决子线程上下文环境问题，就算是高并发下，异步执行任务时再也不丢失token等参数了
- 优雅的密钥交换加密，安全高效的解决HTTP通信传输安全
- 简洁完善的Java全局网络代理，轻松解决内网部署时的网络代理需求

### yue-library-web/webflux（必备）
　　web/webflux模块提供友好的JavaWeb开发环境，免去本土化烦恼，提供友好的默认配置，解决开发中常遇的槽点，提升大家的开发质量与效率，降低企业研发成本。
- 美妙愉悦的开发体验，让跨域、热加载、时间格式、参数获取等，低级而又普遍存在的问题都见鬼去吧
- 强大的HTTP请求参数解析器，解决参数获取困扰，Request请求参数智能解析，让前后端联调和平相处
- 强大的HTTP响应消息转换器，优雅实现固定类型参数格式化、NULL值处理等，轻松收到来至前端同事的致谢
- 强大的HTTP请求包装器，解决Servlet输入流被前置的某个拦截器读取一空，导致请求参数获取不到
- 强大的`ServletUtils`，让我们在任何时候任何地方，对接口的请求参数与响应内容都能为所欲为
- 优雅的接口版本控制`@ApiVersion`，再也不用担心团队成员对接口规范视若无睹为所欲为了

### yue-library-data-jdbc<font color=red>（强烈推荐）</font>
　　data-jdbc模块基于SpringJdbc封装的ORM框架，拥有着强大性能的同时又不失简单灵活，特性如下：
- **强大易用**：比SpringJdbc更方便好用、比SpringJpa更简单灵活
- **无侵入**：data-jdbc在SpringJdbc的基础上进行扩展，只做增强不做改变，简化`CRUD`等操作
- **依赖管理**：引入即可启动项目，关联druid实现SQL全监控
- **预防Sql注入**：内置Sql注入剥离器，有效预防Sql注入攻击
- **损耗小**：封装大量经过SQL优化处理的CRUD方法，直接面向对象操作，对比原生级CRUD处理，性能基本无损甚至更优
- **通用CRUD操作**：内置通用DAO，通过继承方式即可实现单表大部分CRUD操作
- **更科学的分页**：分页参数自动解析，写分页等同于写基本List查询，更有优化型分页SQL检查
- **内置性能分析插件**：可输出Sql语句以及其执行时间，建议开发测试时启用该功能，能有效解决慢查询
- **类型强化**：支持原生级SQL查询，并强化原生查询结果，简单便捷+可维护组合（支持全Json或全DO）
- **CRUD校验**：CRUD操作是否符合预期，更好的避免脏数据的产生与违规操作
- **JDBC审计**：敏感操作全覆盖，简单实现对数据变动的审计需求
- **数据脱敏**：只需简单的配置即可实现对数据脱敏存储需求，操作时自动加解密
- **全局异常处理**：CRUD操作相关异常统一处理，定位更精准，提示更友好，实现全局RESTful风格

### 更多模块
　　关于`data-redis`、`data-es`、`auth-service`、`auth-client`等模块的特性介绍与更详细的使用说明，请查看[中文文档](https://ylyue.cn)

## 社区
　　在[Gitter](https://gitter.im/yl-yue/yue-library)的社区里可以找到yue-library的用户和开发者团队。

## 参与贡献
欢迎各路好汉一起来参与完善 yue-library，我们期待你的 PR！

- 贡献代码：代码地址 [yue-library](https://gitee.com/yl-yue/yue-library) ，欢迎提交 Issue 或者 Pull Requests

> 1. Fork本仓库并从<font color=red>master分支</font>创建你的分支
> 2. 如果你添加的代码需要测试，请添加测试，确保单元测试通过（测试代码请放在：`yue-library-test`中）
> 3. 如果你修改了API，请更新文档
> 4. 确保代码风格一致
> 5. 提交代码
> 6. 新建Pull Request
> 7. 等待维护者合并

- 维护文档：文档地址 [yue-library-doc](https://gitee.com/yl-yue/yue-library/tree/master/docs) ，欢迎参与翻译和修订

### PR遵照原则
yue-library欢迎你的加入，进行[开源共建](https://ylyue.cn/#/开源共建/开源共建)，提交的pr（pull request）需符合如下规范：
- 关于注释：提供完备的注释，尤其对每个新增的方法应按照Java文档规范标明方法说明、参数说明、返回值说明等信息，亦可加上署名，必要时请添加单元测试
- 关于缩进：采用IDEA中默认的<font color=red>**`空格`**</font>作为标准，可设置一个`tab`四个空格
- 关于三方库：新加的方法不要使用第三方库的方法，yue-library遵循无依赖原则

## 特别鸣谢
<a target="_blank" href="https://www.jetbrains.com/?from=yue-library">
	<img src="https://dcloud.ylyue.cn/yue-library/_images/jetbrains.svg" alt="logo-jetbrains">
</a>&emsp;&emsp;&emsp;&emsp;&emsp;
<a target="_blank" href="https://spring.io/tools/?from=yue-library">
	<img src="https://dcloud.ylyue.cn/yue-library/_images/logo-spring-tools-4.png" alt="logo-spring-tools-4">
</a>