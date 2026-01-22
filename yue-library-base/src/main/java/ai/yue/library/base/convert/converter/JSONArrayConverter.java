package ai.yue.library.base.convert.converter;

import ai.yue.library.base.convert.Convert;
import com.alibaba.fastjson2.JSONArray;
import cn.hutool.v7.core.convert.AbstractConverter;

/**
 * JSONArray类型转换器
 *
 * @author	ylyue
 * @since	2019年7月25日
 */
public class JSONArrayConverter extends AbstractConverter {

	private static final long serialVersionUID = 1L;

	@Override
	protected JSONArray convertInternal(Class<?> targetClass, Object value) {
		return Convert.toJSONArray(value);
	}

}
