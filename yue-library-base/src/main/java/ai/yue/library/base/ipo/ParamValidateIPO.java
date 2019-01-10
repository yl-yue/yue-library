package ai.yue.library.base.ipo;

import com.alibaba.fastjson.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 条件-对应paramMap的key值
 * @author  孙金川
 * @version 创建时间：2018年6月22日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParamValidateIPO {

	/** 校验参数 */
	JSONObject paramJSON;
	/** 手机号验证 */
	String cellphoneKey;
	/** 邮箱验证 */
	String emailKey;
	/** 身份证号码验证 */
	String idCardNumberKey;
	
}
