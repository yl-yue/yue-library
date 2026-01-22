## Mapper、Service、Controller示例 <!-- {docsify-ignore} -->
符合RESTful风格的CRUD规范示例

### Entity示例
数据库表映射实体

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class TableExample extends BaseEntity {

	private static final long serialVersionUID = 6404495051119680239L;

	@TableField(fill = FieldFill.INSERT)
	String tenantSys;
	String tenantCo;
	String fieldOne;
	String fieldTwo;
	String fieldThree;

}
```

### IPO示例
接口入参实体

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableExampleIPO {

    /**
     * 有序主键
     */
    @NotNull(groups = ValidationGroups.Update.class)
    private Long id;
    private String fieldOne;
    private String fieldTwo;
    private String fieldThree;
    private String tenantSys;
    private String tenantCo;

}

```

### Mapper示例
```java
@Mapper
public interface TableExampleMapper extends BaseMapper<TableExample> {

}
```

### Service示例
```java
@Service
public class TableExampleService extends ServiceImpl<TableExampleMapper, TableExample> {

    // 插入数据
    public Result<?> insert(TableExampleIPO tableExampleIPO) {
        TableExample tableExample = Convert.toJavaBean(tableExampleIPO, TableExample.class);
        super.save(tableExample);
        return R.success(tableExample);
    }

    // 删除数据
    public Result<?> deleteById() {
        boolean b = super.removeById(666666L);
        return R.success(b);
    }

    // 更新数据
    public Result<?> updateById() {
        boolean b = super.updateById(new TableExample());
        return R.success(b);
    }

    // 查询数据
    public Result<?> getById() {
        TableExample tableExample = super.getById(666666L);
        return R.success(tableExample);
    }

    // 分页
    public Result<?> page(String fieldOne, String fieldTwo) {
        PageHelper.startPage(ServletUtils.getRequest());
        List<TableExample> list = super.lambdaQuery()
                .eq(TableExample::getFieldOne, fieldOne)
                .eq(TableExample::getFieldTwo, fieldTwo)
                .list();

        PageInfo<TableExample> pageInfo = PageInfo.of(list);
        return R.success(pageInfo);
    }

    // 更多方法示例
    public void example() {
        // insert
        super.save(new TableExample());
        super.saveOrUpdate(new TableExample());
        super.saveBatch(new ArrayList<>());
        super.saveOrUpdateBatch(new ArrayList<>());

        // delete
        super.removeById(666666L);
        super.removeByIds(new ArrayList<>());

        // update
        super.updateById(new TableExample());
        super.updateBatchById(new ArrayList<>());

        // query
        super.getById(666666L);
        super.list();

        // 更多
        super.lambdaQuery();
        super.getBaseMapper();
    }

}
```

### Controller示例
```java
@AllArgsConstructor
@ApiVersion(1)
@RestController
@RequestMapping("/auth/{version}/tableExample")
public class AuthTableExampleController {

    TableExampleService tableExampleService;

    /**
     * 插入数据
     */
    @PostMapping("/insert")
    public Result<?> insert(@Valid TableExampleIPO tableExampleIPO) {
        return tableExampleService.insert(tableExampleIPO);
    }

    /**
     * 分页
     */
    @GetMapping("/page")
    public Result<?> page(String fieldOne, String fieldTwo) {
        return tableExampleService.page(fieldOne, fieldTwo);
    }

}
```

> 1. 字段映射支持下划线与驼峰自动识别转换
> 2. 实际中可能会遇到类型转换问题，可使用 [类型转换器](base/类型转换器.md) 类进行转换，支持DO、Json、List等相互转换
> 3. 更多增删改查、分页使用示例，见 [👉MyBatis-Plus](https://baomidou.com/)
