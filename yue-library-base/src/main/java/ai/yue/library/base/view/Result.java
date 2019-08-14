package ai.yue.library.base.view;

import static com.alibaba.fastjson.util.TypeUtils.castToBigDecimal;
import static com.alibaba.fastjson.util.TypeUtils.castToBigInteger;
import static com.alibaba.fastjson.util.TypeUtils.castToBoolean;
import static com.alibaba.fastjson.util.TypeUtils.castToDate;
import static com.alibaba.fastjson.util.TypeUtils.castToDouble;
import static com.alibaba.fastjson.util.TypeUtils.castToInt;
import static com.alibaba.fastjson.util.TypeUtils.castToJavaBean;
import static com.alibaba.fastjson.util.TypeUtils.castToLong;
import static com.alibaba.fastjson.util.TypeUtils.castToSqlDate;
import static com.alibaba.fastjson.util.TypeUtils.castToTimestamp;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.servlet.ServletUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HTTP请求，最外层响应对象。
 * 
 * @author	孙金川
 * @since	2017年10月8日
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
	
	private static final long serialVersionUID = -3830508963654505583L;
	
	/** 状态码 */
	private Integer code;
	/** 提示信息 */
	private String msg;
	/** 状态 */
	private Boolean flag;
	/** 数据 */
	private T data;
	/** count */
	@JsonInclude(Include.NON_NULL)
	private Long count;
	
	/**
	 * 成功校验
	 * <p>
	 * 如果此处获得的Result是一个错误提示结果，那么便会抛出一个 {@linkplain ResultException} 异常，以便于数据回滚并进行异常统一处理。
	 * 
	 * @throws ResultException 返回的请求异常结果
	 */
	public void successValidate() {
		if (!flag) {
			throw new ResultException(this);
		}
	}
	
	public <D> D getData(Class<D> clazz) {
		return castToJavaBean(data, clazz);
	}
	
	public JSONObject dataToJSONObject() {
		return Convert.toJSONObject(data);
	}
	
	public JSONArray dataToJSONArray() {
		return Convert.toJSONArray(data);
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
	
	/**
	 * 不建议使用
	 * 
	 * @deprecated 请使用 {@link #dataToJsonList()}
	 * @return jsonList
	 */
	@Deprecated
	public List<JSONObject> dataToJSONList() {
		return dataToJsonList();
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
	
	/**
	 * HttpServletResponse
	 */
	public void response() {
		HttpServletResponse response = ServletUtil.getResponse();
		response.setContentType("application/json; charset=utf-8");
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.print(JSONObject.toJSONString(this));
			writer.close();
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
