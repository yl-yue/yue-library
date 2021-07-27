package ai.yue.library.data.jdbc.config.properties;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.data.jdbc.constant.EncryptAlgorithmEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * jdbc数据加密可配置属性
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
public class DataEncryptProperties implements Serializable, Cloneable {

	private static final long serialVersionUID = -2792479012600072155L;

	/**
	 * 数据加密算法
	 * <p>如果表级值为空，默认使用父级值</p>
	 */
	private EncryptAlgorithmEnum dataEncryptAlgorithm;

	/**
	 * 数据加密密钥
	 * <p>如果表级值为空，默认使用父级值</p>
	 */
	private String dataEncryptKey;

	/**
	 * 需要进行数据加密的字段
	 */
	private List<String> fieldNames;

	@Override
	public DataEncryptProperties clone() {
		DataEncryptProperties dataEncrypt = null;
		try {
			dataEncrypt = (DataEncryptProperties) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return Convert.toJavaBean(Convert.toJSONObject(dataEncrypt), getClass());
	}

}
