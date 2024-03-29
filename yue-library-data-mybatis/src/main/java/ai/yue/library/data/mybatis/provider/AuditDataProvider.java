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
     * 用户id
     */
    Long getUserId();

    /**
     * 系统租户：一级租户
     */
    String getTenantSys();

    /**
     * 企业租户：二级租户
     */
    String getTenantCo();

}
