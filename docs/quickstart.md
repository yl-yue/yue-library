## 工程结构
```
. yue-library
├── yue-library  基础库
│   ├── yue-library-dependencies  父pom
│   ├── yue-library-base          基础库提供了丰富的Java工具包，同时也自动装配了一系列基础Bean等
│   ├── yue-library-data-jdbc     基于SpringJDBC进行二次封装，拥有着强大性能的同时又不失简单、灵活等
│   ├── yue-library-data-redis    基于SpringRedis进行二次封装，更简单灵活，提供全局token与登录相关特性等
│   ├── yue-library-pay           基于pay-java-parent进行二次封装，让你真正做到一行代码实现支付聚合
│   ├── yue-library-cloud-oss
│   └── yue-library-cloud-sms
├── yue-library-samples  基础库示例
│   ├── yue-library-test				yue-library代码测试项目：单元测试、接口测试、代码示例
│   ├── yue-library-template-simple		yue-library模版：SpringBoot项目模版
│   └── yue-library-template-ssc		yue-library模版：SpringCloud项目模版，SOA共享架构（阿里巴巴中台）
└── yue
```

## 快速开始
### 引入项目依赖
maven项目，在pom.xml文件中添加如下一段代码，并将`${version}`替换为对应版本号：[![maven-central](https://img.shields.io/maven-central/v/ai.ylyue/yue-library-dependencies.svg?label=Maven%20Central)](https://maven-badges.herokuapp.com/maven-central/ai.ylyue/yue-library-dependencies)
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
　　yue-library的版本命名方式，采用SpringCloud版本名作为前缀，然后以.1、.2、.3...这种形式，目的是为了方便区分所依赖的`SpringCloud`版本。<br>
　　`yue-library-base`为其他模块的基础依赖（简称基础库），所以若需要引入除基础库之外的模块（如：data-jdbc、data-redis），可以不引入`yue-library-base`。

|JDK版本|JDK说明												|SpringCloud版本|版本说明																			|
|--		|--														|--				|--																					|
|JDK8	|LTS（Oracle长期支持版本），目前大部分互联网公司采用版本|Finchley		|JDK8兼容版本，每次新特性发布都会进行一次全面的兼容适配与测试，以供JDK8用户稳定使用	|
|JDK11	|LTS（Oracle长期支持版本），作者采用版本				|Greenwich		|JDK11推荐版本，提供更快速的迭代与反馈												|

## 配置与文档说明
　　`yue-library`自动装配了一系列的基础Bean与环境配置项，可在 <b>application.yml</b> 文件中配置关闭，所有配置项皆是以`yue.*`开头，如：`yue.cors.allow=false`代表不允许跨域，更多配置项与细节介绍，可查看 [官方文档](https://ylyue.cn) 中各模块的详细说明。<br>
　　[官方文档](https://ylyue.cn) 提供各模块的详细介绍与使用示例，类与方法说明可参阅 [API文档](https://apidoc.gitee.com/yl-yue/yue-library/)，SpringBoot项目基础模版可使用 `yue-library-template-simple`，单元测试代码与标准示例可参考：`yue-library-test`

## 结语
引用此项目作基础依赖，后续也可很好的对于服务架构进行升级（单体架构、集群架构、微服务、SOA微服务）<br>
新开发项目，建议直接以集群架构形式撸代码，不需要一开始就搭建微服务等，可根据业务实际需求出发。（不限于有SOA共享中心的伙伴）