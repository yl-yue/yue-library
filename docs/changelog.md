# Changelog
---
## TODO LIST
### base

### jdbc
- **内置全局拦截插件**：提供全表 delete 、 update 操作智能分析阻断，预防误操作

### redis
- 权限拦截框架集成
- 增强第三方登录封装

### 其他
- 添加短信、OSS等封装
- 加入996icu license 协议、木兰协议

## 版本
yue-library的版本命名方式，采用SpringCloud版本名作为前缀，然后以.1、.2、.3...这种形式，目的是为了方便区分基础依赖版本。<br>

|JDK版本|JDK说明												|SpringCloud版本|版本说明																			|
|--		|--														|--				|--																					|
|JDK8	|LTS（Oracle长期支持版本），目前大部分互联网公司采用版本|Finchley		|JDK8兼容版本，每次新特性发布都会进行一次全面的兼容适配与测试，以供JDK8用户稳定使用	|
|JDK11	|LTS（Oracle长期支持版本），作者采用版本				|Greenwich		|JDK11推荐版本，提供更快速的迭代与反馈												|

## Greenwich.SR3【规划中】
### 新特性
- 升级系列依赖版本
- 删除失效方法

### Bug修复

## Finchley.SR4.1【2019-10-18】
### 新特性
- JDK8版本，基于 `Greenwich.SR2.1` 做兼容适配。<font color=red>继此版本之后将采用双版本同时发布模式</font>
- 版本更新日志同 [Greenwich.SR2.1](#Greenwich.SR2.1【2019-10-15】)

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
- 【base】规划 `Result` ，移除部分方法，优化提示，建立强标准。只为更好的 `Restful`
- 【base】优化部分异常类，只为更好的 `Restful`
- 【base】 `ObjectUtils.to*` 增强，关联类型转换器 `Convert`

### Bug修复

## Greenwich.SR1.SR1【2019-07-22】
### 重大升级
- **校验框架**：提供强大而全面的校验框架，支持多种校验方式，国内常用校验一网打尽，友好的`Restful`风格提示，更贴切国内使用
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