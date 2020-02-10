## Restful
　　`ai.yue.library.base.view.Result<T>` 定义为最外层响应对象，`ai.yue.library.base.view.ResultInfo` 定义为工具类可便捷返回 Restful 风格API结果。

### 示例
**代码如下：**
```java
@PostMapping("/valid")
public Result<?> valid(@Valid ValidationIPO validationIPO) {
	return ResultInfo.success(validationIPO);
}
```

**响应结果如下图所示**：多了一层最外层响应对象

![请求参数与响应结果](Restful_files/1.jpg)

## 接口质检标准（可选参考）
　　[接口质检标准参考文档](规约/接口质检标准.md)