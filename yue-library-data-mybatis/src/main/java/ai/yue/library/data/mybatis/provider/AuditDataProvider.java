package ai.yue.library.data.mybatis.provider;

/**
 * 审计数据提供者
 * <p>实现此接口，配置为Bean即可</p>
 *
 * @author ylyue
 * @since 2021/7/27
 */
public interface AuditDataProvider {

    /**
     * 用户名、昵称、人名
     */
    String getUser();

    /**
     * 用户 Id
     */
    Long getUserId();

    /**
     * 系统租户 Id：一级租户
     */
    String getTenantSysId();

    /**
     * 企业租户 Id：二级租户
     */
    String getTenantCoId();

}
