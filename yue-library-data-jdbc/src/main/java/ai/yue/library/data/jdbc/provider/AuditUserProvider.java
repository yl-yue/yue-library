package ai.yue.library.data.jdbc.provider;

/**
 * 审计用户信息提供
 * <p>实现此接口，配置为Bean即可</p>
 *
 * @author ylyue
 * @since 2021/7/27
 */
public interface AuditUserProvider {

    /**
     * 审计：用户名、昵称、人名
     *
     * @return 用户名、昵称、人名
     */
    String getUser();

    /**
     * 审计：用户uuid
     *
     * @return 用户uuid
     */
    String getUserUuid();

}
