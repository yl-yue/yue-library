package ai.yue.library.data.jdbc.client.dialect;

import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.util.StringUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.Collection;

/**
 * <b>包装器</b>
 * <p>主要用于字段名的包装（在字段名的前后加字符，例如反引号来避免与数据库的关键字冲突）
 * <p>源自 hutool-db 进行增强扩展
 * 
 * @author	Looly
 * @since	2020年6月15日
 */
public class Wrapper {
	
	/** 前置包装符号 */
	private Character preWrapQuote;
	/** 后置包装符号 */
	private Character sufWrapQuote;
	
	public Wrapper() {
	}
	
	/**
	 * 构造
	 * @param wrapQuote 单包装字符
	 */
	public Wrapper(Character wrapQuote) {
		this.preWrapQuote = wrapQuote;
		this.sufWrapQuote = wrapQuote;
	}
	
	/**
	 * 包装符号
	 * @param preWrapQuote 前置包装符号
	 * @param sufWrapQuote 后置包装符号
	 */
	public Wrapper(Character preWrapQuote, Character sufWrapQuote) {
		this.preWrapQuote = preWrapQuote;
		this.sufWrapQuote = sufWrapQuote;
	}
	
	//--------------------------------------------------------------- Getters and Setters start
	
	/**
	 * @return 前置包装符号
	 */
	public char getPreWrapQuote() {
		return preWrapQuote;
	}
	/**
	 * 设置前置包装的符号
	 * @param preWrapQuote 前置包装符号
	 */
	public void setPreWrapQuote(Character preWrapQuote) {
		this.preWrapQuote = preWrapQuote;
	}
	
	/**
	 * @return 后置包装符号
	 */
	public char getSufWrapQuote() {
		return sufWrapQuote;
	}
	/**
	 * 设置后置包装的符号
	 * @param sufWrapQuote 后置包装符号
	 */
	public void setSufWrapQuote(Character sufWrapQuote) {
		this.sufWrapQuote = sufWrapQuote;
	}
	
	//--------------------------------------------------------------- Getters and Setters end
	
	/**
	 * 包装字段名<br>
	 * 有时字段与SQL的某些关键字冲突，导致SQL出错，因此需要将字段名用单引号或者反引号包装起来，避免冲突
	 *
	 * @param field 字段名
	 * @return 包装后的字段名
	 */
	public String wrap(String field) {
		if (preWrapQuote == null || sufWrapQuote == null || StrUtil.isBlank(field)) {
			return field;
		}

		//如果已经包含包装的引号，返回原字符
		if (StrUtil.isSurround(field, preWrapQuote, sufWrapQuote)) {
			return field;
		}

		//如果字段中包含通配符或者括号（字段通配符或者函数），不做包装
		if (StrUtil.containsAnyIgnoreCase(field, "*", "(", " ", " as ")) {
			return field;
		}

		//对于Oracle这类数据库，表名中包含用户名需要单独拆分包装
		if (field.contains(StrUtil.DOT)) {
			final Collection<String> target = CollUtil.edit(StrUtil.split(field, CharUtil.DOT, 2), t -> StrUtil.format("{}{}{}", preWrapQuote, t, sufWrapQuote));
			return CollectionUtil.join(target, StrUtil.DOT);
		}

		return StrUtil.format("{}{}{}", preWrapQuote, field, sufWrapQuote);
	}

	/**
	 * 包装字段名<br>
	 * 有时字段与SQL的某些关键字冲突，导致SQL出错，因此需要将字段名用单引号或者反引号包装起来，避免冲突
	 * @param fields 字段名
	 * @return 包装后的字段名
	 */
	public String[] wrap(String... fields){
		if(ArrayUtil.isEmpty(fields)) {
			return fields;
		}
		
		String[] wrappedFields = new String[fields.length];
		for(int i = 0; i < fields.length; i++) {
			wrappedFields[i] = wrap(fields[i]);
		}
		
		return wrappedFields;
	}
	
	/**
	 * 包装字段名<br>
	 * 有时字段与SQL的某些关键字冲突，导致SQL出错，因此需要将字段名用单引号或者反引号包装起来，避免冲突
	 * @param fields 字段名
	 * @return 包装后的字段名
	 */
	public Collection<String> wrap(Collection<String> fields){
		if(CollectionUtil.isEmpty(fields)) {
			return fields;
		}
		
		return Arrays.asList(wrap(fields.toArray(new String[0])));
	}
	
	/**
	 * 包装字段名<br>
	 * 有时字段与SQL的某些关键字冲突，导致SQL出错，因此需要将字段名用单引号或者反引号包装起来，避免冲突
	 * 
	 * @param paramJson 被包装的paramJson
	 * @return 包装后的字段名
	 */
	public JSONObject wrap(JSONObject paramJson) {
		if (MapUtils.isEmpty(paramJson)) {
			return paramJson;
		}
		
		// wrap fields
		JSONObject paramJsonWrapped = new JSONObject();
		paramJson.forEach((key, value) -> {
			paramJsonWrapped.put(wrap(key), value);
		});
		
		return paramJsonWrapped;
	}
	
	/**
	 * 包装字段名<br>
	 * 有时字段与SQL的某些关键字冲突，导致SQL出错，因此需要将字段名用单引号或者反引号包装起来，避免冲突
	 * 
	 * @param paramJsons 被包装的paramJson数组
	 * @return 包装后的字段名
	 */
	public JSONObject[] wrap(JSONObject[] paramJsons) {
		if (MapUtils.isEmptys(paramJsons)) {
			return paramJsons;
		}
		
		for (int i = 0; i < paramJsons.length; i++) {
			JSONObject paramJson = paramJsons[i];
			paramJsons[i] = wrap(paramJson);
		}
		
		return paramJsons;
	}
	
	/**
	 * 去除字段包装
	 * 
	 * @param field 字段
	 * @return 去除包装后的字段
	 */
	public String unwrap(String field) {
		if (StringUtils.isEmpty(field)) {
			return field;
		}
		
		return StringUtils.deleteFirstLastEqualString(field, CharUtil.toString(getPreWrapQuote()),
				CharUtil.toString(getSufWrapQuote()));
	}
	
}
