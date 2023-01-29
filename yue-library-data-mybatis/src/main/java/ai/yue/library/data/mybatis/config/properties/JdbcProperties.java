package ai.yue.library.data.mybatis.config.properties;

import ai.yue.library.base.constant.FieldNamingStrategyEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * jdbc可配置属性
 * 
 * @author	ylyue
 * @since	2018年11月6日
 */
@Data
@ConfigurationProperties("yue.data.mybatis")
public class JdbcProperties implements Serializable {

	private static final long serialVersionUID = -2792479012600072153L;

	// ====================== 关键字段定义 ======================

	/**
	 * 关键字段定义-数据删除标识
	 *
	 * <p>数据库类型：大整数</p>
	 * <p>字段值范围推荐：时间戳</p>
	 * <p>默认：delete_time</p>
	 */
	private String fieldDefinitionDeleteTime = "delete_time";

	/**
	 * 关键字段定义-排序
	 *
	 * <p>数据库类型：整数</p>
	 * <p>字段值范围推荐：正整数</p>
	 * <p>默认：sort_idx</p>
	 */
	private String fieldDefinitionSortIdx = "sort_idx";

	// ====================== JDBC属性 ======================


	/**
	 * 启用数据审计
	 *
	 * <p>数据审计提供者见：{@linkplain ai.yue.library.data.mybatis.provider.DataAuditProvider}</p>
	 */
	private boolean enableDataAudit = false;

}
