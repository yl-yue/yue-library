package ai.yue.library.base.convert.converter;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.convert.Convert;
import cn.hutool.core.convert.AbstractConverter;

/**
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
