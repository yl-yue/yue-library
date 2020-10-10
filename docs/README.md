<p align="center">
	<a href="https://ylyue.cn/"><img src="https://gitee.com/yl-yue/yue-library/raw/master/docs/_images/logo.png" width="400"></a>
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
　　yue-library是一个基于SpringBoot封装的增强库，内置丰富的JDK工具，自动装配了一系列的基础Bean与环境配置项，可用于快速构建SpringCloud项目，让微服务变得更简单。

## 工程结构
```
. yue-library
├── yue-library  基础库
│   ├── yue-library-dependencies  父pom
│   ├── yue-library-base          基础库提供了丰富的Java工具包，同时也自动装配了一系列基础Bean等
│   ├── yue-library-base-crypto   基于Hutool实现的加解密模块，提供诸如数据脱敏此类的更多特性
│   ├── yue-library-web           基础库WebMvc实现，用于servlet项目
│   ├── yue-library-webflux       基础库WebFlux实现，用于响应式编程项目（如：SpringCloudGateway）
│   ├── yue-library-data-jdbc     基于SpringJDBC进行二次封装，拥有着强大性能的同时又不失简单、灵活等
│   ├── yue-library-data-redis    基于SpringRedis进行二次封装，更简单灵活，提供全局token与登录相关特性等
│   ├── yue-library-auth-service  基于SpringSecurity进行二次封装，更简单灵活，提供全局token与登录等特性
│   ├── yue-library-auth-client   auth-client为auth-service客户端模块，提供获取当前登录用户状态信息等特性
│   ├── yue-library-pay           基于pay-java-parent进行二次封装，让你真正做到一行代码实现支付聚合
│   ├── yue-library-cloud-oss
│   └── yue-library-cloud-sms
├── yue-library-samples  基础库示例
│   ├── yue-library-test				yue-library-web代码测试项目：单元测试、接口测试、代码示例
│   ├── yue-library-test-webflux		yue-library-webflux代码测试项目：单元测试、接口测试、代码示例
│   ├── yue-library-template-simple		yue-library模版：SpringBoot项目模版
│   └── yue-library-template-ssc		yue-library模版：SpringCloud项目模版，SOA共享架构（阿里巴巴中台）
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
随后引入所需要的模块，如基础库：`yue-library-base`
```xml
<dependencies>
	<dependency>
		<groupId>ai.ylyue</groupId>
		<artifactId>yue-library-base</artifactId>
	</dependency>
	...
