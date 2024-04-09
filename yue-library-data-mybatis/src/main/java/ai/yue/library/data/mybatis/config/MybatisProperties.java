package ai.yue.library.data.mybatis.config;

import ai.yue.library.data.mybatis.constant.DbConstant;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Data
@ConfigurationProperties(MybatisProperties.PREFIX)
public class MybatisProperties {
	
	public static final String PREFIX = "yue.data.mybatis";

	/**
	 * 启用数据审计
	 */
	private boolean enableDataAudit = true;

	/**
	 * 启用逻辑删除
	 */
	private boolean enableLogicDelete = true;

	/**
	 * 启用企业租户
	 */
	private boolean enableTenantCo = true;

	/**
	 * 逻辑删除忽略表
	 * <pre>
	 *     逻辑删除插件会自动扫描所有 @{@linkplain Mapper} Bean，如果实体中没有 {@value DbConstant#CLASS_DELETE_TIME} 字段，则忽略此表。
	 *     此处可配置需要额外忽略的表名
	 * </pre>
	 */
	private Set<String> ignoreTableLogicDeletes;

	/**
	 * 企业租户忽略表
	 * <pre>
	 *     企业租户插件会自动扫描所有 @{@linkplain Mapper} Bean，如果实体中没有 {@value DbConstant#CLASS_TENANT_CO} 字段，则忽略此表。
	 *     此处可配置需要额外忽略的表名
	 * </pre>
	 */
	private Set<String> ignoreTableTenantCos;

}
