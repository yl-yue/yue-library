package ai.yue.library.base.convert.converter;

import ai.yue.library.base.convert.Convert;
import cn.hutool.core.convert.AbstractConverter;
import com.alibaba.fastjson2.JSONObject;

/**
 * JSONObject类型转换器
 * 
 * @author	ylyue
 * @since	2019年7月25日
 */
public class JSONObjectConverter extends AbstractConverter<JSONObject> {

	private static final long serialVersionUID = 1L;

	@Override
	protected JSONObject convertInternal(Object value) {
		return Convert.toJSONObject(value);
	}

}
