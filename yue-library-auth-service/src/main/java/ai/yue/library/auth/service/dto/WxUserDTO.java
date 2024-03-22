package ai.yue.library.auth.service.dto;

import com.alibaba.fastjson2.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信用户信息
 * 
 * @author	ylyue
 * @since	2018年9月11日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxUserDTO {
	
	String openid;
	String nickname;
	Character sex;
	String country;
	String province;
	String city;
	String headimgurl;
	JSONArray privilege;
	String unionid;
	
}
