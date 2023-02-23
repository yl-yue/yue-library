package ai.yue.library.data.mybatis.interceptor;

import ai.yue.library.data.mybatis.constant.DbConstant;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.statement.insert.Insert;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 逻辑删除拦截器
 *
 * @author yl-yue
 * @since 2023/2/20
 */
public class LogicDeleteInnerInterceptor extends TenantLineInnerInterceptor {

    public LogicDeleteInnerInterceptor(String... ignoreTables) {
        TenantLineHandler tenantLineHandler = new TenantLineHandler() {
            @Override
            public String getTenantIdColumn() {
                return DbConstant.DB_FIELD_DEFINITION_DELETE_TIME;
            }

            @Override
            public Expression getTenantId() {
                return new LongValue(0L);
            }

            @Override
            public boolean ignoreTable(String tableName) {
                for (String ignoreTable : ignoreTables) {
                    if (tableName.equalsIgnoreCase(ignoreTable)) {
                        return true;
                    }
                }

                return false;
            }
        };
        super.setTenantLineHandler(tenantLineHandler);
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        if (StrUtil.containsAny(boundSql.getSql(), "delete_time=0", "delete_time = 0")) {
            return;
        }

        super.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
    }

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        if (StrUtil.containsAny(sh.getBoundSql().getSql(), "delete_time=0", "delete_time = 0")) {
            return;
        }

        super.beforePrepare(sh, connection, transactionTimeout);
    }

    @Override
    protected void processInsert(Insert insert, int index, String sql, Object obj) {
    }

}
