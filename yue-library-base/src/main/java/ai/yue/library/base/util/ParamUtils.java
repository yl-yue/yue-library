package ai.yue.library.base.util;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.exception.ParamVoidException;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.ipo.ParamFormatIPO;
import ai.yue.library.base.ipo.ParamValidateIPO;
import ai.yue.library.base.view.ResultInfo;
import cn.hutool.core.lang.Validator;

/**
 * 参数处理工具类
 * @author  孙金川
 * @version 创建时间：2018年6月16日
 */
public class ParamUtils {

	/** 必传参数 */
	private static final String PARAM_PREFIX_MUST = "【必传参数】：";
	/** 可选参数 */
	private static final String PARAM_PREFIX_CAN = "【可选参数】：";
	/** 收到传参 */
	private static final String PARAM_PREFIX_RECEIVED = "【收到传参】：";
	
	// Format
	
	/**
	 * 参数美化-Boolean强类型转换
	 * @param paramJson	需要向强类型转换的参数
	 * @param keys		可多个boolean值的key
	 */
	public static void paramFormatBoolean(JSONObject paramJson, String... keys) {
		for (String key : keys) {
			paramJson.replace(key, paramJson.getBoolean(key));
		}
	}
	
	/**
	 * 参数美化-BigDecimal强类型转换
	 * @param paramJson	需要向强类型转换的参数
	 * @param keys		可多个BigDecimal值的key
	 */
	public static void paramFormatBigDecimal(JSONObject paramJson, String... keys) {
		for (String key : keys) {
			paramJson.replace(key, paramJson.getBigDecimal(key));
		}
	}
	
	/**
	 * 参数美化-JSONObject强类型转换
	 * @param paramJson	需要向强类型转换的参数
	 * @param keys		可多个JSONObject值的key
	 */
	public static void paramFormatJSONObject(JSONObject paramJson, String... keys) {
		for (String key : keys) {
			paramJson.replace(key, paramJson.getJSONObject(key));
		}
	}
	
	/**
	 * 参数美化-JSONArray强类型转换
	 * @param paramJson	需要向强类型转换的参数
	 * @param keys		可多个JSONArray值的key
	 */
	public static void paramFormatJSONArray(JSONObject paramJson, String... keys) {
		for (String key : keys) {
			paramJson.replace(key, paramJson.getJSONArray(key));
		}
	}
	
	/**
	 * 参数美化-Object强类型转换
	 * @param paramJson				需要向强类型转换的参数
	 * @param paramFormatIPOList	多个参数美化IPO
	 */
	public static void paramFormatObject(JSONObject paramJson, List<ParamFormatIPO> paramFormatIPOList) {
		for (ParamFormatIPO paramFormatIPO : paramFormatIPOList) {
			String key = paramFormatIPO.getKey();
			Class<?> clazz = paramFormatIPO.getClazz();
			paramJson.replace(key, paramJson.getObject(key, clazz));
		}
	}
	
	/**
	 * 参数美化--弱类型转强类型
	 * 
	 * @param paramJson			需要向强类型转换的参数
	 * @param booleanKeys		多个boolean值的key（可以为null）
	 * @param decimalKeys		多个BigDecimal值的key（可以为null）
	 * @param jsonObjectKeys	多个JSONObject值的key（可以为null）
	 * @param jsonArrayKeys		多个JSONArray值的key（可以为null）
	 */
	public static void paramFormat(JSONObject paramJson, String[] booleanKeys, String[] decimalKeys
			, String[] jsonObjectKeys, String[] jsonArrayKeys) {
		if (!StringUtils.isEmptys(booleanKeys)) {
			paramFormatBoolean(paramJson, booleanKeys);
		}
		
		if (!StringUtils.isEmptys(decimalKeys)) {
			paramFormatBigDecimal(paramJson, decimalKeys);
		}
		
		if (!StringUtils.isEmptys(jsonObjectKeys)) {
			paramFormatJSONObject(paramJson, jsonObjectKeys);
		}
		
		if (!StringUtils.isEmptys(jsonArrayKeys)) {
			paramFormatJSONArray(paramJson, jsonArrayKeys);
		}
	}
	
	// Validate
	
	/**
	 * 空对象校验
	 * @param objects 对象数组
	 * @throws ResultException 有空对象将抛出异常
	 */
	public static void paramValidate(Object ...objects) {
		for (int i = 0; i < objects.length; i++) {
			Object object = objects[i];
			if (ObjectUtils.isNull(object)) {
				throw new ResultException(ResultInfo.param_check_not_pass());
			}
		}
	}
	
