package ai.yue.library.data.mybatis.interceptor;

import ai.yue.library.base.util.BeanUtils;
import ai.yue.library.base.util.ClassUtils;
import ai.yue.library.base.util.SpringUtils;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.baomidou.mybatisplus.extension.toolkit.SqlParserUtils;
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
import cn.hutool.v7.core.text.StrUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.sql.Connection;
import java.util.Map;
import java.util.Set;

/**
 * 字段拦截器
 * - 租户拦截 + 逻辑拦截 ≈ 10-15ms
 *
 * @author yl-yue
 * @since 2023/2/20
 */
@Slf4j
public class FieldInterceptor extends TenantLineInnerInterceptor implements ApplicationRunner {

    protected String interceptorName;
    protected Set<String> ignoreDsList;
    protected Set<String> ignoreTableList;
    protected String classField;
    protected String dbField;
    protected Expression tenantIdExpression;

    /**
     * 用于匹配的 where 关键词片段
     */
    private String[] whereSqlKeywords;

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
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

    /**
     * 租户拦截 + 逻辑拦截 ≈ 10-15ms
     */
    private boolean isExecute(BoundSql boundSql) {
        TenantLineHandler tenantLineHandler = getTenantLineHandler();
        if (tenantLineHandler == null) {
            return false;
        }

        // 忽略不需要处理的数据源
        if (!ignoreDsList.isEmpty()) {
            String dsName = DynamicDataSourceContextHolder.peek();
            for (String ignoreDs : ignoreDsList) {
                if (ignoreDs.equalsIgnoreCase(dsName)) {
                    return false;
                }
            }
        }

        String sql = boundSql.getSql();
        int whereIndex = StrUtil.indexOfIgnoreCase(sql, "where", 4);
        String subSql = StrUtil.subSuf(sql, whereIndex);
        return !StrUtil.containsAnyIgnoreCase(subSql, whereSqlKeywords);
    }

    /**
     * 扫描所有的Mapper类，查找实体
     * 忽略没有dbField字段的表
     */
    @Override
    public void run(ApplicationArguments args) {
        // 1. 处理需要忽略的表
        ignoreTableList.add("TABLES");
        ignoreTableList.add("COLUMNS");
        initWhereSqlKeywords();

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
                    String simpleTableName = SqlParserUtils.removeWrapperSymbol(tableName);
                    if (simpleTableName.equalsIgnoreCase(ignoreTable)) {
                        return true;
                    }
                }

                return false;
            }
        };
        super.setTenantLineHandler(tenantLineHandler);

        // 3. 扫描需要忽略的表
        Map<String, Object> mapperBeans = SpringUtils.getApplicationContext().getBeansWithAnnotation(Mapper.class);
        for (Object value : mapperBeans.values()) {
            if (value instanceof BaseMapper) {
                // 获得 Mapper 对应的 entity
                Class<?> entityClass = getEntityClass((BaseMapper) value);
                if (entityClass == null) {
                    continue;
                }

                // 跳过存在 dbField 字段的 entity
                boolean existField = BeanUtils.isExistField(entityClass, classField);
                if (existField) {
                    continue;
                }

                // 添加不存在 dbField 字段的 entity 到 ignoreTableList
                TableName tableName = entityClass.getAnnotation(TableName.class);
                String simpleName = entityClass.getSimpleName();
                if (tableName != null) {
                    String ignoreTable = tableName.value();
                    if (StrUtil.isNotBlank(ignoreTable)) {
                        ignoreTableList.add(ignoreTable);
                        continue;
                    }
                }
                ignoreTableList.add(simpleName);
                ignoreTableList.add(PropertyNamingStrategy.SnakeCase.fieldName(simpleName));
            }
        }

        log.debug("【{}-初始化忽略表】忽略没有 {} 字段的表：{}", interceptorName, dbField, ignoreTableList);
    }

    private void initWhereSqlKeywords() {
        whereSqlKeywords = new String[]{
                StrUtil.format("{}=", dbField),
                StrUtil.format("{} =", dbField),
                StrUtil.format("{}<>", dbField),
                StrUtil.format("{} <>", dbField),
                StrUtil.format("{}!=", dbField),
                StrUtil.format("{} !=", dbField),
                StrUtil.format("{}>", dbField),
                StrUtil.format("{} >", dbField),
                StrUtil.format("{} IS", dbField),
                StrUtil.format("{} NOT", dbField),
                StrUtil.format("{} INT", dbField)
        };
    }

    private Class<?> getEntityClass(BaseMapper mapperProxy) {
        try {
            // 尝试获取实际的 Mapper 接口
            Class<?> mapperInterface = mapperProxy.getClass().getInterfaces()[0];
            // 获取 BaseMapper 的泛型参数
            return ClassUtils.getTypeArgument(mapperInterface);
        } catch (Exception e) {
            log.warn("无法获取 Mapper 的实体类: {}", e.getMessage());
            return null;
        }
    }

}
