<p align="center">
	<a href="https://ylyue.cn/"><img src="https://gitee.com/yl-yue/yue-library/raw/master/docs/_images/logo.png" width="400"></a>
</p>
<p align="center">
	<strong>一个基于SpringBoot封装的基础库</strong>
</p>
<p align="center">
	<a target="_blank" href="https://www.apache.org/licenses/LICENSE-2.0.html">
		<img src="https://img.shields.io/:license-apache-blue.svg" ></img>
	</a>
	<a target="_blank" href="https://maven-badges.herokuapp.com/maven-central/ai.ylyue/yue-library-dependencies">
		<img src="https://img.shields.io/maven-central/v/ai.ylyue/yue-library-dependencies.svg?label=Maven%20Central"></img>
	</a>
	<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html">
		<img src="https://img.shields.io/badge/JDK-11+-green.svg" ></img>
	</a>
	<a target="_blank" href="https://gitter.im/yl-yue/yue-library?utm_source=share-link&utm_medium=link&utm_campaign=share-link">
		<img src="https://badges.gitter.im/yl-yue/yue-library.svg" ></img>
	</a>
	<a target="_blank" href="https://gitee.com/yl-yue/yue-library/stargazers">
		<img src='https://gitee.com/yl-yue/yue-library/badge/star.svg?theme=dark' alt='gitee star'></img>
	</a>
<!-- 	<a target="_blank" href='https://github.com/looly/hutool'>
		<img src="https://img.shields.io/github/stars/looly/hutool.svg?style=social" alt="github star"></img>
	</a> -->
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

　　- yue-library-dependencies 父pom  
　　- yue-library-template 使用模版  
　　- yue-library-base 基础库，对JDK的一些常用封装，同时配置一些基础Bean等  
　　- yue-library-data-jdbc 基于SpringJDBC的二次封装，简化SQL查询等  
　　- yue-library-data-redis 基于SpringRedis的二次封装，简化Redis查询等

## 快速使用
maven项目，在pom.xml文件中添加如下一段代码，并将`${version}`替换为对应版本号
``` pom
	<parent>
		<groupId>ai.ylyue</groupId>
		<artifactId>yue-library-dependencies</artifactId>
		<version>${version}</version>
	</parent>
```
随后引入所需要的模块
``` pom
	<dependencies>
		<dependency>
			<groupId>ai.ylyue</groupId>
			<artifactId>yue-library-base</artifactId>
		</dependency>
		...
	</dependencies>
```
yue-library的版本命名方式，采用SpringCloud版本名作为前缀，然后以.SR1、.SR2、.SR3...这种形式，目的是为了方便区分基础依赖版本。
当前最新发布版本[![maven-central](https://img.shields.io/maven-central/v/ai.ylyue/yue-library-dependencies.svg?label=Maven%20Central)](https://maven-badges.herokuapp.com/maven-central/ai.ylyue/yue-library-dependencies)<br>
更多细节，请查看[中文文档](https://ylyue.cn)

## 模块说明
### yue-library-base（必备）
　　基础库提供了丰富的Java工具包，它能够帮助我们简化每一行代码（集成[Hutool](https://hutool.cn)工具包）。<br>
　　同时也自动装配了一系列基础Bean(可在<b>application.yml</b>文件中配置关闭，所有配置项皆是以`yue.*`开头，如：`yue.cors.allow=false`代表不允许跨域)，默认开启如下几项配置：
- RestTemplate-HTTPS客户端
- CorsConfig-跨域配置
- ConstantProperties-yue常量配置
- 默认的统一异常处理`ai.yue.library.base.handler.AllExceptionHandler`（需手动配置）

　　说明：所有模块皆依赖于基础库，若需要引入除基础库之外的模块（如：data-jdbc、data-redis），可以不用引入此模块。更多详细介绍，请查看[中文文档](https://ylyue.cn)
### yue-library-data-jdbc（推荐）
　　data-jdbc库基于SpringJDBC进行二次封装，拥有着强大的性能的同时又不失简单、灵活。特性如下：
- 比SpringJDBC更方便好用、比SpringJPA更简单灵活
- 支持原生级SQL查询
- 大量经过SQL优化处理的CRUD方法
- 强化原生查询结果（支持：POJO、JSON）
- CRUD预期值判断

　　更多详细介绍，请查看[中文文档](https://ylyue.cn)
### yue-library-data-redis
　　data-jdbc库基于SpringRedis进行二次封装，特性如下：
- 简化使用
- 拥有原生常用命令对应的方法

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