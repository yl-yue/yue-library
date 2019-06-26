## 介绍
　　基础库提供了丰富的Java工具包，它能够帮助我们简化每一行代码（集成[Hutool](https://hutool.cn)工具包）。<br>
　　同时也自动装配了一系列基础Bean，可在<b>application.yml</b>文件中配置关闭，所有配置项皆是以`yue.*`开头，如：`yue.cors.allow=false`代表不允许跨域。
- 丰富的Java基础工具类，对文件、流、加密解密、转码、正则、线程、XML等JDK方法进行封装
- `Result`Http最外层响应对象，更适应Restful风格API
- 默认开启热加载、热部署、支持跨域，一键解决联调问题
- 基于`validator`扩展IPO增强校验注解，更适合国内校验场景。（如：手机号、身份证号码）
- 全局统一异常处理基类，结合`Result`对象，定位异常更轻松，前端显示更贴切（可以直接继承使用）

## 配置
　　`yue-library-base`自动装配了一系列基础Bean，可在<b>application.yml</b>文件中配置关闭，所有配置项皆是以`yue.*`开头，如：`yue.cors.allow=false`代表不允许跨域。

### 默认配置Bean
如果你想关闭Bean，只需要在<b>application.yml</b>文件中将对应Bean的开启项，改为`false`即可。
- RestTemplate-HTTPS客户端
- CorsConfig-跨域配置 `yue.cors.allow`

### 默认配置属性
- ConstantProperties-yue常量配置 （AES密钥、RSA公钥、RSA私钥、Token超时时间、验证码超时时间等）

### 可选择配置Bean
- 默认的统一异常处理`ai.yue.library.base.handler.AllExceptionHandler`（需手动配置）
- AllExceptionHandler-统一异常处理，如下：

```java
@ControllerAdvice
public class ExceptionHandlerConfig extends AllExceptionHandler {
```

## 文档说明
　　基础工具包文档见[Hutool](https://hutool.cn/docs)核心模块`hutool-core`部分：https://hutool.cn/docs<br>
　　更多工具包文档见本示例说明：[常用工具类](base-常用工具类.md)，[校验](base-校验.md) ...