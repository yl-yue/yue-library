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

/**
 * List工具类
 * @author  孙金川
 * @version 创建时间：2017年10月27日
 */
public class ListUtils {
	
	/**
	 * 数据分组
	 * <p>
	 * 将拥有相同的 key 值的JSON数据归为一组
	 * @param list	要处理的集合
	 * @param key	分组依据
	 * @return
	 */
	public static JSONArray grouping(List<Map<String, Object>> list, String key) {
		JSONArray result = new JSONArray();
		toListAndDistinct(list, key).forEach(str -> {
			JSONArray jsonArray = new JSONArray();
			list.forEach(map -> {
				JSONObject paramJSON = new JSONObject(map);
				if (str.equals(paramJSON.getString(key))) {
					jsonArray.add(paramJSON);
				};
			});
			result.add(jsonArray);
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
	 * <h1>List-Map集合排序</h1>
	 * <p>
	 * &nbsp;0 （降序）<br>
	 * &nbsp;1+（升序）
	 * </p>
	 * @param list 需要处理的集合
	 * @param sortKey 去重的依据（Map的key）
	 * @param rule 排序方式：0（降序）&nbsp;1（升序）
	 * @return 处理后的List集合
	 */
	public static List<Map<String, Object>> sort(List<Map<String, Object>> list, String sortKey, int rule) {
		Collections.sort(list, new Comparator<Map<?, ?>>() {
			public int compare(Map<?, ?> o1, Map<?, ?> o2) {
				int map1value = (Integer) o1.get(sortKey);
				int map2value = (Integer) o2.get(sortKey);
				if(rule == 0) {
					return map2value - map1value;
				}else {
					return map1value - map2value;
				}
			}
		});
		return list;
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
	 * {@link List}>>{@linkplain Map}根据参数distinct_key（Map的key）去重。
	 * @param list 需要处理的集合
	 * @param distinct_key 去重的依据（Map的key）
	 * @return 处理后的List集合
	 */
	public static List<Map<String, Object>> distinctMap(List<Map<String, Object>> list, String distinct_key) {
		for(int i = 0; i < list.size(); i++) {
			Map<String, Object> mapi = list.get(i);
			for (int j = list.size() - 1; j > i; j--) {
				Map<String, Object> mapj = list.get(j);
				if(mapi.get(distinct_key).equals(mapj.get(distinct_key))){
					list.remove(j);
				}
			}
		}
		return list;
	}
	
	/**
	 * <h1>Map集合去重统计与排序</h1>
	 * {@link List}>>{@linkplain Map}根据参数distinct_key（Map的key），计算元素重复次数。
	 * <p>并为每个Map添加一个<b>frequency</b>（频率元素），value的值是从整数1开始计数。<br>
	 * 示例：<code>map.put("frequency", frequency)</code>
	 * </p>
	 * <p><b>根据frequency（重复频率）排序：</b>
	 * &nbsp;0（降序）
	 * &nbsp;其他数字（升序）
	 * </p>
	 * @param list 需要处理的集合
	 * @param distinct_key 去重的依据（Map的key）
	 * @param sort 排序方式：0（降序）&nbsp;1（升序）
	 * @return 处理后的List集合
	 */
	public static List<Map<String, Object>> distinctCount(List<Map<String, Object>> list, String distinct_key, int sort) {
		for(int i = 0; i < list.size(); i++) {
			int frequency = 1;
			Map<String, Object> mapi = list.get(i);
			for (int j = list.size() - 1; j > i; j--) {
				Map<String, Object> mapj = list.get(j);
				if(mapi.get(distinct_key).equals(mapj.get(distinct_key))){
					list.remove(j);
					frequency ++;
				}
			}
			mapi.put("frequency", frequency);
		}
		
		Collections.sort(list, new Comparator<Map<?, ?>>() {
			public int compare(Map<?, ?> o1, Map<?, ?> o2) {
				int map1value = (Integer) o1.get("frequency");
				int map2value = (Integer) o2.get("frequency");
				if(sort == 0) {
					return map2value - map1value;
				}else {
					return map1value - map2value;
				}
			}
		});
		
		return list;
	}
	
	/**
	 * <h1>Map集合——去重、统计、排序与元素选择性保留</h1>
	 * {@link List}>>{@linkplain Map}根据参数distinct_key（Map的key），计算元素重复次数。
	 * <p>并为每个Map添加一个<b>frequency</b>（频率元素），value的值是从整数1开始计数。<br>
	 * 示例：<code>map.put("frequency", frequency)</code>
	 * </p>
	 * <p><b>根据frequency（重复频率）排序：</b>
	 * &nbsp;0（降序）
	 * &nbsp;其他数字（升序）
	 * </p>
	 * @param list 需要处理的集合
	 * @param distinct_key 去重的依据（Map的key）
	 * @param sort 排序方式：0 或 1
	 * @param keep_key 需要保留的重复元素（此参数必须为可判断的Number类型:&nbsp;0（最大值）&nbsp;1（最小值））<br>如：根据id去重，保留age为最大或最小的Map
	 * @param max_or_min 保留的值：0（最大值）&nbsp;1（最小值）
	 * @return 处理后的List集合
	 */
	public static List<Map<String, Object>> distinct_count_sort_selectKeep(List<Map<String, Object>> list, String distinct_key, int sort, String keep_key, int max_or_min) {
		for(int i = 0; i < list.size(); i++) {
			int frequency = 1;
			Map<String, Object> mapi = list.get(i);
			for (int j = list.size() - 1; j > i; j--) {
				Map<String, Object> mapj = list.get(j);
				if(mapi.get(distinct_key).equals(mapj.get(distinct_key))){
					//i > j
					if(Double.parseDouble(mapi.get(keep_key).toString()) > Double.parseDouble(mapj.get(keep_key).toString())) {
						if(max_or_min != 0){
							mapi.replace(keep_key, mapj.get(keep_key));
						}
						list.remove(j);
						frequency ++;
					}else {
						if(max_or_min == 0){
							mapi.replace(keep_key, mapj.get(keep_key));
						}
						list.remove(j);
						frequency ++;
					}
				}
			}
			mapi.put("frequency", frequency);
		}
		
		Collections.sort(list, new Comparator<Map<?, ?>>() {
			public int compare(Map<?, ?> o1, Map<?, ?> o2) {
				int map1value = (Integer) o1.get("frequency");
				int map2value = (Integer) o2.get("frequency");
				if(sort == 0) {
					return map2value - map1value;
				}else {
					return map1value - map2value;
				}
			}
		});
		
		return list;
	}
	
	/**
	 * <h1>数组转List</h1>
	 * <p>
	 * 此方法为 {@link Arrays#asList} 的安全实现
	 * @param <T>	数组中的对象类
	 * @param a		将被转换的数组
	 * @return		被转换数组的列表视图
	 */
	@SafeVarargs
	public static <T> ArrayList<T> toList(T... a) {
		ArrayList<T> toList = new ArrayList<>(Arrays.asList(a));
		return toList;
	}
	
	/**
	 * <h1>{@linkplain List}>>{@link Map} 转 {@linkplain List}>>{@link String}</h1>
	 * @param list 		需要转换的List
	 * @param keep_key	保留值的key
	 * @return			转换后的List
	 */
	public static List<String> toList(List<Map<String, Object>> list, String keep_key) {
		List<String> toList = new ArrayList<> ();
		for(Map<String, Object> map : list) {
			String value = map.get(keep_key).toString();
			toList.add(value);
		}
		return toList;
	}
	
	/**
	 * <h1>{@linkplain JSONArray} 转 {@linkplain List}>>{@link String}</h1>
	 * @param array 	需要转换的JSONArray
	 * @param keep_key	保留值的key
	 * @return			转换后的List
	 */
	public static List<String> toList(JSONArray array, String keep_key) {
		List<String> toList = new ArrayList<> ();
		array.forEach(action -> {
			JSONObject json = (JSONObject) action;
			toList.add(json.getString(keep_key));
		});
		return toList;
	}
	
	/**
	 * <h1>{@linkplain List}>>{@link Map} 转 {@linkplain List}>>{@link T}</h1>
	 * @param <T>
	 * @param list 		需要转换的List
	 * @param keep_key	保留值的key
	 * @param clazz		类型
	 * @return			转换后的List
	 */
	public static <T> List<T> toList(List<Map<String, Object>> list, String keep_key, Class<T> clazz) {
		List<T> toList = new ArrayList<> ();
		for(Map<String, Object> map : list) {
			toList.add(ObjectUtils.toObject(map.get(keep_key), clazz));
		}
		return toList;
	}
	
	/**
	 * <h1>{@linkplain List}>>{@link Map} 转 {@linkplain List}>>{@link String}并去除重复元素</h1>
	 * @param list 		需要转换的List
	 * @param keep_key	保留值的key
	 * @return			处理后的List
	 */
	@SuppressWarnings("unchecked")
	public static List<String> toListAndDistinct(List<Map<String, Object>> list, String keep_key) {
		List<String> toList = new ArrayList<> ();
		for(Map<String, Object> map : list) {
			String value = map.get(keep_key).toString();
			toList.add(value);
		}
		return distinct(toList);
	}
	
	/**
	 * <h1>{@linkplain JSONArray} 转 {@linkplain List}>>{@link String}并去除重复元素</h1>
	 * @param array 	需要转换的JSONArray
	 * @param keep_key	保留值的key
	 * @return			处理后的List
	 */
	@SuppressWarnings("unchecked")
	public static List<String> toListAndDistinct(JSONArray array, String keep_key) {
		List<String> toList = new ArrayList<> ();
		array.forEach(action -> {
			JSONObject json = (JSONObject) action;
			toList.add(json.getString(keep_key));
		});
		return distinct(toList);
	}
	
	/**
	 * <h1>{@linkplain List}>>{@link Map} 转 {@linkplain List}>>{@link T}并去除重复元素</h1>
	 * @param <T>
	 * @param list 		需要转换的List
	 * @param keep_key	保留值的key
	 * @param clazz		类型
	 * @return			处理后的List
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> toListAndDistinct(List<Map<String, Object>> list, String keep_key, Class<T> clazz) {
		return distinct(toList(list, keep_key, clazz));
	}
	
	/**
	 * <h1>{@linkplain List}>>{@link Map} 转 {@linkplain Map[] maps}</h1>
	 * <p>对象引用转换，内存指针依旧指向元数据</p>
	 * @param list 		需要转换的List
	 * @return			转换后的maps
	 */
	public static Map<String, Object>[] toMaps(List<Map<String, Object>> list) {
		@SuppressWarnings("unchecked")
		Map<String, Object>[] maps = new Map[list.size()];
		int index = 0;
		for (Map<String, Object> map : list) {
			maps[index] = map;
			index++;
		}
		return maps;
	}
	
	/**
	 * <h1>{@linkplain List}>>{@link JSONObject} 转 {@linkplain JSONObject[]}</h1>
	 * <p>对象引用转换，内存指针依旧指向元数据</p>
	 * @param list 		需要转换的List
	 * @return			转换后的jsons
	 */
	public static JSONObject[] toJSONS(List<JSONObject> list) {
		JSONObject[] jsons = new JSONObject[list.size()];
		int index = 0;
		for (JSONObject json : list) {
			jsons[index] = json;
			index++;
		}
		return jsons;
	}
	
	/**
	 * <h1>{@linkplain JSONArray} 转 {@linkplain JSONObject[]}</h1>
	 * <p>对象引用转换，内存指针依旧指向元数据</p>
	 * @param jsonArray 需要转换的JSONArray
	 * @return			转换后的jsons
	 */
	public static JSONObject[] toJSONS(JSONArray jsonArray) {
		int size = jsonArray.size();
		JSONObject[] jsons = new JSONObject[size];
		for (int i = 0; i < size; i++) {
			jsons[i] = jsonArray.getJSONObject(i);
		}
		
		return jsons;
	}
	
	/**
	 * <h1>{@linkplain String} 转 {@linkplain JSONObject[]}</h1>
	 * @param jsonString	需要转换的JSON字符串
	 * @return
	 */
	public static JSONObject[] toJSONS(String jsonString) {
		return toJSONS(JSONArray.parseArray(jsonString));
	}
	
	/**
	 * <h1>{@linkplain String} 转 {@linkplain JSONObject[]}</h1>
	 * <blockquote>示例：
     * <pre>
     * 	{@code
     * 		String text = "1,3,5,9";
     * 		JSONObject[] jsons = toJSONS(text, ",", "id");
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
	public static JSONObject[] toJSONS(String text, String regex, String key) {
		String[] texts = text.split(regex);
		JSONObject[] jsons = new JSONObject[texts.length];
		for (int i = 0; i < texts.length; i++) {
			JSONObject paramJSON = new JSONObject();
			paramJSON.put(key, texts[i]);
			jsons[i] = paramJSON;
		}
		
		return jsons;
	}
	
}
