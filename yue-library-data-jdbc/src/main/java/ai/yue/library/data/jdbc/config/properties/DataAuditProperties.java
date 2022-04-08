package ai.yue.library.data.jdbc.config.properties;

import ai.yue.library.base.convert.Convert;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据审计属性
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
public class DataAuditProperties implements Serializable, Cloneable {

	private static final long serialVersionUID = -2792479012600072156L;

	private String fieldNameCreateUser = "create_user";
	private String fieldNameCreateUserUuid = "create_user_uuid";
	private String fieldNameCreateTime = "create_time";
	private String fieldNameUpdateUser = "update_user";
	private String fieldNameUpdateUserUuid = "update_user_uuid";
	private String fieldNameUpdateTime = "update_time";
	private String fieldNameDeleteUser = "delete_user";
	private String fieldNameDeleteUserUuid = "delete_user_uuid";
	private String fieldNameDeleteTime = "delete_time";

	@Override
	public DataAuditProperties clone() {
		DataAuditProperties audit = null;
		try {
			audit = (DataAuditProperties) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return Convert.toJavaBean(Convert.toJSONObject(audit), getClass());
	}

}
