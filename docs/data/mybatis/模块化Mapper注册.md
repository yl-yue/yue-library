## 模块化 Mapper 注册

　　yue-library-data-mybatis 启动时会自动扫描注册 Mapper 接口，支持三层扫描机制，覆盖从单体应用到多模块项目的各种场景。

### 三层扫描机制

| 层次 | 机制 | 适用场景 | 配置方式 |
|------|------|----------|----------|
| 自动扫描 | 启动类包名 + `.mapper` | 主项目的 Mapper | 无需配置，约定优于配置 |
| SPI 发现 | `META-INF/yue/mapper-packages` | 公共模块 / 独立 JAR 模块 | 在模块资源目录下创建文件 |
| 配置扩展 | `yue.data.mybatis.extra-mapper-packages` | 临时扩展 / 特殊场景 | 在 application.yml 中配置 |

三层扫描结果合并后传给 MyBatis 的 `MapperScannerConfigurer`，最终生效的扫描范围为三者的并集。

### 自动扫描（默认）

yue-library 自动获取 `@SpringBootApplication` 启动类所在包名，追加 `.mapper` 后缀作为默认扫描路径。

例如启动类在 `com.example.demo` 包下，则自动扫描 `com.example.demo.mapper` 包中的所有 Mapper 接口。

> 大多数单体项目只需将 Mapper 接口放在启动类包下的 `mapper` 子包中，无需额外配置。

### SPI 发现（推荐用于公共模块）

公共模块或独立 JAR 模块中的 Mapper 接口，推荐使用 SPI 自动发现机制，无需消费方做任何配置。

**使用方式**：在模块的 `src/main/resources/` 下创建文件 `META-INF/yue/mapper-packages`，每行写入一个 Mapper 接口所在的包路径：

```
com.example.common.module.mapper
com.example.common.module.mapper.ext
```

- 每行一个包路径，支持多行
- 以 `#` 开头的行为注释，空行自动忽略
- 多个模块各自创建该文件，yue-library 启动时会自动扫描所有 JAR 中的该文件并合并

**典型场景**：项目中有公共模块 `common-module`，其中定义了通用的 Mapper 接口。在 `common-module` 的资源目录下创建 SPI 文件后，业务项目只需引入 `common-module` 依赖，Mapper 即被自动注册，无需在业务项目中额外配置。

### 配置扩展

通过 application.yml 配置额外的 Mapper 扫描包路径，适用于临时扩展或特殊场景：

```yaml
yue:
  data:
    mybatis:
      extra-mapper-packages:
        - com.example.custom.mapper
        - com.example.legacy.dao
```

> 公共模块推荐使用 SPI 发现机制（零配置），配置扩展适用于无法修改模块资源或需要动态调整的场景。
