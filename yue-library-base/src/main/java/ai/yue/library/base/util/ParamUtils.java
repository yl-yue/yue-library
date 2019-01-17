package ai.yue.library.base.util;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.exception.ParamException;
import ai.yue.library.base.exception.ParamVoidException;
import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.ipo.ParamFormatIPO;
import ai.yue.library.base.ipo.ParamValidateIPO;
import ai.yue.library.base.view.ResultInfo;

/**
 * 参数处理工具类
 * @author  孙金川
 * @version 创建时间：2018年6月16日
 */
public class ParamUtils {

	/** 必传参数 */
	static final String PARAM_PREFIX_MUST = "【必传参数】：";
	/** 可选参数 */
	static final String PARAM_PREFIX_CAN = "【可选参数】：";
	
	// Format
	
	/**
	 * 参数美化-Boolean强类型转换
	 * @param paramJSON	需要向强类型转换的参数
	 * @param keys		可多个boolean值的key
	 */
	public static void paramFormatBoolean(JSONObject paramJSON, String... keys) {
		for (String key : keys) {
			paramJSON.replace(key, paramJSON.getBoolean(key));
		}
	}
	
	/**
	 * 参数美化-BigDecimal强类型转换
	 * @param paramJSON	需要向强类型转换的参数
	 * @param keys		可多个BigDecimal值的key
	 */
	public static void paramFormatBigDecimal(JSONObject paramJSON, String... keys) {
		for (String key : keys) {
			paramJSON.replace(key, paramJSON.getBigDecimal(key));
		}
	}
	
	/**
	 * 参数美化-JSONObject强类型转换
	 * @param paramJSON	需要向强类型转换的参数
	 * @param keys		可多个JSONObject值的key
	 */
	public static void paramFormatJSONObject(JSONObject paramJSON, String... keys) {
		for (String key : keys) {
			paramJSON.replace(key, paramJSON.getJSONObject(key));
		}
	}
	
	/**
	 * 参数美化-JSONArray强类型转换
	 * @param paramJSON	需要向强类型转换的参数
	 * @param keys		可多个JSONArray值的key
	 */
	public static void paramFormatJSONArray(JSONObject paramJSON, String... keys) {
		for (String key : keys) {
			paramJSON.replace(key, paramJSON.getJSONArray(key));
		}
	}
	
	/**
	 * 参数美化-Object强类型转换
	 * @param paramJSON				需要向强类型转换的参数
	 * @param paramFormatIPOList	多个参数美化IPO
	 */
	public static void paramFormatObject(JSONObject paramJSON, List<ParamFormatIPO> paramFormatIPOList) {
		for (ParamFormatIPO paramFormatIPO : paramFormatIPOList) {
			String key = paramFormatIPO.getKey();
			Class<?> clazz = paramFormatIPO.getClazz();
			paramJSON.replace(key, paramJSON.getObject(key, clazz));
		}
	}
	
	/**
	 * 参数美化--弱类型转强类型
	 * 
	 * @param paramJSON			需要向强类型转换的参数
	 * @param booleanKeys		多个boolean值的key（可以为null）
	 * @param decimalKeys		多个BigDecimal值的key（可以为null）
	 * @param jsonObjectKeys	多个JSONObject值的key（可以为null）
	 * @param jsonArrayKeys		多个JSONArray值的key（可以为null）
	 */
	public static void paramFormat(JSONObject paramJSON, String[] booleanKeys, String[] decimalKeys
			, String[] jsonObjectKeys, String[] jsonArrayKeys) {
		if (!StringUtils.isEmptys(booleanKeys)) {
			paramFormatBoolean(paramJSON, booleanKeys);
		}
		
		if (!StringUtils.isEmptys(decimalKeys)) {
			paramFormatBigDecimal(paramJSON, decimalKeys);
		}
		
		if (!StringUtils.isEmptys(jsonObjectKeys)) {
			paramFormatJSONObject(paramJSON, jsonObjectKeys);
		}
		
		if (!StringUtils.isEmptys(jsonArrayKeys)) {
			paramFormatJSONArray(paramJSON, jsonArrayKeys);
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
			if (StringUtils.isEmpty(object)) {
				throw new ResultException(ResultInfo.param_check_not_pass());
			}
		}
	}
	
