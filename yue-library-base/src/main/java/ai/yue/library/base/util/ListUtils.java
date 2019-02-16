package ai.yue.library.base.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.constant.MaxOrMinEnum;
import ai.yue.library.base.constant.SortEnum;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.util.ArrayUtil;

/**
 * List工具类
 * @author  孙金川
 * @version 创建时间：2017年10月27日
 */
public class ListUtils {
	
	/**
	 * List数组是否为空
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(List<?> list) {
		return null == list || list.isEmpty();
	}
	
	/**
	 * List数组不为空
	 * @param list
	 * @return
	 */
	public static boolean isNotEmpty(List<?> list) {
		return !isEmpty(list);
	}
	
	/**
	 * 数据分组
	 * <p>
	 * 将拥有相同的 key 值的JSON数据归为一组
	 * @param list	要处理的集合
	 * @param key	分组依据
	 * @return
	 */
	public static List<List<JSONObject>> grouping(List<JSONObject> list, String key) {
		List<List<JSONObject>> result = new ArrayList<>();
		toListAndDistinct(list, key).forEach(str -> {
			List<JSONObject> jsonList = new ArrayList<>();
			list.forEach(json -> {
				if (str.equals(json.getString(key))) {
					jsonList.add(json);
				}
			});
			result.add(jsonList);
		});
		
		return result;
	}
	
	/**
	 * 保留相同值
	 * @param list	循环第一层
	 * @param list2	循环第二层
	 * @return 
	 */
	public static List<String> keepSameValue(List<String> list, List<String> list2) {
		List<String> result = new ArrayList<>();
		list.forEach(str -> {
			list2.forEach(str2 -> {
				if (str.equals(str2)) {
					result.add(str);
				}
			});
		});
		return result;
	}
	
	/**
	 * 将JSON集合，合并到数组的JSON集合
	 * <p>
	 * 以条件key获得JSONObject数组中每个对象的value作为JSON对象的key，然后进行合并。<br>
	 * JSON对象key获得的值，应该是一个JSONObject对象<br>
	 * 
	 * <blockquote>示例：
     * <pre>
     *	JSONArray array = [
     * 		{
     * 			"id": 1,
     * 			"name": "name"
     * 		}
     * 	]
     * 	JSONObject json = {
     * 		1: {
     * 			"sex": "男",
     * 			"age": 18
     * 		}
     * 	}
     * 
     * 	String key = "id";
     * 		
     *	JSONArray mergeResult = merge(array, json, key);
     * 	System.out.println(mergeResult);
     * </pre>
     * 结果：
     * 		[{"id": 1, "name": "name", "sex": "男", "age": 18}]
     * </blockquote>
	 * @param array	JSONObject数组
	 * @param json	JSON对象
	 * @param key	条件
	 * @return
	 */
	public static JSONArray merge(JSONArray array, JSONObject json, String key) {
		array.forEach(arrayObj -> {
			JSONObject temp = ObjectUtils.toJSONObject(arrayObj);
			String value = temp.getString(key);
			temp.putAll(json.getJSONObject(value));
		});
		
		return array;
	}
	
	/**
	 * <h1>List合并</h1><br>
	 * 将<b> list2 </b>合并到<b> list1 </b>里面
	 * <p>
	 * @param list1		需要合并的列表
	 * @param list2		被合并的列表
	 * @param key1		list1中Map所使用的key
	 * @param key2		list2中Map所使用的key
	 */
	public static void merge(List<Map<String, Object>> list1, List<Map<String, Object>> list2, String key1, String key2) {
		list1.forEach(map1 -> {
			Object value1 = map1.get(key1);
			for (Map<String, Object> map2 : list2) {
				Object value2 = map2.get(key2);
				if (ObjectUtils.equals(value1, value2)) {
					map2.remove(key2);
					map1.putAll(map2);
					break;
				}
			}
		});
	}
	
