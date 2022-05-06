package ai.yue.library.test.spring.jdbc.ipo;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

/**
 * 经过方言处理的分页查询参数，用于SQL分页查询
 * 
 * @author	ylyue
 * @since	2018年4月13日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {

	// ---------- 关键字定义 ----------

	/** 关键字-LIMIT */
	public static final String KEYWORD_LIMIT = "LIMIT";
	/** 关键字-OFFSET */
	public static final String KEYWORD_OFFSET = "OFFSET";

	// ---------- 命名占位符定义 ----------

	/** 命名占位符-分页查询起始行 */
	public static final String NAMED_PARAMETER_PAGE = ":page";
	/** 命名占位符-分页查询限制数量 */
	public static final String NAMED_PARAMETER_LIMIT = ":limit";

	// ---------- 参数名定义 ----------

	/** 参数名-page */
	public static final String PARAM_NAME_PAGE = "page";
	/** 参数名-limit */
	public static final String PARAM_NAME_LIMIT = "limit";
	
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
		paramJson.put(PARAM_NAME_PAGE, this.page);
		paramJson.put(PARAM_NAME_LIMIT, this.limit);
		if (null != conditions && !conditions.isEmpty()) {
			paramJson.putAll(conditions);
		}
		
		return paramJson;
	}
	
}