	/**
	 * param参数校验
	 * <p>
	 * 判断Map数据结构key的一致性
	 * @param paramJSON			参数
	 * @param mustContainKeys	必须包含的key（必传）
	 * @param canContainKeys	可包含的key（非必传）
	 * @throws ParamException 	不满足条件抛出此异常及其提示信息
	 */
	public static void paramValidate(JSONObject paramJSON, String[] mustContainKeys, String... canContainKeys) {
		for (String key : mustContainKeys) {
			if (!paramJSON.containsKey(key)) {
				throw new ParamException(PARAM_PREFIX_MUST + Arrays.toString(mustContainKeys));
			}
		}
		
		// 无可包含key
		if (StringUtils.isEmptys(canContainKeys)) {
			return;
		}
		
		int keySize = mustContainKeys.length + canContainKeys.length;
		if (paramJSON.size() > keySize) {
			throw new ParamException(PARAM_PREFIX_MUST + Arrays.toString(mustContainKeys) + "，" + PARAM_PREFIX_CAN
					+ Arrays.toString(canContainKeys));
		}
		
		int paramJSONCanContainKeysLength = 0;
		for (String key : canContainKeys) {
			if (paramJSON.containsKey(key)) {
				paramJSONCanContainKeysLength++;
			}
		}
		
		if (paramJSONCanContainKeysLength + mustContainKeys.length != paramJSON.size()) {
			throw new ParamException(PARAM_PREFIX_MUST + Arrays.toString(mustContainKeys) + "，" + PARAM_PREFIX_CAN
					+ Arrays.toString(canContainKeys));
		}
	}
	
	/**
	 * param参数校验
	 * <p>
	 * 判断Map数组数据结构key的一致性
	 * 
	 * @param jsonArray       		参数数组
	 * @param mustContainKeys 		必须包含的key（必传）
	 * @param canContainKeys  		可包含的key（非必传）
	 * @throws ParamVoidException 	参数是否为空抛出此异常
	 * @throws ParamException     	不满足条件抛出此异常及其提示信息
	 */
	public static void paramValidate(JSONArray jsonArray, String[] mustContainKeys, String... canContainKeys) {
		// 1. 校验参数是否为空
		if (jsonArray.isEmpty()) {
			throw new ParamVoidException();
		}

		// 2. 确认参数key
		for (int i = 0; i < jsonArray.size(); i++) {
			paramValidate(jsonArray.getJSONObject(i), mustContainKeys, canContainKeys);
		}
	}
	
	/**
	 * 参数验证
	 * @param paramValidateIPO
	 * @throws ResultException 验证未通过将抛出相应的异常即其提示信息
	 */
	public static void paramValidate(ParamValidateIPO paramValidateIPO) {
		JSONObject paramJSON = paramValidateIPO.getParamJSON();
		String cellphoneKey = paramValidateIPO.getCellphoneKey();
		String emailKey = paramValidateIPO.getEmailKey();
		String idCardNumberKey = paramValidateIPO.getIdCardNumberKey();
		
		// 1. 11位手机号验证
		String cellphone = paramJSON.getString(cellphoneKey);
		if (!StringUtils.isEmpty(cellphone)) {
			if (!isCellphoneValidate(cellphone)) {
				throw new ResultException(ResultInfo.cellphone_error());
			}
		}
		
		// 2. 邮箱验证
		String email = paramJSON.getString(emailKey);
		if (!StringUtils.isEmpty(email)) {
			if (!isEmailValidate(email)) {
				throw new ResultException(ResultInfo.email_error());
			}
		}
		
		// 3. 身份证号码验证
		String idCardNumber = paramJSON.getString(idCardNumberKey);
		if (!StringUtils.isEmpty(idCardNumber)) {
			if (!isIdCardNumberValidate(idCardNumber)) {
				throw new ResultException(ResultInfo.id_card_number_error());
			}
		}
	}
	
	/**
	 * 邮箱验证
	 * @param email
	 * @return
	 */
    static boolean isEmailValidate(String email) {
    	String regex = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        return email.matches(regex);
    }
    
	/**
	 * <h3>身份证号码验证</h3>
	 * 
     * 我国公民的身份证号码特点如下：<br>
     * 1.长度18位<br>
     * 2.第1-17号只能为数字<br>
     * 3.第18位只能是数字或者x<br>
     * 4.第7-14位表示特有人的年月日信息<br>
     * 
     * 正则实现如下：<br>
     * 1.地区：[1-9]\d{5}<br>
     * 2.年的前两位：(18|19|20)            1800-2099<br>
     * 3.年的后两位：\d{2}<br>
     * 4.月份：((0[1-9])|(10|11|12)) <br>
     * 5.天数：(([0-2][1-9])|10|20|30|31)	闰年不能禁止29+<br>
     * 6.三位顺序码：\d{3}<br>
     * 7.校验码：[0-9Xx]
     */
	static boolean isIdCardNumberValidate(String id) {
		String regex = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
		return id.matches(regex);
	}
    
    /**
     * 国内11位手机号码验证
     * @param cellphone
     * @return
     */
    static boolean isCellphoneValidate(String cellphone) {
    	String regex = "^((13[0-9])|(14[5|7|9])|(15([0-3]|[5-9]))|(17[0-8])|(18[0,1,2,3,5-9]))\\d{8}$";
        return cellphone.matches(regex);
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
     * 11位手机号码加密
     * @param cellphone
     * @return
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
