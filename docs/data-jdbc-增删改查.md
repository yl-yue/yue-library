## 增
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
根据ID更新数据：
```java
	/**
	 * 更新-ById
	 * @param paramJson
	 */
	public void updateById(JSONObject paramJson) {
		db.updateById("tableName", paramJson);
	}
```
根据条件更新：
根据
## 查

### 分页