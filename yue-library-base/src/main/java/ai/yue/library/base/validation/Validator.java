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
import cn.hutool.core.util.StrUtil;
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
    
    // 提示
	private static final String NOT_NULL_HINT_MSG = "参数必须不为 null";
	private static final String NOT_EMPTY_HINT_MSG = "参数必须不为empty(null 或 \"\")";
	private static final String ASSERT_TRUE_HINT_MSG = "参数必须为 true";
	private static final String ASSERT_FALSE_HINT_MSG = "参数必须为 false";
	private static final String CHINESE_HINT_MSG = "中文校验不通过";
	private static final String ENGLISH_HINT_MSG = "英文校验不通过";
	private static final String BIRTHDAY_HINT_MSG = "生日校验不通过";
	private static final String CELLPHONE_HINT_MSG = "不是一个合法的手机号码";
	private static final String EMAIL_HINT_MSG = "不是一个合法的邮箱格式";
	private static final String ID_CARD_HINT_MSG = "不是一个合法的身份证号码";
	private static final String PLATE_NUMBER_HINT_MSG = "不是一个合法的中国车牌号码";
	private static final String UUID_HINT_MSG = "不是一个合法的UUID";
	private static final String URL_HINT_MSG = "不是一个合法的URL";
	private static final String IPV4_HINT_MSG = "不是一个合法的IPV4地址";
	private static final String IPV6_HINT_MSG = "不是一个合法的IPV6地址";
	private static final String MAC_ADDRESS_HINT_MSG = "不是一个合法的MAC地址";
    
    /**
     * 切换校验对象
     *
     * @param param 校验对象
     * @return Validator
     */
    public Validator param(Object param) {
        this.param = param;
        return this;
    }
    
    /**
     * 必须不为 {@code null}
     *
     * @return Validator
     */
    public Validator notNull() {
        return notNull(NOT_NULL_HINT_MSG);
    }
    
    /**
     * 必须不为 {@code null}
     * 
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator notNull(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateNotNull(param, errorMsg);
        return this;
    }
    
    /**
     * 必须不为empty(null 或 "")
     *
     * @return Validator
     */
    public Validator notEmpty() {
        return notEmpty(NOT_EMPTY_HINT_MSG);
    }
    
    /**
     * 必须不为empty(null 或 "")
     * 
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator notEmpty(String errorMsg) {
		cn.hutool.core.lang.Validator.validateNotEmpty(param, errorMsg);
        return this;
    }
    
    /**
     * 必须为 true
     *
     * @return Validator
     */
    public Validator assertTrue() {
        return assertTrue(ASSERT_TRUE_HINT_MSG);
    }
    
    /**
     * 必须为 true
     * 
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator assertTrue(String errorMsg) {
		cn.hutool.core.lang.Validator.validateTrue((boolean) param, errorMsg);
        return this;
    }
    
    /**
     * 必须为 false
     *
     * @return Validator
     */
    public Validator assertFalse() {
        return assertFalse(ASSERT_FALSE_HINT_MSG);
    }
    
    /**
     * 必须为 false
     * 
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator assertFalse(String errorMsg) {
		cn.hutool.core.lang.Validator.validateFalse((boolean) param, errorMsg);
        return this;
    }
    
    /**
     * 必须是一个数字，其值必须在可接受的范围内（包含）
     *
     * @return Validator
     */
    public Validator digits(Number max, Number min) {
		return digits(max, min, StrUtil.format("必须是一个数字，其值必须在{}，{}之间（包含）", max, min));
    }
    
    /**
     * 必须是一个数字，其值必须在可接受的范围内（包含）
     * 
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator digits(Number max, Number min, String errorMsg) {
		cn.hutool.core.lang.Validator.validateBetween((Number) param, min, max, errorMsg);
        return this;
    }
    
    /**
     * 最大值校验
     *
     * @param max 最大值
     * @return Validator
     */
    public Validator max(Number max) {
        return max(max, "不能超过最大值：" + max);
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
        return min(min, "不能低于最小值：" + min);
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
		return length(max, min, StrUtil.format("最大长度不能超过{}，最小长度不能少于{}", max, min));
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
     * @return Validator
     */
    public Validator chinese() {
        return chinese(CHINESE_HINT_MSG);
    }
    
    /**
     * 中文校验
     *
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator chinese(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateChinese((CharSequence) param, errorMsg);
        return this;
    }
    
    /**
     * 英文校验
     *
     * @return Validator
     */
    public Validator english() {
        return english(ENGLISH_HINT_MSG);
    }
    
    /**
     * 英文校验
     *
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator english(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateWord((CharSequence) param, errorMsg);
        return this;
    }
    
    /**
     * 生日校验
     *
     * @return Validator
     */
    public Validator birthday() {
        return birthday(BIRTHDAY_HINT_MSG);
    }
    
    /**
     * 生日校验
     *
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator birthday(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateBirthday((CharSequence) param, errorMsg);
        return this;
    }
	
    /**
     * 手机号校验
     *
     * @return Validator
     */
    public Validator cellphone() {
        return cellphone(CELLPHONE_HINT_MSG);
    }
    
    /**
     * 手机号校验
     *
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator cellphone(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateMobile((CharSequence) param, errorMsg);
        return this;
    }
    
    /**
     * 邮箱校验
     *
     * @return Validator
     */
    public Validator email() {
        return email(EMAIL_HINT_MSG);
    }
    
    /**
     * 邮箱校验
     *
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator email(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateEmail((CharSequence) param, errorMsg);
        return this;
    }
    
    /**
     * 身份证校验
     *
     * @return Validator
     */
    public Validator idCard() {
        return idCard(ID_CARD_HINT_MSG);
    }
    
    /**
     * 身份证校验
     *
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator idCard(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateCitizenIdNumber((CharSequence) param, errorMsg);
        return this;
    }
    
    /**
     * 中国车牌号校验
     *
     * @return Validator
     */
    public Validator plateNumber() {
        return plateNumber(PLATE_NUMBER_HINT_MSG);
    }
    
    /**
     * 中国车牌号校验
     *
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator plateNumber(String errorMsg) {
    	cn.hutool.core.lang.Validator.validatePlateNumber((CharSequence) param, errorMsg);
        return this;
    }
    
    /**
     * UUID校验
     *
     * @return Validator
     */
    public Validator uuid() {
        return uuid(UUID_HINT_MSG);
    }
    
    /**
     * UUID校验
     *
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator uuid(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateUUID((CharSequence) param, errorMsg);
        return this;
    }
	
    /**
     * URL校验
     *
     * @return Validator
     */
    public Validator url() {
        return url(URL_HINT_MSG);
    }
    
    /**
     * URL校验
     *
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator url(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateUrl((CharSequence) param, errorMsg);
        return this;
    }
	
    /**
     * IPV4地址校验
     *
     * @return Validator
     */
    public Validator ipv4() {
        return ipv4(IPV4_HINT_MSG);
    }
    
    /**
     * IPV4地址校验
     *
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator ipv4(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateIpv4((CharSequence) param, errorMsg);
        return this;
    }
	
    /**
     * IPV6地址校验
     *
     * @return Validator
     */
    public Validator ipv6() {
        return ipv6(IPV6_HINT_MSG);
    }
    
    /**
     * IPV6地址校验
     *
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator ipv6(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateIpv6((CharSequence) param, errorMsg);
        return this;
    }
	
    /**
     * MAC地址校验
     *
     * @return Validator
     */
    public Validator macAddress() {
        return macAddress(MAC_ADDRESS_HINT_MSG);
    }
    
    /**
     * MAC地址校验
     *
     * @param errorMsg 错误信息
     * @return Validator
     */
    public Validator macAddress(String errorMsg) {
    	cn.hutool.core.lang.Validator.validateMac((CharSequence) param, errorMsg);
        return this;
    }
	
    /**
     * 正则校验
     * 
     * @param regex 正则表达式
     * @return Validator
     */
    public Validator regex(String regex) {
        return regex(regex, "不满足正则表达式：" + regex);
    }
    
    /**
     * 正则校验
     *
     * @param regex 正则表达式
     * @param errorMsg 验证错误的信息
     * @return Validator
     */
	public Validator regex(String regex, String errorMsg) {
		cn.hutool.core.lang.Validator.validateMatchRegex(regex, (CharSequence) param, errorMsg);
		return this;
	}
	
    /**
     * POJO对象校验（通过注解）
     *
     * @param param 校验对象
     * @return Validator
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
