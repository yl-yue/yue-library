package ai.yue.library.data.jdbc.ipo;

import com.alibaba.fastjson.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 分页查询对象，用于SQL分页查询
 * 
 * @author	ylyue
 * @since	2018年4月13日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {
	
	/** LIMIT关键字 */
	public static final String LIMIT_KEYWORD = "LIMIT";
	
	/** 分页查询起始行命名占位符 **/
	public static final String PAGE_NAMED_PARAMETER = ":page";
	
	/** 分页查询限制数量命名占位符 */
	public static final String LIMIT_NAMED_PARAMETER = ":limit";
	
	/** page参数名 **/
	public static final String PAGE_PARAM_NAME = "page";
	
	/** limit参数名 */
	public static final String LIMIT_PARAM_NAME = "limit";
	
	/**
	 * 查询起始行，从0开始计数，包含关系
	 * <p>当前条件下，从第几行开始查询
	 */
	@NonNull
	Long page;
	
	/**
	 * 查询限制数量
	 * <p>你想要查询多少条数据
	 */
	@NonNull
	Integer limit;
	
	/**
	 * 查询条件（null表示无条件）
	 */
	JSONObject conditions;
	
	/**
	 * 转换为Db参数Json
	 * 
	 * @return paramJson
	 */
	public JSONObject toParamJson() {
		JSONObject paramJson = new JSONObject();
		paramJson.put(PAGE_PARAM_NAME, this.page);
		paramJson.put(LIMIT_PARAM_NAME, this.limit);
		if (null != conditions && !conditions.isEmpty()) {
			paramJson.putAll(conditions);
		}
		
		return paramJson;
	}
	
}
