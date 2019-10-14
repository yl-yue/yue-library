package ai.yue.library.data.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * QQ用户信息
 * 
 * @author	ylyue
 * @since	2018年9月11日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QqUserDTO {

	String nickname;
	String figureurl_qq_1;
	Character gender;
	
}
