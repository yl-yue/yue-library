package ai.yue.library.data.redis.dto;

import com.alibaba.fastjson.JSONArray;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author	孙金川
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
