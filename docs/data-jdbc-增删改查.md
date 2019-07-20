## DAO
`DBDAO`为 JSON 对象提供服务

`DBTDAO`为 DO 对象提供服务（DO 转 JSON 工具类：ObjectUtils.toJSONObject(Object)）

### <font color=red>DAO内置实现：</font><br>
实际中可能会用到的工具类提示：
- 对于List和JSON数组的转换，可以查看`ListUtils`工具类。

```java
// 插入数据
insert(JSONObject)
// 插入数据-批量
insertBatch(JSONObject[])
// 删除
delete(Long)
// 更新-ById
updateById(JSONObject)
// 单个
get(Long)
// 列表-全部
listAll()
// 分页
page(PageIPO)
// 分页-降序
pageDESC(PageIPO)
```

## CRUD
`DAO`类提供了默认实现，也支持我们自由扩展

### 增
简单的插入一条数据只需要传入表名和要插入的json数据即可：
```java
	/**
	 * 插入数据
	 * @param paramJson
	 * @return
	 */
	public Long insert(JSONObject paramJson) {
		return db.insert("tableName", paramJson);
	}
```
如需要批量插入一组数据，可采用`insertBatch`方法：<br>
对于List和JSON数组的转换，可以查看`ListUtils`工具类。
```java
	/**
	 * 插入数据-批量
	 * @param paramJsons
	 */
	public void insertBatch(JSONObject[] paramJsons) {
		db.insertBatch("tableName", paramJsons);
	}
```
更多方法请参阅API文档：
- 向表中插入一条数据，并自动递增 <i>sort_idx</i>
- 插入或更新

## 删
通过主键ID删除一条数据：
```java
	/**
	 * 删除
	 * @param id
	 */
	public void delete(Long id) {
		db.delete("tableName", id);
	}
```
通过表中某个字段删除多条数据：
```java
	/**
	 * 删除
	 * @param name
	 */
	public void deleteByName(String name) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("name", name);
		db.delete("tableName", paramJson);
	}
```
批量删除数据，一组条件对应一条数据，并且每组条件都采用相同的key
```java
	/**
	 * 删除-批量
	 * @param paramJsons
	 */
	public void deleteBatch(JSONObject[] paramJsons) {
		db.deleteBatch("tableName", paramJsons);
	}
```
更多方法请参阅API文档：
- 批量删除数据，指定SQL语句以创建预编译执行SQL和绑定删除参数

## 改
根据ID更新：
```java
	/**
	 * 更新-ById
	 * @param paramJson
	 */
	public void updateById(JSONObject paramJson) {
		db.updateById("tableName", paramJson);
	}
	
	// ...更多重载方法
```
根据条件更新：
```java
	/**
	 * 更新-ByName
	 * @param paramJson
	 */
	public void updateByName(JSONObject paramJson) {
		String[] conditions = {"name"};
		long updateRowsNumber = db.update("tableName", paramJson, conditions);
		int expectedValue = 1;
		db.updateAndExpectedEqual(updateRowsNumber, expectedValue);
	}
	
	// ...更多重载方法
```
更新-排序
```java
	/**
	 * 更新-排序
	 * @param id
	 * @param move
	 */
	public void updateSort(Long id, Integer move) {
		String uniqueKeys = "name";
		db.updateSort("tableName", id, move, uniqueKeys);
	}
```
更多方法请参阅API文档...

## 查
根据ID查询数据：
```java
	/**
	 * 单个
	 * @param id
	 * @return
	 */
	public JSONObject get(Long id) {
		return db.queryById("tableName", id);
	}
```
返回DO实体
```java
	/**
	 * 单个
	 * @param id
	 * @return
	 */
	public UserDO get(Long id) {
		return db.queryById("tableName", id, UserDO.class);
	}
```
查询全部：
```java
	/**
	 * 列表-全部
	 * @return
	 */
	public List<JSONObject> listAll() {
		return db.queryAll("tableName");
	}
	
	// ...更多重载方法
```
SQL查询：
```java
	/**
	 * 列表
	 * @param id
	 * @return
	 */
	public List<JSONObject> list(Long id) {
		// 1. 处理参数
		JSONObject paramJson = new JSONObject();
		paramJson.put("id", id);

		// 2. 查询SQL
		String sql = "";

		// 3. 返回结果
		return db.queryForList(sql, paramJson);
	}
	
	// ...更多重载方法
```
更多方法请参阅API文档...

### 分页
绝对条件查询分页：`PageIPO`类就是一个分页实体类，包含`page`（当前页）、`limit`（每页显示的条数）、`conditions`（查询条件）
```java
	/**
	 * 分页
	 * @param pageIPO
	 * @return
	 */
	public PageVO page(PageIPO pageIPO) {
		return db.page("tableName", pageIPO);
	}
	
	// ...更多重载方法
```
复杂分页，传入SQL查询语句：
```java
	/**
	 * 分页
	 * @param pageIPO
	 * @return
	 */
	public PageVO pageSql(PageIPO pageIPO) {
		String querySql = "";
		return db.pageSql(querySql, pageIPO);
	}
	
	// ...更多重载方法
```
更多方法请参阅API文档...