	/**
	 * <h1>List-JSONObject集合排序</h1>
	 * 
	 * @param list 需要处理的集合
	 * @param sortKey 排序依据（JSONObject的key）
	 * @param sortEnum 排序方式
	 * @return 处理后的List集合
	 */
	public static List<JSONObject> sort(List<JSONObject> list, String sortKey, SortEnum sortEnum) {
		Collections.sort(list, new Comparator<JSONObject>() {
			public int compare(JSONObject json1, JSONObject json2) {
				var json1value = json1.get(sortKey);
				var json2value = json2.get(sortKey);
				if (sortEnum == SortEnum.升序) {
					return CompareUtil.compare(json1value, json2value, false);
				} else {
					return CompareUtil.compare(json2value, json1value, false);
				}
			}
		});
		
		return list;
	}
	
	/**
	 * <h1>List-T集合排序</h1>
	 * @param <T>
	 * @param list 需要处理的集合
	 * @param sortField 排序字段
	 * @param sortEnum 排序方式
	 * @return 处理后的List集合
	 */
	public static <T> List<T> sortT(List<T> list, String sortField, SortEnum sortEnum) {
		Collections.sort(list, new Comparator<T>() {
			public int compare(T o1, T o2) {
				JSONObject json1 = ObjectUtils.toJSONObject(o1);
				JSONObject json2 = ObjectUtils.toJSONObject(o2);
				var json1value = json1.get(sortField);
				var json2value = json2.get(sortField);
				if (sortEnum == SortEnum.升序) {
					return CompareUtil.compare(json1value, json2value, false);
				} else {
					return CompareUtil.compare(json2value, json1value, false);
				}
			}
		});
		
		return list;
	}
	
	/**
	 * 反转集合
	 * @param <T>
	 * @param list	需要处理的集合
	 * @param clazz 集合元素类型
	 * @return 反转后的List集合
	 */
	public static <T> List<T> reverse(List<T> list, Class<T> clazz) {
		return ListUtils.toList(ArrayUtil.reverse(ArrayUtil.toArray(list, clazz)));
	}
	
	/**
	 * HashSet去重
	 * @param list	需要去重的List
	 * @return		去重后的List
	 */
	@SuppressWarnings("all")
	public static List distinct(List list) {
		HashSet h = new HashSet(list);
		list.clear();
		list.addAll(h);
		return list;
	}
	
	/**
	 * <h1>Map集合去重</h1>
	 * {@link List}>>{@linkplain Map}根据参数distinctKey（Map的key）去重。
	 * @param list 需要处理的集合
	 * @param distinctKey 去重的依据（Map的key）
	 * @return 处理后的List集合
	 */
	public static List<Map<String, Object>> distinctMap(List<Map<String, Object>> list, String distinctKey) {
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> mapi = list.get(i);
			for (int j = list.size() - 1; j > i; j--) {
				Map<String, Object> mapj = list.get(j);
				if (mapi.get(distinctKey).equals(mapj.get(distinctKey))) {
					list.remove(j);
				}
			}
		}
		
