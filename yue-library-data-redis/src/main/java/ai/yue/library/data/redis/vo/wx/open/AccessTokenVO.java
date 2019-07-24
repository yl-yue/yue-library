package ai.yue.library.data.redis.vo.wx.open;

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
public class AccessTokenVO {
	
	String access_token;// 接口调用凭证
	Integer expires_in;// access_token接口调用凭证超时时间，单位（秒）
	String refresh_token;// 用户刷新access_token
	String openid;// 授权用户唯一标识
	String scope;// 用户授权的作用域，使用逗号（,）分隔
	
}