	/**
	 * param参数校验
	 * <p>1. 判断Map数据结构key的一致性
	 * <p>2. 必传参数是否为空字符串
	 * 
	 * @param paramJson			参数
	 * @param mustContainKeys	必须包含的key（必传）
	 * @param canContainKeys	可包含的key（非必传）
	 * @throws ParamException 	不满足条件抛出此异常及其提示信息
	 */
	public static void paramValidate(JSONObject paramJson, String[] mustContainKeys, String... canContainKeys) {
		// 1. 判断Map数据结构key的一致性
		boolean isHint = false;
		if (!MapUtils.isKeys(paramJson, mustContainKeys, canContainKeys)) {
			isHint = true;
		}
		
		// 2. 必传参数是否为空字符串
		for (String key : mustContainKeys) {
			if (StringUtils.isEmptyIfStr(paramJson.get(key))) {
				isHint = true;
			}
		}
		
		// 3. 提示
		if (isHint) {
			StringBuffer paramHint = new StringBuffer();
			paramHint.append(PARAM_PREFIX_MUST + Arrays.toString(mustContainKeys));
			paramHint.append("，");
			paramHint.append(PARAM_PREFIX_CAN + Arrays.toString(canContainKeys));
			paramHint.append("，");
			paramHint.append(PARAM_PREFIX_RECEIVED + paramJson.keySet());
			throw new ParamException(paramHint.toString());
		}
	}
	
	/**
	 * param参数校验
	 * <p>1. 判断Map数组数据结构key的一致性
	 * <p>2. 必传参数是否为空字符串
	 * 
	 * @param paramList       		参数数组
	 * @param mustContainKeys 		必须包含的key（必传）
	 * @param canContainKeys  		可包含的key（非必传）
	 * @throws ParamVoidException 	参数是否为空抛出此异常
	 * @throws ParamException     	不满足条件抛出此异常及其提示信息
	 */
	public static void paramValidate(List<JSONObject> paramList, String[] mustContainKeys, String... canContainKeys) {
		// 1. 校验参数是否为空
		if (paramList.isEmpty()) {
			throw new ParamVoidException();
		}
		
		// 2. 确认参数key
		for (JSONObject paramJson : paramList) {
			paramValidate(paramJson, mustContainKeys, canContainKeys);
		}
	}
	
	/**
	 * 参数验证
	 * @param paramValidateIPO 需要验证的参数
	 * @throws ResultException 验证未通过将抛出相应的异常即其提示信息
	 */
	public static void paramValidate(ParamValidateIPO paramValidateIPO) {
		JSONObject paramJson = paramValidateIPO.getParamJson();
		String cellphoneKey = paramValidateIPO.getCellphoneKey();
		String emailKey = paramValidateIPO.getEmailKey();
		String idCardNumberKey = paramValidateIPO.getIdCardNumberKey();
		
		// 1. 11位手机号验证
		String cellphone = paramJson.getString(cellphoneKey);
		if (!StringUtils.isEmpty(cellphone)) {
			if (!Validator.isMobile(cellphone)) {
				throw new ResultException(ResultInfo.cellphone_error());
			}
		}
		
		// 2. 邮箱验证
		String email = paramJson.getString(emailKey);
		if (!StringUtils.isEmpty(email)) {
			if (!Validator.isEmail(email)) {
				throw new ResultException(ResultInfo.email_error());
			}
		}
		
		// 3. 身份证号码验证
		String idCardNumber = paramJson.getString(idCardNumberKey);
		if (!StringUtils.isEmpty(idCardNumber)) {
			if (!Validator.isCitizenId(idCardNumber)) {
				throw new ResultException(ResultInfo.id_card_number_error());
			}
		}
	}
    
    /**
     * 验证固话号码
     * @param telephone
     * @return
     */
	boolean isTelephoneValidate(String telephone) {
		String regex = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";
		return telephone.matches(regex);
	}
    
    /**
     * 11位手机号码隐藏加密
     * @param cellphone 手机号
     * @return 隐藏加密后的手机号
     */
    public static String cellphoneEncrypt(String cellphone) {
    	if (cellphone.length() == 11) {
    		return cellphone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    	}
    	if (cellphone.length() > 4) {
    		return cellphone.replaceAll("(\\d{2})\\d+(\\d{2})", "$1****$2");
    	}
    	return cellphone.replaceAll("(\\d{1})\\d+(\\d{1})", "$1**$2");
    }
    
}
