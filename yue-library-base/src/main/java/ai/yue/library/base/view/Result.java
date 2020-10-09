package ai.yue.library.base.view;

import static com.alibaba.fastjson.util.TypeUtils.castToBigDecimal;
import static com.alibaba.fastjson.util.TypeUtils.castToBigInteger;
import static com.alibaba.fastjson.util.TypeUtils.castToBoolean;
import static com.alibaba.fastjson.util.TypeUtils.castToDate;
import static com.alibaba.fastjson.util.TypeUtils.castToDouble;
import static com.alibaba.fastjson.util.TypeUtils.castToInt;
import static com.alibaba.fastjson.util.TypeUtils.castToLong;
import static com.alibaba.fastjson.util.TypeUtils.castToSqlDate;
import static com.alibaba.fastjson.util.TypeUtils.castToTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.base.webenv.WebEnv;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.var;

/**
 * HTTP请求最外层响应对象，更适应Restful风格API
 * 
 * @author	ylyue
 * @since	2017年10月8日
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
	
	private static final long serialVersionUID = -3830508963654505583L;
	
	/** 状态码 */
	@JSONField(ordinal = 1)
	private Integer code;
	/** 提示信息 */
	@JSONField(ordinal = 2)
	private String msg;
	/** 状态 */
	@JSONField(ordinal = 3)
	private boolean flag;
	/** count */
	@JSONField(ordinal = 4)
	private Long count;
	/** 数据 */
	@JSONField(ordinal = 5)
	private T data;
	
	/**
	 * <b>成功校验</b>
	 * <p>如果此处获得的Result是一个错误提示结果，那么便会抛出一个 {@linkplain ResultException} 异常，以便于数据回滚并进行异常统一处理。
	 * 
	 * @throws ResultException 返回的请求异常结果
	 */
	public void successValidate() {
		if (!flag) {
			throw new ResultException(this);
		}
	}
	
	// -------- getData --------
	
	public <D> D getData(Class<D> clazz) {
		return Convert.convert(data, clazz);
	}
	
	public <D> D dataToObject(Class<D> clazz) {
		return getData(clazz);
	}
	
	public <D> D dataToJavaBean(Class<D> clazz) {
		return Convert.toJavaBean(data, clazz);
	}
	
	public JSONObject dataToJSONObject() {
		return Convert.toJSONObject(data);
	}
	
	public JSONArray dataToJSONArray() {
		return Convert.toJSONArray(data);
	}
	
	public <D> List<D> dataToList(Class<D> clazz) {
		return Convert.toList(clazz, data);
	}
	
	@SuppressWarnings("unchecked")
	public List<JSONObject> dataToJsonList() {
		if (data instanceof List) {
			var dataTemp = (List<?>) data;
			if (ListUtils.isNotEmpty(dataTemp)) {
				if (dataTemp.get(0) instanceof JSONObject) {
		            return (List<JSONObject>) data;
				}
			}
		}
		
		return ListUtils.toJsonList(dataToJSONArray());
	}
	
	public Boolean dataToBoolean() {
		if (data == null) {
			return null;
		}

		return castToBoolean(data);
	}

	public Integer dataToInteger() {

		return castToInt(data);
	}

	public Long dataToLong() {

		return castToLong(data);
	}

	public Double dataToDouble() {

		return castToDouble(data);
	}

	public BigDecimal dataToBigDecimal() {

		return castToBigDecimal(data);
	}

	public BigInteger dataToBigInteger() {

		return castToBigInteger(data);
	}

	public String dataToString() {

		if (data == null) {
			return null;
		}

		return data.toString();
	}

	public String dataToJSONString() {

		if (data == null) {
			return null;
		}

		return JSONObject.toJSONString(data);
	}

	public Date dataToDate() {

		return castToDate(data);
	}

	public java.sql.Date dataToSqlDate() {

		return castToSqlDate(data);
	}

	public java.sql.Timestamp dataToTimestamp() {

		return castToTimestamp(data);
	}
	
	// -------- result convert --------
	
	public ResponseEntity<Result<?>> castToResponseEntity() {
		return ResponseEntity.status(getCode()).body(this);
	}
	
	/**
	 * 将Result写入当前请求上下文的响应结果中，如：HttpServletResponse等。具体由当前 {@link WebEnv} 环境实现
	 */
	public void response() {
		WebEnv webEnv = SpringUtils.getBean(WebEnv.class);
		webEnv.resultResponse(this);
	}
	
}
