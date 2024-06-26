package ai.yue.library.data.mybatis.interceptor;

import ai.yue.library.base.util.BeanUtils;
import ai.yue.library.base.util.ClassUtils;
import ai.yue.library.base.util.SpringUtils;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.insert.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 字段拦截器
 *
 * @author yl-yue
 * @since 2023/2/20
 */
@Slf4j
public class FieldInterceptor extends TenantLineInnerInterceptor implements ApplicationRunner {

    protected String interceptorName;
    protected Set<String> ignoreTableList;
    protected String classField;
    protected String dbField;
    protected Expression tenantIdExpression;

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        if (isExecute(boundSql)) {
            super.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
        }
    }

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        if (isExecute(sh.getBoundSql())) {
            super.beforePrepare(sh, connection, transactionTimeout);
        }
    }

    @Override
    protected void processInsert(Insert insert, int index, String sql, Object obj) {
    }

    private boolean isExecute(BoundSql boundSql) {
        TenantLineHandler tenantLineHandler = getTenantLineHandler();
        String tenantIdColumn = tenantLineHandler.getTenantIdColumn();
        String tenantId = tenantLineHandler.getTenantId().toString();
        String whereSql1 = StrUtil.format("{}={}", tenantIdColumn, tenantId);
        String whereSql2 = StrUtil.format("{} = {}", tenantIdColumn, tenantId);
        return !StrUtil.containsAny(boundSql.getSql(), whereSql1, whereSql2);
    }

    /**
     * 扫描所有的Mapper类，查找实体
     * 忽略没有dbField字段的表
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 1. 处理需要忽略的表
        if (ignoreTableList == null) {
            ignoreTableList = new HashSet<>();
        }

        // 2. 创建逻辑删除字段处理器
        TenantLineHandler tenantLineHandler = new TenantLineHandler() {
            @Override
            public String getTenantIdColumn() {
                return dbField;
            }

            @Override
            public Expression getTenantId() {
                return tenantIdExpression;
            }

            @Override
            public boolean ignoreTable(String tableName) {
                for (String ignoreTable : ignoreTableList) {
                    if (tableName.equalsIgnoreCase(ignoreTable)) {
                        return true;
                    }
                }

                return false;
            }
        };
        super.setTenantLineHandler(tenantLineHandler);

        // 3. 扫描需要忽略的表
        Map<String, Object> mapperBeans = SpringUtils.getApplicationContext().getBeansWithAnnotation(Mapper.class);
        mapperBeans.forEach((key, value) -> {
            if (value instanceof BaseMapper) {
                Class<?> entityClass = ClassUtils.getTypeArgument((Class) ReflectUtil.getFieldValue(ReflectUtil.getFieldValue(value, "h"), "mapperInterface"));
                boolean existField = BeanUtils.isExistField(entityClass, classField);
                if (!existField) {
                    TableName tableName = entityClass.getAnnotation(TableName.class);
                    String ignoreTable = tableName.value();
                    if (StrUtil.isNotBlank(ignoreTable)) {
                        ignoreTableList.add(ignoreTable);
                    } else {
                        ignoreTableList.add(entityClass.getSimpleName());
                        ignoreTableList.add(PropertyNamingStrategy.SnakeCase.fieldName(entityClass.getSimpleName()));
                    }
                }
            }
        });
        log.debug("【{}-初始化忽略表】忽略没有 {} 字段的表：{}", interceptorName, dbField, ignoreTableList);
    }

}
