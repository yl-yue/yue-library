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

	private String fieldNameCreateUserId = "create_user_id";
	private String fieldNameCreateTime = "create_time";
	private String fieldNameUpdateUserId = "update_user_id";
	private String fieldNameUpdateTime = "update_time";
	private String fieldNameDeleteUserId = "delete_user_id";
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
