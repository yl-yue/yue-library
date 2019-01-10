package ai.yue.library.base.ipo;

import java.util.Map;

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
	Integer page;//当前页
	@NonNull
	Integer limit;//每页显示的条数
	JSONObject conditions;//查询条件（null表示无条件）

	/**
	 * 将分页参数转换为分页对象
	 * <p>
	 * {@linkplain JSONObject} >> {@linkplain PageIPO}
	 * @param param 最大limit值 = 200
	 * @return
	 */
	public static PageIPO parsePageIPO(Map<String, Object> param) {
		return parsePageIPO(param, 200);
	}
	
	/**
	 * 将分页参数转换为分页对象
	 * <p>
	 * {@linkplain JSONObject} >> {@linkplain PageIPO}
	 * @param param
	 * @param maxLimit 最大限制
	 * @return
	 */
	public static PageIPO parsePageIPO(Map<String, Object> param, int maxLimit) {
		JSONObject conditions = new JSONObject(param);
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
	
	/**
	 * 将分页参数转换为分页对象
	 * <p>
	 * {@linkplain JSONObject} >> {@linkplain PageIPO}
	 * 
	 * @deprecated {@link #parsePageIPO(Map)}
	 * @param param 最大limit值 = 200
	 * @return
	 */
	@Deprecated
	public static PageIPO toPageIPO(Map<String, Object> param) {
		return toPageIPO(param, 200);
	}
	
	/**
	 * 将分页参数转换为分页对象
	 * <p>
	 * {@linkplain JSONObject} >> {@linkplain PageIPO}
	 * 
	 * @deprecated {@link #parsePageIPO(Map, int)
	 * @param param
	 * @param maxLimit 最大限制
	 * @return
	 */
	@Deprecated
	public static PageIPO toPageIPO(Map<String, Object> param, int maxLimit) {
		JSONObject conditions = new JSONObject(param);
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
