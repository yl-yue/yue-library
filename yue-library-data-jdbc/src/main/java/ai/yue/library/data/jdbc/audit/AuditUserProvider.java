package ai.yue.library.data.jdbc.audit;

/**
 * 审计用户信息提供
 * <p>实现此接口，配置为Bean即可</p>
 *
 * @author ylyue
 * @since 2021/7/27
 */
public interface AuditUserProvider {

    /**
     * 获得用户ID
     *
     * @return 有序的正整数ID
     */
    Long getUserId();

}
