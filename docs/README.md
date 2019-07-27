<p align="center">
	<a href="https://ylyue.cn/"><img src="https://gitee.com/yl-yue/yue-library/raw/master/docs/_images/logo.png" width="400"></a>
</p>
<p align="center">
	<strong>一个基于SpringBoot封装的基础库</strong>
</p>
<p align="center">
	<a href="https://ylyue.cn/">
		<img src="https://img.shields.io/badge/文档-yue-blue.svg?style=flat-square" alt="yue-library官网">
	</a>
	<a target="_blank" href="https://www.apache.org/licenses/LICENSE-2.0.html">
		<img src="https://img.shields.io/:license-apache-blue.svg" >
	</a>
	<a target="_blank" href="https://maven-badges.herokuapp.com/maven-central/ai.ylyue/yue-library-dependencies">
		<img src="https://img.shields.io/maven-central/v/ai.ylyue/yue-library-dependencies.svg?label=Maven%20Central">
	</a>
	<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html">
		<img src="https://img.shields.io/badge/JDK-11+-green.svg" >
	</a>
	<a target="_blank" href="https://spring.io/projects/spring-boot">
		<img src="https://img.shields.io/badge/Spring%20Boot-2.1+-green.svg" alt="Spring%20Boot Version">
	</a>
	<a target="_blank" href="https://spring.io/projects/spring-cloud">
		<img src="https://img.shields.io/badge/Spring%20Cloud-Greenwich+-green.svg" alt="Spring%20Cloud Version">
	</a>
	<a target="_blank" href="https://gitter.im/yl-yue/yue-library?utm_source=share-link&utm_medium=link&utm_campaign=share-link">
		<img src="https://badges.gitter.im/yl-yue/yue-library.svg" >
	</a>
	<a target="_blank" href="https://gitee.com/yl-yue/yue-library">
		<img src='https://gitee.com/yl-yue/yue-library/badge/star.svg?theme=dark' alt='gitee star'>
	</a>
	<a target="_blank" href='https://github.com/yl-yue/yue-library'>
		<img src="https://img.shields.io/github/stars/yl-yue/yue-library.svg?style=social" alt="github star">
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

　　yue-library是一个基于SpringBoot封装的基础库，内置丰富的JDK工具，并且自动装配了一系列的基础Bean，不仅如此，还可以用于快速构建SpringCloud项目，让微服务变得更简单。

## 工程结构

```
. yue-library
├── yue-library  基础库
│   ├── yue-library-dependencies  父pom
│   ├── yue-library-base          基础库提供了丰富的Java工具包，同时也自动装配了一系列基础Bean等
│   ├── yue-library-data-jdbc     基于SpringJDBC进行二次封装，拥有着强大性能的同时又不失简单、灵活等
│   ├── yue-library-data-redis    基于SpringRedis进行二次封装，更简单灵活，提供全局token与登录相关特性等
│   ├── yue-library-cloud-oss
│   ├── yue-library-cloud-sms
│   └── yue-library-pay
├── yue-library-samples  基础库示例
│   ├── yue-library-template
│   └── yue-library-demo
└── yue
```

## 快速使用
maven项目，在pom.xml文件中添加如下一段代码，并将`${version}`替换为对应版本号：[![maven-central](https://img.shields.io/maven-central/v/ai.ylyue/yue-library-dependencies.svg?label=Maven%20Central)](https://maven-badges.herokuapp.com/maven-central/ai.ylyue/yue-library-dependencies)
```xml
<parent>
	<groupId>ai.ylyue</groupId>
	<artifactId>yue-library-dependencies</artifactId>
	<version>${version}</version>
</parent>
```
随后引入所需要的模块
```xml
<dependencies>
	<dependency>
		<groupId>ai.ylyue</groupId>
		<artifactId>yue-library-base</artifactId>
	</dependency>
	...
</dependencies>
```
　　yue-library的版本命名方式，采用SpringCloud版本名作为前缀，然后以.1、.2、.3...这种形式，目的是为了方便区分基础依赖版本。<br>
　　同时所有模块皆依赖于基础库，若需要引入除基础库之外的模块（如：data-jdbc、data-redis），可以不用引入此模块。<br>

　　更多细节，请查看[中文文档](https://ylyue.cn)

## 模块说明
### yue-library-base（必备）
　　`yue-library-base`提供了丰富的Java工具包，它能够帮助我们简化每一行代码（集成[Hutool](https://hutool.cn)工具包）。<br>
　　同时也自动装配了一系列基础Bean，可在 <b>application.yml</b> 文件中配置关闭，所有配置项皆是以`yue.*`开头，如：`yue.cors.allow=false`代表不允许跨域。
- 丰富的Java基础工具类，对文件、流、加密解密、转码、正则、线程、XML等JDK方法进行封装
- `Result`Http最外层响应对象，更适应Restful风格API
- 默认开启热加载、热部署、支持跨域，一键解决联调问题
- 基于`validator`扩展IPO增强校验注解，更适合国内校验场景。（如：手机号、身份证号码）
- 全局统一异常处理，结合`Result`对象，定位异常更轻松，前端显示更贴切
- 异步线程池：共用父线程上下文环境，异步执行任务时不丢失token

　　更多详细介绍，请查看[中文文档](https://ylyue.cn)

### yue-library-data-jdbc<font color=red>（强烈推荐）</font>
　　data-jdbc库基于SpringJDBC进行二次封装，拥有着强大性能的同时又不失简单、灵活。特性如下：
- 比SpringJDBC更方便好用、比SpringJPA更简单灵活
- **无侵入**：data-jdbc 在 SpringJDBC 的基础上进行扩展，只做增强不做改变，简化`CRUD`操作
- **依赖管理**：引入即可启动项目，关联druid实现SQL全监控
- **预防Sql注入**：内置Sql注入剥离器，有效预防Sql注入攻击
- **损耗小**：原生级CURD操作，性能基本无损耗，直接面向对象操作，同时还有大量经过SQL优化处理的CRUD方法
- **通用CRUD操作**：内置通用 DAO，通过继承方式即可实现单表大部分 CRUD 操作
- **更科学的分页**：分页参数自动解析，写分页等同于写基本List查询。更有优化型分页SQL检查
- **内置性能分析插件**：可输出Sql语句以及其执行时间，建议开发测试时启用该功能，能有效解决慢查询
- **类型强化**：支持原生级SQL查询，并强化原生查询结果，简单便捷 + 可维护组合（支持全JSON或全DO）
- **查询校验**：CRUD预期值判断
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

> 1. Fork 本仓库并从master或某个版本创建你的分支
> 2. 如果你添加的代码需要测试，请添加测试，确保单元测试通过
> 3. 如果你修改了 API，请更新文档
> 4. 确保代码风格一致
> 5. 提交代码
> 6. 新建 Pull Request

- 维护文档：文档地址 [yue-library-doc](https://gitee.com/yl-yue/yue-library/tree/master/docs) ，欢迎参与翻译和修订