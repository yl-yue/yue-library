## 快速开始
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
　　yue-library的版本命名方式，采用SpringCloud版本名作为前缀，然后以.SR1、.SR2、.SR3...这种形式，目的是为了方便区分基础依赖版本。<br>
　　同时所有模块皆依赖于基础库，若需要引入除基础库之外的模块（如：data-jdbc、data-redis），可以不用引入此模块。

## 配置
　　`yue-library`自动装配了一系列的基础Bean与环境配置项，可在<b>application.yml</b>文件中配置关闭，所有配置项皆是以`yue.*`开头，如：`yue.cors.allow=false`代表不允许跨域。

### 默认配置项
- ConstantProperties-yue常量配置 （AES密钥、RSA公钥、RSA私钥、Token超时时间、验证码超时时间等）详见：`base`
- Validator-校验器 （唯一Bean，可直接覆盖）详见：`base`
- RestTemplate-HTTPS客户端 （唯一Bean，可直接覆盖）详见：`base`
- DB JDBC数据库操作，建议直接继承`DBDAO 或 DBTDAO`。详见：`data-jdbc`
- Redis Redis数据库操作，如：分布式锁等。详见：`data-redis`
- User 用户Bean，如：登录、token操作等。详见：`data-redis`

### 可选配置项
- ExceptionHandlerConfig-全局统一异常处理 `yue.exception-handler.*` 详见：`base`
- AsyncConfig-异步线程池（共用父线程上下文环境，异步执行任务时不丢失token） `yue.thread-pool.async.*` 详见：`base`
- CorsConfig-跨域配置 `yue.cors.*` 详见：`base`
- WxMaUser 微信小程序登录Bean `yue.wx.miniapp.*` 详见：`data-redis`

## 结语
引用此项目作基础依赖，后续也可很好的对于服务架构进行升级（单体架构、集群架构、微服务、SOA微服务）。<br>
新开发项目，建议直接以集群架构形式撸代码，不需要一开始就搭建微服务等，可根据业务需求实际出发。（不限于有SOA共享中心的伙伴）