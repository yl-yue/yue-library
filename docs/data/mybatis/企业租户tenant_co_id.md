## 企业租户 `tenant_co_id` <!-- {docsify-ignore} -->
企业租户 `tenant_co_id` 为 data-mybatis 框架内置的行级实现方案，采用 `tenant_co_id` 字段进行数据隔离，自动改写SQL，追加 `tenant_co_id` 条件。
- `tenant_co_id`是自动追加的，如：`AND table.tenant_co_id = 'co'`
- 追加时将判断表中是否存在 `tenant_co_id` 字段
  - `TenantCoIdInterceptor`插件会自动扫描所有 `@Mapper Bean`，如果**实体**中没有 `tenantCoId` 字段，则忽略此表
  - 当自动忽略不生效时，也可以通过配置 `yue.data.mybatis.ignore-table-tenant-cos` 主动忽略某张表
  - 自行编写的 sql，存在 **tenant_co** `=`、`<>`、`!=`、`>`、`IS`、`NOT`、`INT` 条件时，可绕过 `tenant_co_id = xxx` 条件追加拦截器

### `tenantCoId`实体类型与DDL字段类型声明参考
实体类
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "table_example_standard", autoResultMap = true)
public class TableExampleStandard extends BaseEntity {

	private static final long serialVersionUID = 6404495051119680239L;

	@TableField(fill = FieldFill.INSERT)
	String tenantCoId;
	@TableField(fill = FieldFill.INSERT)
	String tenantSysId;

}
```

MySQL DDL
```sql
CREATE TABLE `table_example` (
  `tenant_co_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业租户 Id：二级租户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='建表规范示例：提供基础字段规范';
```

### `tenant_co_id`数据填充与自动条件追加
```java
@Bean
public AuditDataProvider auditDataProvider() {
	return new AuditDataProvider() {
		@Override
		public String getTenantCoId() {
			return null;
		}
	};
}
```

1. 在新增数据时自动填充数据，详解见：[👉数据审计](data/mybatis/数据审计.md)
    - 同样支持自动跳过，实体类中没有声明时，将不会填充此字段
2. 在查询或更新数据时会自动追加条件`tenant_co_id = xxx`，进行租户数据过滤。
    - 同样支持自动跳过，实体类中没有声明时，将不会追加此字段
    - MyBatis XML中的自定义SQL，若是写了`AND table.tenant_co_id = 'xxx'`条件，也会执行跳过追加逻辑
    - 自行编写的 sql，存在 **tenant_co_id** `=`、`<>`、`IS`、`NOT`、`INT`、`>` 条件时，可绕过 `tenant_co_id = xxx` 条件追加拦截器
3. `tenant_co`过滤SQL示例：
> - 删除：`DELETE FROM user WHERE id = 1` **` AND tenant_co_id = 'xxx'`**
> - 修改：`UPDATE user SET name = ‘李四’ WHERE name = ‘张三’` **` AND tenant_co_id = 'xxx'`**
> - 查询：`SELECT id,name,delete_time FROM user` **` WHERE tenant_co_id = 'xxx'`**

### 为没有`tenant_co_id`字段的表，执行跳过数据填充与条件追加
- `TenantCoInterceptor`插件会自动扫描所有 `@Mapper Bean`，如果**实体**中没有 `tenantCoId` 字段，则忽略此表
- 当自动忽略不生效时，也可以通过配置 `yue.data.mybatis.ignore-table-tenant-co-ids` 主动忽略某张表

```yml
yue:
  data:
    mybatis:
      enable-tenant-co-id: true                        # 启用企业租户，默认：false
      ignore-table-tenant-co-ids:                      # 企业租户忽略表，需要显示声明忽略的表
        - aaa
        - bbb
```

### 按数据源忽略企业租户

多数据源场景下，如果某个数据源的表没有 `tenant_co_id` 列，可通过 `ignore-ds-tenant-co-ids` 按数据源名跳过企业租户拦截器。

> 排除粒度为数据源级别，该数据源下的所有表都将跳过企业租户条件追加。如果一个数据源对应多个物理库且仅部分库需要排除，推荐为不同物理库配置独立数据源名。

```yml
yue:
  data:
    mybatis:
      enable-tenant-co-id: true
      ignore-ds-tenant-co-ids:                         # 配置企业租户需要忽略的数据源
        - archive                                      # archive 数据源下的所有表跳过企业租户
        - log_db                                       # log_db 数据源下的所有表跳过企业租户
```
