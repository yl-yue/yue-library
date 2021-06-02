# Changelog
---
## 版本说明
　　yue-library的版本命名方式，继2.1.0开始采用与 [SpringBoot版本发行](https://github.com/spring-projects/spring-boot/wiki/Supported-Versions) 对应的命名方式。<br>
　　`yue-library-base`为其他模块的基础依赖（简称基础库），所以若需要引入除基础库之外的模块（如：web、webflux、data-jdbc、data-redis），可以不引入`yue-library-base`。

|示例版本号								|版本号区别								|
|--										|--										|
|`j8.2.x`								|基于Java 8的2.x.x版本					|
|`j11.2.x`								|基于Java 11的2.x.x版本					|
|`Finchley.x`、`Greenwich.x`、`2.1.x`	|历史版本，具体区分请查看历史版本文档		|

[👉点击查看pom.xml依赖](https://gitee.com/yl-yue/yue-library/blob/master/pom.xml)

## 2.4.1【规划中】
## 2.4.0【即将发布】
### 新特性
### Bug修复
### Maven仓库实际发布版本号
`j8.2.4.0`、`j11.2.4.0`

[**关键pom.xml依赖：**](https://gitee.com/yl-yue/yue-library/blob/j11.2.4.0/pom.xml)

|依赖					|版本		|
|--						|--			|
|spring-boot			|2.4.3		|
|spring-cloud			|2020.0.2	|
|spring-cloud-alibaba	|2021.1		|
|hutool					|5.6.3		|
|fastjson				|1.2.76		|

## 2.3.3【2021-05-30】
2.3.3主要为bug修复与安全加固版本，并优化了大量文档细节

### 新特性
- 【base】校验框架提供静态方法`Validator.getValidatorAndSetParam(Object param)`获取参数校验器，无需bean注入
- 【base】校验框架实现分组校验与提供默认分组`ValidationGroups`
- 【base】新增`@CarDrivingLicence`、`@CarVin`、`@CreditCode`、`@ZipCode`四个校验注解
- 【web】迁移RequestParamUtils实现至ServletUtils，并优化参数获取方式
- 【web】优化ServletUtils内部实现，移除multipart相关类改用hutool提供
- 【jdbc】从**2.3.3**开始使用（强依赖）druid进行连接池管理与SQL解析

### Bug修复
- 【base】移除actuator配置
- 【web】修复异步线程装饰器在开启ServletAsyncContext时，接口响应被无故追加404异常 [#I3HTAW](https://gitee.com/yl-yue/yue-library/issues/I3HTAW)

### Maven仓库实际发布版本号
`j8.2.3.3`、`j11.2.3.3`

[**关键pom.xml依赖：**](https://gitee.com/yl-yue/yue-library/blob/j11.2.3.3/pom.xml)

|依赖					|版本			|
|--						|--				|
|spring-boot			|2.3.10.RELEASE	|
|spring-cloud			|Hoxton.SR11	|
|spring-cloud-alibaba	|2.2.5.RELEASE	|
|hutool					|5.6.3			|
|fastjson				|1.2.76			|

## 2.3.2【2021-04-17】
此版本重点实现：**密钥交换加解密**、**增强Bean转换能力**、**JDBC新增Elasticsearch-SQL、达梦、PostgreSQL方言**。

### 新特性
- 【base】ParamUtils提示优化，添加错误原因
- 【base】添加JSONListConverter类型转换器从而支持`List<JSONObject>`类型处理（JDBC实体数据库查询映射时JSONArray格式文本数据不支持映射成`List<JSONObject>`）
- 【base】优化fastjson bean转换的jsonstr识别方式
- 【base】增强DateUtils与规范UUID工具类为IdUtils并优化IdUtils实现
- 【base】增强fastjson JavaBean转换能力，支持Character类型
- 【base】MapUtils增强值提取，支持list根据key提取map提取值支持map、fastjson [pulls !17](https://gitee.com/yl-yue/yue-library/pulls/17)
- 【crypto】新增重磅特性-密钥交换加密：支持`@RequestDecrypt`注解实现请求自动解密
- 【crypto】新增重磅特性-密钥交换加密：支持`@ResponseEncrypt`注解实现响应内容加密
- 【crypto】密钥交换加密：默认提供本地Map与Redis两种交换密钥存储方案
- 【crypto】密钥交换加密：`@RequestDecrypt`与`@ResponseEncrypt`注解支持使用交换密钥加密或自定义密钥等特性
- 【web】修复ApiVersion注解minimumVersion值等于的情况下410
- 【web】优化响应结果处理器在标准HTTP状态码时的空值处理
- 【web】新增ServletUtils.getAuthToken()方法，获取请求中的OAuth2 Token
- 【webflux】修复ApiVersion注解minimumVersion值等于的情况下410
- 【jdbc】对jdbc方言实现进行完善与优化，新增Elasticsearch-SQL、达梦、PostgreSQL方言
- 【jdbc】db.queryForObject 自动识别Bean类型与简单类型
- 【jdbc】参数美化增强支持JSONArray数据类型与`List<JSONObject>`数据类型
- 【jdbc】优化多行查询结果转换为单行查询结果实现
- 【jdbc】所有mappedClass查询方法自动识别所需RowMapper类型，实现JavaBean、map、基本类型结果自动匹配
- 【jdbc】规范内部部分常量命名与移除分页中不优雅的泛型实例PageTVO
- 【jdbc】增强自动方言识别，根据驱动类自动识别所需方言类型
- 【jdbc】默认Db Bean实现根据不同驱动类型，使用对应方言配置
- 【jdbc】优化DAO实现，抽象基础DAO
- 【jdbc】优化所有jdbc方法注释，描述更简洁，表达更清晰，注释更规范
- 【jdbc】删除早期存在的部分过时方法
- 【es】支持配置ConnectTimeout与SocketTimeout，并调大各自默认值为25与15秒

### Bug修复
- 【base】修复fastjson JavaBean转换BUG [#3688](https://github.com/alibaba/fastjson/pull/3688)
- 【jdbc】修复isDataSize()方法可能因为数据库存在多行数据，而返回false的隐患
- 【jdbc】修复因错误测试而删除的参数类型美化（现已支持：Character、JSONObject、LocalDateTime进行特殊转换处理与布尔值映射识别）

### Maven仓库实际发布版本号
`j8.2.3.2`、`j11.2.3.2`

[**关键pom.xml依赖：**](https://gitee.com/yl-yue/yue-library/blob/j11.2.3.2/pom.xml)

|依赖					|版本			|
|--						|--				|
|spring-boot			|2.3.8.RELEASE	|
|spring-cloud			|Hoxton.SR10	|
|spring-cloud-alibaba	|2.2.5.RELEASE	|
|hutool					|5.6.3			|
|fastjson				|1.2.76			|

## 2.3.1【2021-02-18】
### 新特性
- 【all】规范Redis、异步线程池枚举命名
- 【jdbc】DAO中新增基于业务键的删、改、查方法，并建议使用：可避免主键ID被遍历风险
- 【jdbc】优化delete方法为行数确认安全删除机制
- 【jdbc】对依赖于主键ID作为唯一键进行删、改、查的方法添加有序主键可遍历安全风险提示（可能存在数据越权行为），并推荐使用业务唯一键
- 【jdbc】默认开启动态数据源的sql打印
- 【jdbc】全面接入参数类型美化（现已支持：Character、JSONObject、LocalDateTime进行特殊转换处理）
- 【jdbc】实现布尔类型识别与is命名规约识别
- 【jdbc】新增支持识别单行数据进行简单数据类型映射（如：String）
- 【web】新增支持使用FastJson做HTTP消息转换器时按照属性声明顺序进行序列化排序
- 【web】更改HTTP消息转换器默认配置将 Null Boolean 输出为 false

### Bug修复
- 【web】解决@RequestMapping中指定produces为xml类型时，JavaBean转换会去解析xml内容BUG [#I2ALJW](https://gitee.com/yl-yue/yue-library/issues/I2ALJW)
- 【web】解决获取request、response空指针，改为返回null
- 【jdbc】优化DbBase与Dialect相互依赖设计，实现Db.clone()深度克隆并解决DbBase与Dialect相互依赖造成的成员变量（JdbcProperties）初始化null异常
- 【jdbc】解决spring-cloud-stream启动时循环调用DbBase的equals()方法错误
- 【jdbc】修改jdbcQueryBoolean返回类型错误
- 【jdbc】修复JdbcProperties默认未注入问题

### Maven仓库实际发布版本号
`j8.2.3.1`、`j11.2.3.1`

[**关键pom.xml依赖：**](https://gitee.com/yl-yue/yue-library/blob/j11.2.3.1/pom.xml)

|依赖					|版本			|
|--						|--				|
|spring-boot			|2.3.5.RELEASE	|
|spring-cloud			|Hoxton.SR9		|
|spring-cloud-alibaba	|2.2.3.RELEASE	|
|hutool					|5.4.4			|
|fastjson				|1.2.74			|

## 2.3.0【2021-01-10】
### 新特性
**data-jdbc进行了重大优化，如下：**
1. 替换Db JavaBean转换方案，性能提升约300%+
2. 优化Convert JavaBean转换性能
3. 优化Convert异常提示，划分日志等级
4. 重载驼峰转换方法到Convert类

**JDBC实体类映射方案已重构，废弃了Spring的转换器方案，这是一次底层改动。我已测试了如下场景成功支持：**
1. 驼峰转换（自动映射、自动识别boolean is命名）
2. JSONObject、JSONArray解析支持
3. 支持实体类多set方法存在

**新增如下RowMapper：**
1. BeanPropertyRowMapper，JavaBean映射两倍有余Spring原生的性能，支持更多类型映射，支持 JSONField 注解
2. ColumnMapRowMapper，转换 Map 为 fastjson 的 JSONObject

- 【base】优化异步线程池默认配置，完美解决异步上下文遇到的所有问题（包括并发模式与Servlet提前关闭导致的子线程获取不到参数异常）
- 【base】全局统一异常处理新增FeignException处理，提供异常消息格式化构造
- 【base】增强ExceptionUtils，提供多种堆栈打印方式与获取方式，并优化异常拦截返回错误内容
- 【base】Java全局网络代理配置，允许使用`,`分割（如：`localhost|127.*` `localhost,127.*`）
- 【base】新增R.errorPromptFormat()方法
- 【base】提供最外层HTTP状态码503（停机维护）约定
- 【base】新增JsonList驼峰等属性命名策略多个重载工具方法
- 【base】增强validator校验框架，支持将`@Valid`注解添加到POJO类上
- 【jdbc】规范排序、比较、预期、枚举，创建比较code定义命名规范类
- 【jdbc】对BaseDO进行驼峰命名规范
- 【jdbc】增强Db WHERE SQL对数组类型的处理

### Bug修复
- 【base】解决@Chinese注解中文汉字校验，value为空时的NullPointerException
- 【web】HttpMessageConverter选用fastjson时，解析非json格式响应体错误 [#I2ALJW](https://gitee.com/yl-yue/yue-library/issues/I2ALJW)

### Maven仓库实际发布版本号
`j8.2.3.0`、`j11.2.3.0`

[**关键pom.xml依赖：**](https://gitee.com/yl-yue/yue-library/blob/j11.2.3.0/pom.xml)

|依赖					|版本			|
|--						|--				|
|spring-boot			|2.3.5.RELEASE	|
|spring-cloud			|Hoxton.SR9		|
|spring-cloud-alibaba	|2.2.3.RELEASE	|
|hutool					|5.4.4			|
|fastjson				|1.2.74			|

## 2.2.0【2020-10-09】
### Maven仓库实际发布版本号
`j8.2.2.0`、`j11.2.2.0`

解释：j8对应Java 8，详情查看版本区别说明

### 新特性
此版本主要实现`HttpServletRequest`输入流可反复读取，重点解决全局异常捕获（包括过滤器中的异常）问题，规范RESTful处理让body中的code值与http状态码保持一致。此版本关键依赖定义如下：

|依赖					|版本			|
|--						|--				|
|spring-boot			|2.2.5.RELEASE	|
|spring-cloud			|Hoxton.SR3		|
|spring-cloud-alibaba	|2.2.1.RELEASE	|
|hutool					|5.3.10			|
|fastjson				|1.2.73			|

[点击查看更多依赖版本定义](https://gitee.com/yl-yue/yue-library/blob/master/pom.xml)

- 【base】标准了`Result`构建与使用（废弃~~ResultInfo~~类，添加**R**类进行`Result`构建），错误提示使用：`R.errorPrompt("用户名或密码错误")`、`R.errorPrompt("验证码错误")` 等
- 【base】Java全局网络代理封装，简化配置与操作（开启全局代理、获取代理配置、临时设置全局代理、取消全局代理等）
- 【web】提供`RepeatedlyReadServletRequestWrapper`过滤器，传递输入流可反复读取的`HttpServletRequest`
- 【web】解决全局异常捕获与HTTP状态码同步，并捕获404、405等异常
- 【web】提供Array数据结构参数解析器`ArrayArgumentResolver`
- 【webflux】解决全局异常捕获与HTTP状态码同步，并捕获404、405等异常
- 【jdbc】提供jdbc逻辑删除数据剔除查询
- 【jdbc】支持JavaBean中存在多个setMethod方法
- 【jdbc】在JavaBean中调用setMethod方法设置JSONObject类型value时进行额外解析处理
- 【jdbc】克隆Db支持
- 【jdbc】JdbcTemplate支持，提供`queryXX`单参数类型方法
- 【es】新增es模块，提供es rest便捷配置与es sql配置

### Bug修复
- 【redis】hashMap 序列化采用Object

## 2.1.0【2020-08-08】
### 新特性
基于全新的spring-cloud-alibaba体系封装改造，拆分独立的OAuth认证体系，对webmvc、webflux分开支持。依赖定义如下：

|依赖					|版本			|
|--						|--				|
|spring-boot			|2.1.10.RELEASE	|
|spring-cloud-alibaba	|2.1.2.RELEASE	|
|spring-cloud			|Greenwich.SR5	|

[点击查看更多依赖版本定义](https://gitee.com/yl-yue/yue-library/blob/master/pom.xml)

- 【base】提供`yml`默认配置支持，一键解决常规坑点困扰（如：时间格式化、可执行SQL打印、接口定义打印等），更适合国内标准
- 【base】`@ApiVersion` 注解可优雅的实现接口版本控制，只为更好的RESTful
- 【web】实用的参数解析器（解决参数获取困扰，不再区分Query传参与Body传参，Request请求参数智能解析），并提供`RequestParamUtils`工具类适用于各种环境下获取请求参数
- 【web】HTTP消息转换器增强，fastjson与jackson一键切换
- 【redis】规范redis包名标准，分离OAuth认证体系
- 【redis】提供可配置的Redis存储对象序列/反序列化器
- 【jdbc】遵守Java开发手册命名规约，Service/DAO 层方法命名规约，优化如获取单个对象采用 get 做前缀
- 【jdbc】提供友好的方言支持，为适配国产化数据库打下基础
- 【jdbc】提供业务主键支持
- 【jdbc】提供字段命名策略支持
- 【jdbc】提供逻辑删除支持
- 【jdbc】提供获得表元数据支持

### Bug修复
- 【jdbc】批量插入容易报错问题

## Finchley.SR4.1【2019-10-18】
### 历史版本命名说明
yue-library的版本命名方式，采用SpringCloud版本名作为前缀，然后以.1、.2、.3...这种形式，目的是为了方便区分基础依赖版本。<br>

|JDK版本|JDK说明												|SpringCloud版本|版本说明																			|
|--		|--														|--				|--																					|
|JDK8	|LTS（Oracle长期支持版本），目前大部分互联网公司采用版本|Finchley		|JDK8兼容版本，每次新特性发布都会进行一次全面的兼容适配与测试，以供JDK8用户稳定使用	|
|JDK11	|LTS（Oracle长期支持版本），作者采用版本				|Greenwich		|JDK11推荐版本，提供更快速的迭代与反馈												|

### 新特性
- JDK8版本，基于 `Greenwich.SR2.1` 做兼容适配。<font color=red>继此版本之后将采用双版本同时发布模式</font>
- 版本更新日志同 [Greenwich.SR2.1](#GreenwichSR21【2019-10-15】)

## Greenwich.SR2.1【2019-10-15】
### 新特性
- 完善包注释与类注释，提供更舒爽的javadoc，完善项目文档，提供更多的代码示例与使用说明。同时优化项目部分代码结构与紧急修复redis User类
- 合并统一异常处理类继承结构，更改redis常量配置属性为可配置属性
- 【base】增强字段校验器 `Validator` 类型自动识别与错误参数提示，更加强大方便好用
- 【base】 `Result` 新增方法 `public <D> List<D> dataToList(Class<D> clazz) {`
- 【base】 `Result` 新增方法 `public <D> D dataToObject(Class<D> clazz) {`
- 【base】 `Result` 新增方法 `public <D> D dataToJavaBean(Class<D> clazz) {`
- 【samples】建立示例项目组结构，添加简单的SpringBoot示例项目模版 `template-simple`，用于直接上手开发

### Bug修复
- 【redis】 User 类所依赖的 ConstantProperties 配置未启用，导致的启动异常

## Finchley.SR4【2019-09-16】
### 新特性
- 你们要的JDK8版本来了，基于Greenwich.SR2做兼容适配
- 版本升级注意：`lombok` 版本和 `IDE lombok` 版本一致，此版本完善了之前遗留下来的规范，导致部分类与方法失效，但所有失效类与方法都提供了指引说明
- 升级基础依赖：`SpringCloud:Finchley.SR4` `SpringBoot:2.0.9.RELEASE` `hutool:4.6.2` `fastjson:1.2.59`
- 新增 `yue-library-base-crypto` 模块，基于hutool提供更完善的加解密、签名等操作。提供自动配置全局单例
- 新增 `yue-library-pay` 模块，基于pay-java-parent进行二次封装，**让你真正做到一行代码实现支付聚合**，让你可以不用理解支付怎么对接，只需要专注你的业务
- 优化serialVersionUID
- 完善文档-更详细的教程，更多的细节介绍和建议。
- 【base】`JSONObject` 参数解析器 `@PostMapping public Result<?> post(JSONObject paramJson) {`
- 【base】`ApplicationContextUtils` 普通类操作Spring上下文，更方面的SpringBean操作
- 【base】`ServletUtils` 对Servlet操作的增强，迁入原有工具类 `HttpUtils` `CookieUtils`

## Greenwich.SR2【2019-09-03】
### 新特性
- 版本升级注意：`lombok` 版本和 `IDE lombok` 版本一致，此版本完善了之前遗留下来的规范，导致部分类与方法失效，但所有失效类与方法都提供了指引说明
- 升级基础依赖：`SpringCloud:Greenwich.SR2` `SpringBoot:2.1.6.RELEASE` `hutool:4.6.2` `fastjson:1.2.59`
- 新增 `yue-library-base-crypto` 模块，基于hutool提供更完善的加解密、签名等操作。提供自动配置全局单例
- 新增 `yue-library-pay` 模块，基于pay-java-parent进行二次封装，**让你真正做到一行代码实现支付聚合**，让你可以不用理解支付怎么对接，只需要专注你的业务
- 优化serialVersionUID
- 完善文档-更详细的教程，更多的细节介绍和建议。
- 【base】`JSONObject` 参数解析器 `@PostMapping public Result<?> post(JSONObject paramJson) {`
- 【base】`ApplicationContextUtils` 普通类操作Spring上下文，更方面的SpringBean操作
- 【base】`ServletUtils` 对Servlet操作的增强，迁入原有工具类 `HttpUtils` `CookieUtils`

### Bug修复

## Greenwich.SR1.2【2019-07-27】
### 新特性
- 【base】 `Convert` 强大的类型转换器，提供简单全面的类型转换，无与伦比的性能体验，无脑的使用他吧

### 变更
- 【yue】全类标准化：规划版本、优化细节、删除部分失效方法（为强迫症代言）
- 【yue】升级`hutool`依赖为`4.5.16`
- 【base】规划 `Result` ，移除部分方法，优化提示，建立强标准。只为更好的 `RESTful`
- 【base】优化部分异常类，只为更好的 `RESTful`
- 【base】 `ObjectUtils.to*` 增强，关联类型转换器 `Convert`

### Bug修复

## Greenwich.SR1.SR1【2019-07-22】
### 重大升级
- **校验框架**：提供强大而全面的校验框架，支持多种校验方式，国内常用校验一网打尽，友好的`RESTful`风格提示，更贴切国内使用
- **异步线程池**：提供默认异步线程池（可配置），`@Async` 共用父线程上下文环境，异步执行任务时不丢失token
- **全局统一异常处理**：默认全局统一异常处理（不再需要手动继承）

### 新特性
- 【base】 `ParamUtils.paramValidate()` 必传参数增加空字符串校验
- 【base】 `yue.constant` 常量属性增加验证码超时时间
- 【redis】新增微信小程序登录方式，用户操作一网打尽

### 变更
- 【base】降低fastjson依赖，以寻求解决JSONObject.toJavaObject问题
- 【redis】标准化微信开发平台登录与QQ登录

### Bug修复
- 【JDBC】DBDAO继承问题修复

## Greenwich.SR1【2019-06-03】
### 重大升级
- 升级JDK到长期支持版本`Java SE 11.0.3 (LTS)`
- 升级SpringCloud版本到`Greenwich.SR1`
- 升级各项推荐库版本

### 新特性
- 【base】jpush 极光推送 依赖支持
- 【base】IPO校验框架引入、新增`| @IdCard | 身份证校验 || @Cellphone | 手机号校验 |`
- 【base】增强`toJSONObject()`
- 【jdbc】DB类queryForJson方法名变更
- 【jdbc】DB类新增update重载方法
- 【jdbc】新增BaseDO类（DO基础类，对应关系型数据库设计标准）
- 【jdbc】DB类新增`isDataSize`方法，用于确认查询结果
- 【redis】User优化
- 【redis】封装login、logout
- 【redis】封装第三方登录特性（如：微信、QQ）
- 【redis】封装分布式图片验证码
- 【redis】封装基础DAO（DBDAO,DBTDAO）

### 变更
- 【base】ListUtils全面基于JSONObject操作
- 【base】`ParamUtils.paramValidate()`错误提示增强

### Bug修复
- 【jdbc】DB类分页查询结果PageVO无数据

## Finchley.SR2.SR1【2019-02-22】
### 介绍
　　经过漫长的迭代后，总于迎来了第一个开源版本，并同步maven中央仓库。

### 历史
　　2016初期此项目仅为一个工具库，封装各种常用特性<br>
　　2017年迎来一次版本大迭代，迎合SpringBoot<br>
　　2018年建立标准重构此项目，全面适配SpringCloud。<br>
　　2019年初项目进行开源迭代直到正式发布第一个Maven版本：[Finchley.SR2.SR1](https://search.maven.org/artifact/ai.ylyue/yue-library-dependencies/Finchley.SR2.SR1/pom)

### 版本变化
1. 整理各种基础工具类，加入常用库
2. pom标准建立
3. 标准化javadoc