</dependencies>
```

### 版本说明
　　yue-library的版本命名方式，继2.1.0开始采用与SpringBoot和SpringCloudAlibaba相同的命名方式。<br>
　　`yue-library-base`为其他模块的基础依赖（简称基础库），所以若需要引入除基础库之外的模块（如：web、webflux、data-jdbc、data-redis），可以不引入`yue-library-base`。

**版本区别说明**

|示例版本								|区别说明								|
|--										|--										|
|`j8.2.x`								|基于Java 8的2.x.x版本					|
|`j11.2.x`								|基于Java 11的2.x.x版本					|
|`Finchley.x`、`Greenwich.x`、`2.1.x`	|历史版本，具体区分请查看历史版本文档		|

**默认版本依赖关系**

|yue-library|SpringBoot	|SpringCloud|SpringCloudAlibaba	|
|--			|--			|--			|--					|
|2.1		|2.1+		|Greenwich	|2.1+				|
|2.2		|2.2+		|Hoxton		|2.2+				|

[SpringBoot版本发行说明](https://github.com/spring-projects/spring-boot/wiki/Supported-Versions)<br>
[SpringCloudAlibaba版本发行说明](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E)

更多细节，请查看[中文文档](https://ylyue.cn)

## 模块说明
### yue-library-base（必备）
　　`yue-library-base`提供了丰富的Java工具包，它能够帮助我们简化每一行代码（集成[Hutool](https://hutool.cn)工具包）。<br>
　　同时也自动装配了一系列基础Bean，可在 <b>application.yml</b> 文件中配置关闭，所有配置项皆是以`yue.*`开头，如：`yue.cors.allow=false`代表不允许跨域。
- 丰富的Java基础工具类，对文件、流、加密解密、转码、正则、线程、XML等JDK方法进行封装
- 默认开启热加载、热部署、支持跨域，一键解决联调问题
- 全局统一异常处理基类，结合`Result`对象，定位异常更轻松，前端显示更贴切
- 异步线程池：共用父线程上下文环境，异步执行任务时不丢失token
- `Result`Http最外层响应对象，更适应Restful风格API
- `validator`参数校验器，支持单参数连写与POJO对象（注解）校验等，更多的校验规则，更贴切的国内校验场景。（如：手机号、身份证号码）
- `Convert`类型转换器，内置hutool、fastjson、yue三种类型转换规则，判断精确性能强大，未知类型兼容性更强

　　更多详细介绍，请查看[中文文档](https://ylyue.cn)

### yue-library-data-jdbc<font color=red>（强烈推荐）</font>
　　data-jdbc库基于SpringJDBC进行二次封装，拥有着强大性能的同时又不失简单、灵活。特性如下：
- 比SpringJDBC更方便好用、比SpringJPA更简单灵活
- **无侵入**：data-jdbc 在 SpringJDBC 的基础上进行扩展，只做增强不做改变，简化`CRUD`操作
- **依赖管理**：引入即可启动项目，关联druid实现SQL全监控
- **预防Sql注入**：内置Sql注入剥离器，有效预防Sql注入攻击
- **损耗小**：封装大量经过SQL优化处理的CRUD方法，直接面向对象操作，对比原生级CRUD处理，性能基本无损耗甚至更优
- **通用CRUD操作**：内置通用 DAO，通过继承方式即可实现单表大部分 CRUD 操作
- **更科学的分页**：分页参数自动解析，写分页等同于写基本List查询。更有优化型分页SQL检查
- **内置性能分析插件**：可输出Sql语句以及其执行时间，建议开发测试时启用该功能，能有效解决慢查询
- **类型强化**：支持原生级SQL查询，并强化原生查询结果，简单便捷 + 可维护组合（支持全JSON或全DO）
- **CRUD校验**：CRUD操作是否符合预期，更好的避免脏数据的产生与违规操作
- **全局异常处理**：CRUD操作相关异常统一处理，定位更精准，提示更友好，实现全局Restful风格

　　更多详细介绍，请查看[中文文档](https://ylyue.cn)

### yue-library-data-redis（推荐）
　　data-redis库基于SpringRedis进行二次封装，更简单灵活，提供全局token与登录等特性：
- 简化使用并拥有Redis原生常用命令所对应的方法
- 保留SpringRedis所有常用特性：分布式缓存
- 提供分布式token、分布式锁
- 封装大量第三方登录特性，使登录更简单易于维护
- 封装常用的登录判断操作与redis-token解析

　　更多详细介绍，请查看[中文文档](https://ylyue.cn)

## 社区
　　在[Gitter](https://gitter.im/yl-yue/yue-library)的社区里可以找到yue-library的用户和开发者团队。

## 参与贡献
欢迎各路好汉一起来参与完善 yue-library，我们期待你的 PR！

- 贡献代码：代码地址 [yue-library](https://gitee.com/yl-yue/yue-library) ，欢迎提交 Issue 或者 Pull Requests

> 1. Fork 本仓库并从 <font color=red>JDK对应的分支或对应的某个版本</font> 创建你的分支
> 2. 如果你添加的代码需要测试，请添加测试，确保单元测试通过（测试代码请放在：`yue-library-test`中）
> 3. 如果你修改了 API，请更新文档
> 4. 确保代码风格一致
> 5. 提交代码
> 6. 新建 Pull Request

- 维护文档：文档地址 [yue-library-doc](https://gitee.com/yl-yue/yue-library/tree/master/docs) ，欢迎参与翻译和修订