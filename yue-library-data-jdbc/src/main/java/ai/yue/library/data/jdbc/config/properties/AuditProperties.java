package ai.yue.library.data.jdbc.config.properties;

import ai.yue.library.base.convert.Convert;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * jdbc审计可配置属性
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
public class AuditProperties implements Serializable, Cloneable {

	private static final long serialVersionUID = -2792479012600072156L;


	/**
	 * 需要进行数据加密的表
	 */
	private String delete_key;
//	creator_key
//			creator
//	update_key

    /**
	 * 需要进行数据加密的字段
	 */
	private List<String> fieldNames;

	@Override
	public AuditProperties clone() {
		AuditProperties audit = null;
		try {
			audit = (AuditProperties) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return Convert.toJavaBean(Convert.toJSONObject(audit), getClass());
	}

}
