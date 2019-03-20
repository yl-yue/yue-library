## 介绍

　　yue-library-base是一个基于SpringBoot封装的基础库，内置丰富的JDK工具，并且自动装配了一系列的基础Bean。

## 模块说明
　　基础库提供了丰富的Java工具包，它能够帮助我们简化每一行代码（集成[Hutool](https://hutool.cn)工具包）。<br>
　　同时也自动装配了一系列基础Bean(可在<b>application.yml</b>文件中配置关闭，所有配置项皆是以`yue.*`开头，如：`yue.cors.allow=false`代表不允许跨域)，默认开启如下几项配置：
- RestTemplate-HTTPS客户端
- CorsConfig-跨域配置
- ConstantProperties-yue常量配置
- 默认的统一异常处理`ai.yue.library.base.handler.AllExceptionHandler`（需手动配置）

　　说明：所有模块皆依赖于基础库，若需要引入除基础库之外的模块（如：data-jdbc、data-redis），可以不用引入此模块。

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
