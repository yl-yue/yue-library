package ai.yue.library.data.jdbc.vo;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author	ylyue
 * @since	2018年7月18日
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PageTVO<T> {
	
	/** 总数 */
	Long count;
	/** 分页列表数据 */
	List<T> data;
	
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		json.put("count", count);
		json.put("data", data);
		return json;
	}
	
	/**
	 * 将分页结果转换成最外层响应对象
	 * 
	 * @return HTTP请求，最外层响应对象
	 */
	public Result<List<T>> toResult() {
		return ResultInfo.success(data, count);
	}
	
}
