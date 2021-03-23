package ai.yue.library.base.convert.converter;

import ai.yue.library.base.convert.Convert;
import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * JSONList类型转换器
 *
 * @author	ylyue
 * @since	2019年7月25日
 */
public class JSONListConverter extends AbstractConverter<List<JSONObject>> {

	private static final long serialVersionUID = -4664287699033731805L;

	private List<JSONObject> genericTypeDefinition;

	@Override
	protected List<JSONObject> convertInternal(Object value) {
		return Convert.toJsonList(Convert.toJSONArray(value));
	}

	public Type getRegistryType() {
		return ReflectUtil.getField(getClass(), "genericTypeDefinition").getGenericType();
	}

}
