package ai.yue.library.base.convert.converter;

import com.alibaba.fastjson.JSONArray;

import ai.yue.library.base.convert.Convert;
import cn.hutool.core.convert.AbstractConverter;

/**
 * JSONArray类型转换器
 * 
 * @author	ylyue
 * @since	2019年7月25日
 */
public class JSONArrayConverter extends AbstractConverter<JSONArray> {

	private static final long serialVersionUID = 1L;

	@Override
	protected JSONArray convertInternal(Object value) {
		return Convert.toJSONArray(value);
	}

}
