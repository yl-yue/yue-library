package ai.yue.library.base.convert.converter;

import ai.yue.library.base.convert.Convert;
import com.alibaba.fastjson2.JSONObject;
import cn.hutool.v7.core.convert.AbstractConverter;
import cn.hutool.v7.core.reflect.FieldUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * JSONList类型转换器
 *
 * @author	ylyue
 * @since	2019年7月25日
 */
public class JSONListConverter extends AbstractConverter {

	private static final long serialVersionUID = -4664287699033731805L;

	private List<JSONObject> registryType;
	private ArrayList<JSONObject> registryType2;

	@Override
	protected List<JSONObject> convertInternal(Class<?> targetClass, Object value) {
		return Convert.toJsonList(Convert.toJSONArray(value));
	}

	public static List<Type> getRegistryTypes() {
		Type registryType = FieldUtil.getField(JSONListConverter.class, "registryType").getGenericType();
		Type registryType2 = FieldUtil.getField(JSONListConverter.class, "registryType2").getGenericType();
		List<Type> registryTypes = new ArrayList<>();
		registryTypes.add(registryType);
		registryTypes.add(registryType2);
		return registryTypes;
	}

}
