package ai.yue.library.data.mybatis.config;

import ai.yue.library.data.mybatis.constant.DbConstant;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

@Data
@ConfigurationProperties(MybatisProperties.PREFIX)
public class MybatisProperties {
	
	public static final String PREFIX = "yue.data.mybatis";

	/**
	 * 启用逻辑删除
	 */
	private boolean enableLogicDelete = false;

	/**
	 * 启用数据审计
	 */
	private boolean enableDataAudit = false;

	/**
	 * 启用企业租户 Id
	 */
	private boolean enableTenantCoId = false;

	/**
	 * 逻辑删除忽略表
	 * <pre>
	 *     逻辑删除插件会自动扫描所有 @{@linkplain Mapper} Bean，如果实体中没有 {@value DbConstant#CLASS_DELETE_TIME} 字段，则忽略此表。
	 *     此处可配置需要额外忽略的表名
	 * </pre>
	 */
	private Set<String> ignoreTableLogicDeletes = new HashSet<>();

	/**
	 * 逻辑删除忽略数据源
	 */
	private Set<String> ignoreDsLogicDeletes = new HashSet<>();

	/**
	 * 企业租户 Id 忽略表
	 * <pre>
	 *     企业租户插件会自动扫描所有 @{@linkplain Mapper} Bean，如果实体中没有 {@value DbConstant#CLASS_TENANT_CO_ID} 字段，则忽略此表。
	 *     此处可配置需要额外忽略的表名
	 * </pre>
	 */
	private Set<String> ignoreTableTenantCoIds = new HashSet<>();

	/**
	 * 企业租户 Id 忽略数据源
	 */
	private Set<String> ignoreDsTenantCoIds = new HashSet<>();

	/**
	 * 额外 Mapper 扫描包
	 * <pre>
	 *     用于扩展 Mapper 扫描范围，适用于 yue-library 子模块或业务项目需要额外扫描的场景。
	 *     yue-library 子模块推荐使用 META-INF/yue/mapper-packages 文件约定自动注册，此配置项适用于手动扩展。
	 * </pre>
	 */
	private Set<String> extraMapperPackages = new HashSet<>();

}
