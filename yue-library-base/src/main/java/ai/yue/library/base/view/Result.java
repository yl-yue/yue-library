package ai.yue.library.base.view;

import static com.alibaba.fastjson.JSON.toJSON;
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

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ai.yue.library.base.exception.ResultException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * http请求返回的最外层对象。
 * 
 * @author  孙金川
 * @version 创建时间：2017年10月8日
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
     * Result结果验证
     * <p>
     * 如果此处获得的Result是一个错误提示结果，那么便会抛出一个{@linkplain ResultException}异常，以便于数据回滚并进行异常统一处理。<br>
     * @throws ResultException
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
        if (data instanceof JSONObject) {
            return (JSONObject) data;
        }

        if (data instanceof String) {
            return JSON.parseObject((String) data);
        }

        return (JSONObject) toJSON(data);
	}
	
    public JSONArray dataToJSONArray() {
        if (data instanceof JSONArray) {
            return (JSONArray) data;
        }

        if (data instanceof String) {
            return (JSONArray) JSON.parse((String) data);
        }

        return (JSONArray) toJSON(data);
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
     * @throws IOException 
     */
	public void response() throws IOException {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		response.setContentType("application/json; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.print(JSONObject.toJSONString(this));
		writer.close();
		response.flushBuffer();
	}
    
}
