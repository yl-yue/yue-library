package ai.yue.library.template.dataobject;

import java.time.LocalDateTime;
import java.util.Date;

import ai.yue.library.data.jdbc.dataobject.DBDO;
import ai.yue.library.template.constant.UserStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author	孙金川
 * @since	2019年3月25日
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserDO extends DBDO {

	String nickname;
	Integer age;
	UserStatusEnum user_status;
	protected Date date;// 数据插入时间
	protected LocalDateTime localDateTime;// 数据插入时间
	
}
