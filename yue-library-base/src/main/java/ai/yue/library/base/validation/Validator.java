package ai.yue.library.base.validation;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author  孙金川
 * @version 创建时间：2019年6月25日
 */
@Slf4j
@NoArgsConstructor
public class Validator {

    private Object param;
    @Autowired
	private javax.validation.Validator validator;
    
    /**
     * 切换校验对象
     *
     * @param param 校验对象
     * @return Validate
     */
    public Validator param(Object param) {
        this.param = param;
        return this;
    }
    
    /**
     * 非空校验
     *
     * @return Validate
     */
    public Validator notNull() {
        return notNull(null);
    }
    
    /**
     * 非空校验
     * 
     * @param errorMsgTemplate 错误消息内容模板（变量使用{}表示）
	 * @param params 模板中变量替换后的值
     * @return Validate
     */
    public Validator notNull(String errorMsgTemplate, Object... params) {
    	cn.hutool.core.lang.Validator.validateNotNull(param, errorMsgTemplate, params);
        return this;
    }
    
    /**
     * 正则校验
     * 
     * @param regex 正则表达式
     * @return Validate
     */
    public Validator regex(String regex) {
        return regex(regex, null);
    }
    
    /**
     * 正则校验
     *
     * @param regex 正则表达式
     * @param errorMsg 验证错误的信息
     * @return Validate
     */
	public Validator regex(String regex, String errorMsg) {
		cn.hutool.core.lang.Validator.validateMatchRegex(regex, (CharSequence) param, errorMsg);
		return this;
	}
    
    /**
     * 最大值校验
     *
     * @param max 最大值
     * @return Validator
     */
    public Validator max(Number max) {
        return max(max, null);
    }
    
    /**
     * 最大值校验
     *
     * @param max 最大值
     * @param errorMsg 错误信息
     * @return Validator
     */
	public Validator max(Number max, String errorMsg) {
		BigDecimal bigNum1 = NumberUtil.toBigDecimal((Number) param);
		BigDecimal bigNum2 = NumberUtil.toBigDecimal(max);
		
    	if (!NumberUtil.isLessOrEqual(bigNum1, bigNum2)) {
    		throw new ValidateException(errorMsg);
    	}
        return this;
    }
    
    /**
     * 最小值校验
     *
     * @param min 最小值
     * @return Validator
     */
    public Validator min(Number min) {
        return min(min, null);
    }
    
    /**
     * 最小值校验
     *
     * @param min 最小值
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator min(Number min, String errorMsg) {
		BigDecimal bigNum1 = NumberUtil.toBigDecimal((Number) param);
		BigDecimal bigNum2 = NumberUtil.toBigDecimal(min);
		
    	if (!NumberUtil.isGreaterOrEqual(bigNum1, bigNum2)) {
    		throw new ValidateException(errorMsg);
    	}
        return this;
    }
	
    /**
     * 长度校验
     *
     * @param max 最大长度
     * @param min 最小长度
     * @return Validator
     */
	public Validator length(int max, int min) {
		return length(max, min, null);
    }
    
    /**
     * 长度校验
     *
     * @param max 最大长度
     * @param min 最小长度
     * @param errorMsg 错误信息
     * @return Validator
     */
	public Validator length(int max, int min, String errorMsg) {
		int length = ObjectUtil.length(param);
		if (false == (length <= max && length >= min)) {
			throw new ValidateException(errorMsg);
		}
		
        return this;
    }
    
    /**
     * 中文校验
     *
     * @return Validate
     */
    public Validator chinese() {
        return chinese(null);
    }

    /**
     * 中文校验
     *
     * @param errorMsg 错误信息
     * @return Validate
     */
    public Validator chinese(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateChinese((CharSequence) param, errorMsg);
        return this;
    }
    
    /**
     * 英文校验
     *
     * @return Validate
     */
    public Validator english() {
        return english(null);
    }
    
    /**
     * 英文校验
     *
     * @param errorMsg 错误信息
     * @return Validate
     */
    public Validator english(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateWord((CharSequence) param, errorMsg);
        return this;
    }
    
    /**
     * 手机号校验
     *
     * @return Validate
     */
    public Validator cellphone() {
        return cellphone(null);
    }
    
    /**
     * 手机号校验
     *
     * @param errorMsg 错误信息
     * @return Validate
     */
    public Validator cellphone(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateMobile((CharSequence) param, errorMsg);
        return this;
    }
    
    /**
     * 邮箱校验
     *
     * @return Validate
     */
    public Validator email() {
        return email(null);
    }
    
    /**
     * 邮箱校验
     *
     * @param errorMsg 错误信息
     * @return Validate
     */
    public Validator email(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateEmail((CharSequence) param, errorMsg);
        return this;
    }
    
//    /**
//     * 自定义日期格式校验
//     *
//     * @param format 格式
//     * @return Validate
//     */
//    public Validate date(String format) {
//        return date(format, null);
//    }
//    
//    /**
//     * 自定义日期格式校验
//     *
//     * @param format 格式
//     * @param errorMsg    错误信息
//     * @return Validate
//     */
//    public Validate date(String format, String errorMsg) {
//        Validator.date(format, param, errorMsg);
//        return this;
//    }
    
    /**
     * 身份证校验
     *
     * @return Validate
     */
    public Validator idCard() {
        return idCard(null);
    }
    
    /**
     * 身份证校验
     *
     * @param errorMsg 错误信息
     * @return Validate
     */
    public Validator idCard(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateCitizenIdNumber((CharSequence) param, errorMsg);
        return this;
    }
    
    /**
     * IP地址校验
     *
     * @return Validate
     */
    public Validator Ipv4() {
        return Ipv4(null);
    }
    
    /**
     * IP地址校验
     *
     * @param errorMsg 错误信息
     * @return Validate
     */
    public Validator Ipv4(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateIpv4((CharSequence) param, errorMsg);
        return this;
    }
    
    /**
     * POJO对象校验（通过注解）
     *
     * @param param 校验对象
     * @return Validate
     */
	public Validator valid(Object param) {
    	Set<ConstraintViolation<Object>> violations = validator.validate(param);
		if (violations.size() > 0) {
			log.warn("{} violations.", violations.size());
			Console.log("校验对象：{}", param);
			JSONObject paramHint = new JSONObject();
			violations.forEach(violation -> {
				String key = violation.getPropertyPath().toString();
				String msg = violation.getMessage();
				paramHint.put(key, msg);
				System.out.println(key + " " + msg);
			});
			
			throw new ValidateException(paramHint.toJSONString());
		}
    	
    	return this;
    }
    
}
