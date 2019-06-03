## 快速开始

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

## 配置
### 默认配置Bean

如果你想关闭Bean，只需要在<b>application.yml</b>文件中将对应Bean的开启项，改为`false`即可。

- RestTemplate-HTTPS客户端
- CorsConfig-跨域配置 `yue.cors.allow`

### 默认配置属性

- ConstantProperties-yue常量配置 （AES密钥、RSA公钥、RSA私钥、Token超时时间等）

### 可选择配置Bean

- AllExceptionHandler-统一异常处理，如下：

```java
@ControllerAdvice
public class ExceptionHandlerConfig extends AllExceptionHandler{
```

## 结语

引用此项目作基础依赖，后续也可很好的对于服务架构进行升级（单体架构、集群架构、微服务、SOA微服务）。<br>
新开发项目，建议直接以集群架构形式撸代码，不需要一开始就搭建微服务等，可根据业务需求实际出发。（不限于有SOA共享中心的伙伴）