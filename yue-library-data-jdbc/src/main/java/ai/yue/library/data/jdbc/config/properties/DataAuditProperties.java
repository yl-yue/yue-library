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

	/**
	 * 审计字段定义-创建人
	 *
	 * <p>默认：create_user</p>
	 */
	private String fieldNameCreateUser = "create_user";

	/**
	 * 审计字段定义-创建人uuid
	 *
	 * <p>默认：create_user_uuid</p>
	 */
	private String fieldNameCreateUserUuid = "create_user_uuid";

	/**
	 * 审计字段定义-创建时间
	 *
	 * <p>默认：create_time</p>
	 */
	private String fieldNameCreateTime = "create_time";

	/**
	 * 审计字段定义-更新人
	 *
	 * <p>默认：update_user</p>
	 */
	private String fieldNameUpdateUser = "update_user";

	/**
	 * 审计字段定义-更新人uuid
	 *
	 * <p>默认：update_user_uuid</p>
	 */
	private String fieldNameUpdateUserUuid = "update_user_uuid";

	/**
	 * 审计字段定义-更新时间
	 *
	 * <p>默认：update_time</p>
	 */
	private String fieldNameUpdateTime = "update_time";

	/**
	 * 审计字段定义-删除人
	 *
	 * <p>默认：delete_user</p>
	 */
	private String fieldNameDeleteUser = "delete_user";

	/**
	 * 审计字段定义-删除人uuid
	 *
	 * <p>默认：delete_user_uuid</p>
	 */
	private String fieldNameDeleteUserUuid = "delete_user_uuid";

	/**
	 * 审计字段定义-删除时间戳
	 *
	 * <p>默认：delete_time</p>
	 */
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
