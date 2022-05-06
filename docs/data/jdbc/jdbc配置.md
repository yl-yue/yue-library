# Jdbc配置
`ai.yue.library.data.jdbc.config.properties.JdbcProperties` Jdbc可配置属性

## 默认配置预览（全局配置）
开发者根据实际情况进行修改或注释掉相关配置项，保留需要的配置即可，此处配置仅供参考，具体配置根据`spring yml`配置提示与`JdbcProperties`类属性为准。

```yml
spring:
  profiles:
    group:
      "yue": "yue-library-data-jdbc"                        # 导入yue-library提供的默认配置支持（如：开启可执行SQL打印）
    active: yue                                             # 导入yue-library提供的默认配置支持（如：开启可执行SQL打印）
	
  datasource:                                               # data-jdbc就是SpringJDBC的封装
    druid: 
      url: jdbc:mysql://localhost:3306/yue_library_test?characterEncoding=utf-8&useSSL=false
      username: root
      password: 02194096e7d840a992a2f627629a94da            # 养成良好习惯，无论任何环境不使用弱密码

yue:
  jdbc:
    enable-boolean-map-recognition: true                    # 启用布尔值映射识别
    enable-logic-delete-filter: true                        # 启用逻辑删除过滤（只对自动生成的sql生效）
    enable-field-naming-strategy-recognition: true          # 启用数据库字段命名策略识别
    database-field-naming-strategy: snake_case              # 数据库字段命名策略
    field-definition-uuid: uuid                             # 关键字段定义-无序主键名
    field-definition-sort-idx: sort_idx                     # 关键字段定义-排序
    field-definition-delete-time: delete_time               # 关键字段定义-数据删除标识
    data-encrypt-algorithm: AES                             # 缺省数据加密算法（仅当在表级未配置单独的加密算法时，以缺省值的方式生效）
    data-encrypt-key: 1234567890123456                      # 缺省数据加密密钥（仅当在表级未配置单独的加密密钥时，以缺省值的方式生效）
    data-encrypt-configs:                                   # 数据加密配置（key：表名，value：数据加密规则）
      data_encrypt:                                         # 数据库对应的表名
        data-encrypt-algorithm: AES                         # 当前表加密算法（未设置使用缺省值）
        data-encrypt-key: 1234567890123455                  # 当前表加密密钥（未设置使用缺省值）
        fieldNames:                                         # 加密字段
          - cellphone
          - password
      data_encrypt_2:                                       # 数据库对应的表名
        fieldNames:                                         # 加密字段
          - email
          - password
    data-audit-table-name-match-enum: match                 # 数据审计表名匹配方式
    data-audit-table-names:                                 # 数据审计表名
      - data_audit
      - data_audit2
    data-audit-properties:                                  # 数据审计属性
      field-name-create-user: create_user                   # 审计字段定义-创建人
      field_name_create_user_uuid: create_user_uuid         # 审计字段定义-创建人uuid
      field_name_create_time: create_time                   # 审计字段定义-创建时间
      field_name_update_user: update_user                   # 审计字段定义-更新人
      field_name_update_user_uuid: update_user_uuid         # 审计字段定义-更新人uuid
      field_name_update_time: update_time                   # 审计字段定义-更新时间
      field_name_delete_user: delete_user                   # 审计字段定义-删除人
      field_name_delete_user_uuid: delete_user_uuid         # 审计字段定义-删除人uuid
      field_name_delete_time: delete_time                   # 审计字段定义-删除时间戳
    data-fill-table-name-match-enum: exclude                # 数据填充表名匹配方式
    data-fill-table-names:                                  # 数据填充表名
      - data_fill
      - data_fill2
```

## 单个`DAO`、单个`Bean`、单个`Db`配置
适用于某个特立独行的DAO（某张表）需要特别配置如：
- 全局开启删除查询过滤，但这个DAO（这张表）不需要
- 全局开启布尔值映射识别，但这个DAO（这张表）不需要
- 全局设置了同一字段命名策略，但这个DAO（这张表）不一样
- 多数据源不同配置等

使用方式如下：
```java
public class OrgPersonRelationDAO extends AbstractRepository<BaseOrgPersonRelationDO> {

	@PostConstruct
	private void init() {
		db = db.clone();
		JdbcProperties jdbcProperties = db.getJdbcProperties();
		jdbcProperties.setEnableDeleteQueryFilter(true);
		jdbcProperties.setEnableBooleanMapRecognition(false);
		jdbcProperties.setEnableFieldNamingStrategyRecognition(false);
		jdbcProperties.setDataEncryptKey("4f5de3ab9acf4d4f94b2470e17d1beb7");
	}
	
	...
}
```

## 可配置特性介绍
- 【别处单独介绍的特性】动态数据源
- 【别处单独介绍的特性】逻辑删除
- 【别处单独介绍的特性】数据填充
- 【别处单独介绍的特性】数据审计
- 【别处单独介绍的特性】数据脱敏
- 【此处统一介绍的特性】数据库字段命名策略
- 【此处统一介绍的特性】布尔值映射识别
- 【此处统一介绍的特性】关键字段定义

### 数据库字段命名策略
- 默认为开启状态`yue.jdbc.enable-field-naming-strategy-recognition=true`启用数据库字段命名策略识别
- 默认策略为`SNAKE_CASE`命名法（俗称：下划线分割单词）
- 定义数据库字段命名策略，用于POJO与数据库字段自动映射转换，如：驼峰转下划线，下划线转驼峰

### 布尔值映射识别
- 用于识别`JSONObject paramJson`或请求参数中的布尔值
- 数字`1=true`，数字`0=fasle`
- 识别字符串值`"true", "false"`，进行美化转换
- 识别POJO规范，跟随**数据库字段命名策略（需开启）**，识别`is`前缀，并自动映射

### 关键字段定义
`ai.yue.library.data.jdbc.client.Db`类提供了很多基础的增删改查方法，包括数据填充、审计、逻辑删除等特性，而这些特性都和下面可定义的关键字段息息相关，
yue-library为了用户更好的扩展性，因此实现了这些关键字段可以自定义名称，但表达的含义与能力是一致的。

```yml
field-definition-uuid: uuid                             # 关键字段定义-无序主键名
field-definition-sort-idx: sort_idx                     # 关键字段定义-排序
field-definition-delete-time: delete_time               # 关键字段定义-数据删除标识
```
