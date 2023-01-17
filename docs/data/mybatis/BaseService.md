# BaseService
基础Service，继承后即可获得符合RESTful风格的内置CRUD实现

## 使用示例
**Mapper示例**
```java
@Mapper
public interface TableExampleMapper extends BaseMapper<TableExample> {

}
```

**Service示例**
```java
@Service
public class TableExampleService extends BaseService<TableExampleMapper, TableExample> {

    public void example() {
        // insert
        super.insert(new TableExample());
        super.insertOrUpdate(new TableExample());
        super.insertBatch(new ArrayList<>());
        super.insertOrUpdateBatch(new ArrayList<>());

        // delete
        super.deleteById(666666L);
        super.deleteByIds(new ArrayList<>());

        // update
        super.updateById(new TableExample());
        super.updateBatchById(new ArrayList<>());

        // query
        super.getById(666666L);
        super.listAll();
        super.page(new TableExample());

        // 更多
        ServiceImpl<TableExampleMapper, TableExample> serviceImpl = super.getServiceImpl();
        // serviceImpl.xxx
        TableExampleMapper baseMapper = super.getBaseMapper();
        // baseMapper.xxx
    }

}
```

**Controller示例**
```java
@AllArgsConstructor
@ApiVersion(1)
@RestController
@RequestMapping("/auth/{version}/tableExampleTest")
public class AuthTableExampleController {

    TableExampleService tableExampleTestService;

    /**
     * 插入数据
     */
    @PostMapping("/insert")
    public Result<?> insert(TableExampleTestIPO tableExampleTestIPO) {
        return tableExampleTestService.insert(tableExampleTestIPO);
    }

    /**
     * 分页
     */
    @GetMapping("/page")
    public Result<?> page(TableExampleTestIPO tableExampleTestIPO) {
        return tableExampleTestService.page(tableExampleTestIPO);
    }

}
```

## 源码速览
```java
public abstract class BaseService<M extends BaseMapper<T>, T extends BaseEntity> {

    @Autowired
    protected M baseMapper;
    private ServiceImpl<M, T> serviceImpl = new ServiceImpl<>();
    @SuppressWarnings("unchecked")
    protected Class<T> entityClass = (Class<T>) ClassUtil.getTypeArgument(getClass(), 1);

    @PostConstruct
    private void init() {
        ReflectUtil.setFieldValue(serviceImpl, "baseMapper", baseMapper);
        ReflectUtil.setFieldValue(serviceImpl, "entityClass", entityClass);
        ReflectUtil.setFieldValue(serviceImpl, "mapperClass", (Class<T>) ClassUtil.getTypeArgument(getClass(), 0));
    }

    /**
     * 插入数据
     *
     * @param entity 实体参数，支持实体对象、map、json
     * @return 填充后的实体
     */
    public Result<T> insert(Object entity) {
        T entityObject = Convert.toJavaBean(entity, entityClass);
        serviceImpl.save(entityObject);
        return R.success(entityObject);
    }

    /**
     * 插入数据-批量
     *
     * @param entityList 实体集合
     * @return 是否成功
     */
    public Result<Boolean> insertBatch(Collection<?> entityList) {
        ArrayList<T> entityObjectList = new ArrayList<>(entityList.size());
        for (Object entity : entityList) {
            T entityObject = Convert.toJavaBean(entity, entityClass);
            entityObjectList.add(entityObject);
        }
        boolean success = serviceImpl.saveBatch(entityObjectList);
        return R.success(success);
    }

    /**
     * 插入或更新数据
     * <p><code>@TableId</code> 注解存在，则更新，否则插入</p>
     *
     * @param entity 实体参数，支持实体对象、map、json
     * @return 填充后的实体
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<T> insertOrUpdate(Object entity) {
        T entityObject = Convert.toJavaBean(entity, entityClass);
        serviceImpl.saveOrUpdate(entityObject);
        return R.success(entityObject);
    }

    /**
     * 插入或更新数据-批量
     * <p><code>@TableId</code> 注解存在，则更新，否则插入</p>
     *
     * @param entityList 实体集合
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> insertOrUpdateBatch(Collection<?> entityList) {
        ArrayList<T> entityObjectList = new ArrayList<>(entityList.size());
        for (Object entity : entityList) {
            T entityObject = Convert.toJavaBean(entity, entityClass);
            entityObjectList.add(entityObject);
        }
        boolean success = serviceImpl.saveOrUpdateBatch(entityObjectList);
        return R.success(success);
    }

    /**
     * 删除-ById
     *
     * @param id 主键id
     * @return 是否成功
     */
    public Result<Boolean> deleteById(Long id) {
        boolean success = serviceImpl.removeById(id);
        return R.success(success);
    }

    /**
     * 批量删除-ById
     *
     * @param list 主键ID或实体列表
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> deleteByIds(Collection<?> list) {
        boolean success = serviceImpl.removeByIds(list);
        return R.success(success);
    }

    /**
     * 更新-ById
     *
     * @param entity 实体参数，支持实体对象、map、json
     * @return 是否成功
     */
    public Result<Boolean> updateById(Object entity) {
        T entityObject = Convert.toJavaBean(entity, entityClass);
        boolean success = serviceImpl.updateById(entityObject);
        return R.success(success);
    }

    /**
     * 批量更新-ById
     *
     * @param entityList 实体集合
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateBatchById(Collection<?> entityList) {
        ArrayList<T> entityObjectList = new ArrayList<>(entityList.size());
        for (Object entity : entityList) {
            T entityObject = Convert.toJavaBean(entity, entityClass);
            entityObjectList.add(entityObject);
        }
        boolean success = serviceImpl.updateBatchById(entityObjectList);
        return R.success(success);
    }

    /**
     * 单个-ById
     *
     * @param id 主键id
     * @return 实体
     */
    public Result<T> getById(Long id) {
        T entity = serviceImpl.getById(id);
        return R.success(entity);
    }

    /**
     * 列表-全部
     *
     * @return 实体集合
     */
    public Result<List<T>> listAll() {
        return R.success(serviceImpl.list());
    }

    /**
     * 分页
     * <p>分页能力只在HttpServletRequest环境下生效，webflux、grpc等web项目，请自行调用PageHelper.startPage()方法，开启分页</p>
     *
     * @param entity 实体参数，支持实体对象、map、json
     * @return 分页实体集合
     */
    public Result<PageInfo<T>> page(Object entity) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            Object request = ReflectUtil.invoke(requestAttributes, "getRequest");
            PageHelper.startPage(request);
        } else if (PageHelper.getLocalPage() == null) {
            log.error("BaseService.page()方法，分页能力只在HttpServletRequest下生效，如仍需使用，请自行调用PageHelper.startPage()方法，开启分页。");
        }

        T entityObject = Convert.toJavaBean(entity, entityClass);
        QueryWrapper<T> queryWrapper = new QueryWrapper<>(entityObject);
        List<T> list = serviceImpl.list(queryWrapper);
        return R.success(PageInfo.of(list));
    }

}
```

[👉点击前往源码仓库查看](https://gitee.com/yl-yue/yue-library/blob/master/yue-library-data-mybatis/src/main/java/ai/yue/library/data/mybatis/service/BaseService.java)
