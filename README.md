<!-- <p align=center>
  <a href="http://www.layui.com">
    <img src="https://sentsin.gitee.io/res/images/layui/layui.png" alt="layui" width="360">
  </a>
</p>
<p align=center>
  Classic modular front-end UI framework
</p>

<p align="center">
  <a href="https://travis-ci.org/sentsin/layui"><img alt="Build Status" src="https://img.shields.io/travis/sentsin/layui/master.svg"></a>
  <a href="https://saucelabs.com/beta/builds/7e6196205e4f492496203388fc003b65"><img src="https://saucelabs.com/buildstatus/layui" alt="Build Status"></a>
  <a href="https://coveralls.io/r/sentsin/layui?branch=master"><img alt="Test Coverage" src="https://img.shields.io/coveralls/sentsin/layui/master.svg"></a>
</p>
<p align="center">
  <a href="https://saucelabs.com/beta/builds/7e6196205e4f492496203388fc003b65"><img src="https://saucelabs.com/browser-matrix/layui.svg" alt="Browser Matrix"></a>
</p> -->

---

## yue-library

### 介绍

　　yue-library是一个基于SpringBoot封装的基础库，内置丰富的JDK工具，并且自动装配了一系列的基础Bean，不仅如此，还可以用于快速构建SpringCloud项目，让微服务变得更简单。

### 工程结构

　　- yue-library-dependencies 父pom  
　　- yue-library-template 使用模版  
　　- yue-library-base 基础库，对JDK的一些常用封装，同时配置一些基础Bean等  
　　- yue-library-data-jdbc 基于SpringJDBC的二次封装，简化SQL查询等  
　　- yue-library-data-redis 基于SpringRedis的二次封装，简化Redis查询等

### 快速使用

``` pom
	<parent>
		<groupId>ai.yue.library</groupId>
		<artifactId>yue-library-dependencies</artifactId>
		<version>${version}</version>
	</parent>
```

### 模块说明：yue-library-base

　　基础库提供了丰富的Java工具包，它能够帮助我们简化每一行代码，减少每一个方法的开发。（集成Hutool工具包）
　　基础库提供了丰富的配置Bean(可在application.yml文件中配置关闭)，默认开启：restTemplate-HTTPS客户端、CorsConfig-跨域、ConstantProperties-yue常量配置

### 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request