		return list;
	}
	
	/**
	 * <h1>JSONObject集合去重统计与排序</h1>
	 * {@link List}>>{@linkplain JSONObject}根据参数distinctKey（JSONObject的key），计算元素重复次数。
	 * <p>并为每个JSONObject添加一个<b>frequency</b>（频率元素），value的值是从整数1开始计数。<br>
	 * 示例：<code>json.put("frequency", frequency)</code>
	 * </p>
	 * <p><b>根据frequency（重复频率）排序</b>
	 * 
	 * @param list 需要处理的集合
	 * @param distinctKey 去重的依据（JSONObject的key）
	 * @param sortEnum 排序方式
	 * @return 处理后的List集合
	 */
	public static List<JSONObject> distinctCount(List<JSONObject> list, String distinctKey, SortEnum sortEnum) {
		for (int i = 0; i < list.size(); i++) {
			int frequency = 1;
			JSONObject jsoni = list.get(i);
			for (int j = list.size() - 1; j > i; j--) {
				JSONObject jsonj = list.get(j);
				if (jsoni.get(distinctKey).equals(jsonj.get(distinctKey))) {
					list.remove(j);
					frequency++;
				}
			}
			jsoni.put("frequency", frequency);
		}
		
		return sort(list, "frequency", sortEnum);
	}
	
	/**
	 * <h1>JSONObject集合——去重、统计、排序与元素选择性保留</h1>
	 * {@link List}>>{@linkplain JSONObject}根据参数distinctKey（JSONObject的key），计算元素重复次数。
	 * <p>并为每个JSONObject添加一个<b>frequency</b>（频率元素），value的值是从整数1开始计数。<br>
	 * 示例：<code>json.put("frequency", frequency)</code>
	 * </p>
	 * <p><b>根据frequency（重复频率）排序</b>
	 * @param list 需要处理的集合
	 * @param distinctKey 去重的依据（JSONObject的key）
	 * @param sortEnum 排序方式
	 * @param keepKey 需要保留的重复元素（此参数必须为可判断的Number类型：根据maxOrMinEnum选择保留最大值 <i>或</i> 最小值）<b><i>如：</i></b>根据id去重，保留age为最大或最小的JSONObject
	 * @param maxOrMinEnum 保留的值：最大值 <i>或</i> 最小值
	 * @return 处理后的List集合
	 */
	public static List<JSONObject> distinctCountSortSelectKeep(List<JSONObject> list, String distinctKey, SortEnum sortEnum, String keepKey, MaxOrMinEnum maxOrMinEnum) {
		for (int i = 0; i < list.size(); i++) {
			int frequency = 1;
			JSONObject jsoni = list.get(i);
			for (int j = list.size() - 1; j > i; j--) {
				JSONObject jsonj = list.get(j);
				if (jsoni.get(distinctKey).equals(jsonj.get(distinctKey))) {
					// i > j
					if (CompareUtil.compare(jsoni.get(keepKey), jsonj.get(keepKey), false) > 0) {
						if (maxOrMinEnum == MaxOrMinEnum.最小值) {
							jsoni.replace(keepKey, jsonj.get(keepKey));
						}
						list.remove(j);
						frequency++;
					} else {
						if (maxOrMinEnum == MaxOrMinEnum.最大值) {
							jsoni.replace(keepKey, jsonj.get(keepKey));
						}
						list.remove(j);
						frequency++;
					}
				}
			}
			jsoni.put("frequency", frequency);
		}
		
		return sort(list, "frequency", sortEnum);
	}
	
	/**
	 * <h1>数组转List</h1>
	 * <p>
	 * 此方法为 {@link Arrays#asList} 的安全实现
	 * @param <T>	数组中的对象类
	 * @param array	将被转换的数组
	 * @return		被转换数组的列表视图
	 */
	public static <T> ArrayList<T> toList(T[] array) {
		ArrayList<T> toList = new ArrayList<>(Arrays.asList(array));
		return toList;
	}
	
	/**
	 * <h1>{@linkplain List}>>{@link JSONObject} 转 {@linkplain List}>>{@link T}</h1>
	 * @param <T>
	 * @param list 		需要转换的List
	 * @param clazz		json转换的POJO类型
	 * @return			转换后的List
	 */
	public static <T> List<T> toList(List<JSONObject> list, Class<T> clazz) {
		List<T> toList = new ArrayList<> ();
		for(JSONObject json : list) {
			toList.add(ObjectUtils.toJavaObject(json, clazz));
		}
		
		return toList;
	}
	
	/**
	 * <h1>{@linkplain List}>>{@link JSONObject} 转 {@linkplain List}>>{@link String}</h1>
	 * @param list 		需要转换的List
	 * @param keepKey	保留值的key
	 * @return			转换后的List
	 */
	public static List<String> toList(List<JSONObject> list, String keepKey) {
		List<String> toList = new ArrayList<> ();
		for(JSONObject json : list) {
			String value = json.getString(keepKey);
			toList.add(value);
		}
		
		return toList;
	}
	
	/**
	 * <h1>{@linkplain List}>>{@link JSONObject} 转 {@linkplain List}>>{@link T}</h1>
	 * @param <T>
	 * @param list 		需要转换的List
	 * @param keepKey	保留值的key
	 * @param clazz		类型
	 * @return			转换后的List
	 */
	public static <T> List<T> toList(List<JSONObject> list, String keepKey, Class<T> clazz) {
		List<T> toList = new ArrayList<> ();
		for(JSONObject json : list) {
			toList.add(ObjectUtils.toObject(json.get(keepKey), clazz));
		}
		
		return toList;
	}
	
	/**
	 * <h1>{@linkplain List} >> {@link JSONObject} 转 {@linkplain List} >> {@link String}并去除重复元素</h1>
	 * <p>
	 * @param list 		需要转换的List
	 * @param keepKey	保留值的key
	 * @return			处理后的List
	 */
	@SuppressWarnings("unchecked")
	public static List<String> toListAndDistinct(List<JSONObject> list, String keepKey) {
		List<String> toList = new ArrayList<> ();
		for(JSONObject json : list) {
			String value = json.getString(keepKey);
			toList.add(value);
		}
		
		return distinct(toList);
	}
	
	/**
	 * <h1>{@linkplain List}>>{@link JSONObject} 转 {@linkplain List}>>{@link T}并去除重复元素</h1>
	 * @param <T>
	 * @param list 		需要转换的List
	 * @param keepKey	保留值的key
	 * @param clazz		类型
	 * @return			处理后的List
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> toListAndDistinct(List<JSONObject> list, String keepKey, Class<T> clazz) {
		return distinct(toList(list, keepKey, clazz));
	}
	
	/**
	 * <h1>{@linkplain List} >> {@link Map} 转 {@linkplain List} >> {@link JSONObject}</h1>
	 * <p>
	 * 	<b><i>性能测试说明：</i></b><br>
	 * 	<i>测试CPU：</i>i7-4710MQ<br>
	 * 	<i>测试结果：</i>百万级数据平均200ms（毫秒）<br>
	 * </p>
	 * @param list 		需要转换的List
	 * @return			转换后的List
	 */
	public static List<JSONObject> toJsonList(List<Map<String, Object>> list) {
		List<JSONObject> jsonList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			jsonList.add(new JSONObject(list.get(i)));
		}
		
		return jsonList;
	}
	
	/**
	 * <h1>{@linkplain JSONArray} 转 {@linkplain List} >> {@link JSONObject}</h1>
	 * <p>
	 * 	<b><i>性能测试报告：</i></b><br>
	 *  <i>无类型转换（类型推断）：</i>见 {@linkplain #toList(List)}<br>
	 * 	<i>安全模式强制类型转换：</i>暂未测试<br>
	 * </p>
	 * @param jsonArray 需要转换的JSONArray
	 * @return 转换后的jsonList
	 */
	public static List<JSONObject> toJsonList(JSONArray jsonArray) {
		List<JSONObject> jsonList = new ArrayList<>();
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonList.add(jsonArray.getJSONObject(i));
		}
		
		return jsonList;
	}
	
	/**
	 * <h1>{@linkplain List} >> {@link T} 转 {@linkplain List} >> {@link JSONObject}</h1>
	 * <p>
	 * 	<b><i>性能测试报告：</i></b><br>
	 * 	<i>安全模式强制类型转换：</i>暂未测试<br>
	 * </p>
	 * @param list 需要转换的List
	 * @return 转换后的jsonList
	 */
	public static <T> List<JSONObject> toJsonListT(List<T> list) {
		List<JSONObject> jsonList = new ArrayList<>();
		for (T obj : list) {
			jsonList.add(ObjectUtils.toJSONObject(obj));
		}
		
		return jsonList;
	}
	
	/**
	 * <h1>{@linkplain JSONArray} 转 {@linkplain JSONObject}[]</h1>
	 * <p>对象引用转换，内存指针依旧指向元数据</p>
	 * @param jsonArray 需要转换的JSONArray
	 * @return			转换后的jsons
	 */
	public static JSONObject[] toJsons(JSONArray jsonArray) {
		int size = jsonArray.size();
		JSONObject[] jsons = new JSONObject[size];
		for (int i = 0; i < size; i++) {
			jsons[i] = jsonArray.getJSONObject(i);
		}
		
		return jsons;
	}
	
	/**
	 * <h1>{@linkplain List}>>{@link JSONObject} 转 {@linkplain JSONObject}[]</h1>
	 * <p>对象引用转换，内存指针依旧指向元数据</p>
	 * @param list 		需要转换的List
	 * @return			转换后的jsons
	 */
	public static JSONObject[] toJsons(List<JSONObject> list) {
		JSONObject[] jsons = new JSONObject[list.size()];
		int index = 0;
		for (JSONObject json : list) {
			jsons[index] = json;
			index++;
		}
		return jsons;
	}
	
	/**
	 * <h1>{@linkplain List} >> {@link T} 转 {@linkplain JSONObject}[]</h1>
	 * <p>
	 * 	<b><i>性能测试报告：</i></b><br>
	 * 	<i>安全模式强制类型转换：</i>暂未测试<br>
	 * </p>
	 * @param list 需要转换的List
	 * @return 转换后的jsons
	 */
	public static <T> JSONObject[] toJsonsT(List<T> list) {
		JSONObject[] jsons = new JSONObject[list.size()];
		int index = 0;
		for (T obj : list) {
			jsons[index] = ObjectUtils.toJSONObject(obj);
			index++;
		}
		
		return jsons;
	}
	
	/**
	 * <h1>{@linkplain List} >> {@link T} 转 {@linkplain JSONObject}[] 并移除空对象</h1>
	 * <p>
	 * 	<b><i>性能测试报告：</i></b><br>
	 * 	<i>安全模式强制类型转换：</i>暂未测试<br>
	 * </p>
	 * @param list 需要转换的List
	 * @return 转换后的jsons
	 */
	public static <T> JSONObject[] toJsonsTAndRemoveEmpty(List<T> list) {
		JSONObject[] jsons = new JSONObject[list.size()];
		int index = 0;
		for (T obj : list) {
			JSONObject json = ObjectUtils.toJSONObject(obj);
			MapUtils.removeEmpty(json);
			jsons[index] = json;
			index++;
		}
		
		return jsons;
	}
	
	/**
	 * <h1>{@linkplain String} 转 {@linkplain JSONObject}[]</h1>
	 * @param jsonString	需要转换的JSON字符串
	 * @return
	 */
	public static JSONObject[] toJsons(String jsonString) {
		return toJsons(JSONArray.parseArray(jsonString));
	}
	
	/**
	 * <h1>{@linkplain String} 转 {@linkplain JSONObject}[]</h1>
	 * <blockquote>示例：
     * <pre>
     * 	{@code
     * 		String text = "1,3,5,9";
     * 		JSONObject[] jsons = toJSONs(text, ",", "id");
     * 		System.out.println(Arrays.toString(jsons));
     * }
     * </pre>
     * 结果：
     * 		[{"id":"1"}, {"id":"3"}, {"id":"5"}, {"id":"9"}]
     * </blockquote>
	 * @param text		需要转换的文本
	 * @param regex		文本分割表达式，同{@linkplain String}类的split()方法
	 * @param key		JSON的key名称
	 * @return			转换后的jsons
	 */
	public static JSONObject[] toJsons(String text, String regex, String key) {
		String[] texts = text.split(regex);
		JSONObject[] jsons = new JSONObject[texts.length];
		for (int i = 0; i < texts.length; i++) {
			JSONObject paramJson = new JSONObject();
			paramJson.put(key, texts[i]);
			jsons[i] = paramJson;
		}
		
		return jsons;
	}
	
}
