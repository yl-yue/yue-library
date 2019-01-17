package ai.yue.library.data.jdbc.ipo;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.view.ResultErrorPrompt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author  孙金川
 * @version 创建时间：2018年4月13日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageIPO {
	
	@NonNull
	Integer page;// 当前页
	@NonNull
	Integer limit;// 每页显示的条数
	JSONObject conditions;// 查询条件（null表示无条件）
	
	/**
	 * 将分页参数转换为分页对象
	 * <p>
	 * {@linkplain JSONObject} >> {@linkplain PageIPO}
	 * @paramJSON paramJSON 最大limit值 = 200
	 * @return
	 */
	public static PageIPO parsePageIPO(JSONObject paramJSON) {
		return parsePageIPO(paramJSON, 200);
	}
	
	/**
	 * 将分页参数转换为分页对象
	 * <p>
	 * {@linkplain JSONObject} >> {@linkplain PageIPO}
	 * @paramJSON paramJSON
	 * @paramJSON maxLimit 最大限制
	 * @return
	 */
	public static PageIPO parsePageIPO(JSONObject paramJSON, int maxLimit) {
		JSONObject conditions = new JSONObject(paramJSON);
		Integer page = conditions.getInteger("page");
		Integer limit = conditions.getInteger("limit");
		if (null == page || null == limit) {
			throw new ParamException(ResultErrorPrompt.PARAM_CHECK_NOT_PASS);
		}
		conditions.remove("page");
		conditions.remove("limit");
		if (limit > maxLimit) {
			throw new ParamException(ResultErrorPrompt.MAX_LIMIT);
		}
		return PageIPO.builder().page(page).limit(limit).conditions(conditions).build();
	}
	
}
