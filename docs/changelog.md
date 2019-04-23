# Changelog
---
## Greenwich.SR1【2019-04-22】
### 重大升级
- 升级JDK到长期支持版本`Java SE 11.0.3 (LTS)`
- 升级SpringCloud版本到`Greenwich.SR1`
- 升级各项推荐库版本

### 新特性
- jpush 极光推送 依赖支持
- 【jdbc】DB类queryForJson方法名变更
- 【jdbc】DB类新增update重载方法

### 变更
- 【base】ListUtils全面基于JSONObject操作

### Bug修复
- 【jdbc】DB类分页查询结果PageVO无数据

## Finchley.SR2.SR1【2019-02-22】
### 介绍
　　经过漫长的迭代后，总于迎来了第一个开源版本，并同步maven中央仓库。
### 历史
　　2016初期此项目仅为一个工具库，封装各种常用特性
　　2017年迎来一次版本大迭代，迎合SpringBoot
　　2018年建立标准重构此项目，全面适配SpringCloud。
　　2019年初项目进行开源迭代直到正式发布第一个Maven版本：Finchley.SR2.SR1
### 版本变化
1. 整理各种基础工具类，加入常用库
2. pom标准建立
3. 标准化javadoc