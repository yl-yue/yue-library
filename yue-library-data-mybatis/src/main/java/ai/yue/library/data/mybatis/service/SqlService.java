package ai.yue.library.data.mybatis.service;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.util.DateUtils;
import ai.yue.library.base.util.time.interval.TimeInterval;
import ai.yue.library.data.mybatis.dto.MultiSqlExecResult;
import ai.yue.library.data.mybatis.dto.PageIPO;
import ai.yue.library.data.mybatis.mapper.SqlMapper;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import cn.hutool.v7.core.text.StrUtil;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Sql Service<br>
 * - 配置 mybatis-plus.configuration.call-setters-on-nulls = true 解决 null 值映射问题，可返回 null 字段
 */
@Service
public class SqlService {

    @Resource
    SqlMapper sqlMapper;

    /**
     * <b>查询一行数据</b>
     *
     * @param sql       要执行的SQL查询
     * @param param     要绑定到查询的参数映射
     * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
     */
    public JSONObject queryForJson(String dsName, String sql, @Nullable Map<String, Object> param) {
        return sqlMapper.queryForJson(dsName, sql, param);
    }

    /**
     * <b>查询一行数据</b>
     *
     * @param sql               要执行的SQL查询
     * @param param             要绑定到查询的参数映射
     * @param javaBeanClass     查询结果映射类型，只支持JavaBean类型
     * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
     */
    public <T> T queryForJavaBean(String dsName, String sql, @Nullable Map<String, Object> param, Class<T> javaBeanClass) {
        JSONObject json = queryForJson(dsName, sql, param);
        return Convert.toJavaBean(json, javaBeanClass);
    }

    /**
     * <b>查询多行数据</b>
     *
     * @param sql       要执行的查询SQL
     * @param param     要绑定到查询的参数映射
     * @return 多行查询结果
     */
    public List<JSONObject> queryForJsonList(String dsName, String sql, @Nullable Map<String, Object> param) {
        return sqlMapper.queryForJsonList(dsName, sql, param);
    }

    /**
     * <b>查询多行数据</b>
     *
     * @param sql               要执行的查询SQL
     * @param param             要绑定到查询的参数映射
     * @param javaBeanClass     查询结果映射类型，只支持JavaBean类型
     * @return 多行查询结果
     */
    public <T> List<T> queryForJavaBeanList(String dsName, String sql, @Nullable Map<String, Object> param, Class<T> javaBeanClass) {
        List<JSONObject> list = sqlMapper.queryForJsonList(dsName, sql, param);
        return Convert.toList(list, javaBeanClass);
    }

    /**
     * <b>查询一个数据</b>
     *
     * @param sql               要执行的SQL查询
     * @param param             要绑定到查询的参数映射
     * @param simpleTypeClass   查询结果映射类型，只支持简单类型（如：Long, String, Boolean、BigDecimal、LocalDateTime）
     * @return 可以是一个正确的单个查询结果、或null、或查询结果是多条数据而引发的预期错误异常
     */
    public <T> T queryForSimpleType(String dsName, String sql, @Nullable Map<String, Object> param, Class<T> simpleTypeClass) {
        String str = sqlMapper.queryForStr(dsName, sql, param);
        return Convert.toObject(str, simpleTypeClass);
    }

    // ========== 执行 sql ==========

    /**
     * 执行 sql 语句，返回一行结果
     * - 支持 select、insert、update、delete 语句
     * - 支持 ddl 语句
     *
     * @param sql       要执行的 sql 语句
     * @param param     要绑定的参数
     * @return 返回一行结果，可以是一行查询结果，也可以是 ddl 语句执行的结果
     */
    public JSONObject execSqlForJson(String dsName, String sql, @Nullable Map<String, Object> param) {
        return sqlMapper.queryForJson(dsName, sql, param);
    }

    /**
     * 执行 sql 语句，返回多行结果
     * - 支持 select、insert、update、delete 语句
     * - 支持 ddl 语句
     *
     * @param sql       要执行的 sql 语句
     * @param param     要绑定的参数
     * @return 返回多行结果，可以是多行查询结果，也可以是多个 ddl 语句执行的结果
     */
    public List<JSONObject> execSqlForJsonList(String dsName, String sql, @Nullable Map<String, Object> param) {
        return sqlMapper.queryForJsonList(dsName, sql, param);
    }

    /**
     * 执行多条 sql 语句，返回多个执行结果
     * - 支持 select、insert、update、delete 语句
     * - 支持 ddl 语句
     *
     * @param sql       要执行的多条 sql 语句
     * @param param     要绑定的参数
     * @param pageIPO   分页请求参数，亦可作分页限制（避免用户一次性查询所有数据，如果 sql 中已包含分页，此参数将被忽略）
     * @return 返回多个执行结果
     */
    public List<MultiSqlExecResult> execMultiSqlForJsonList(String dsName, String sql, @Nullable Map<String, Object> param, @Nullable PageIPO pageIPO) {
        String[] splitSql = sql.split(";");
        List<MultiSqlExecResult> multiResult = new ArrayList<>();
        TimeInterval timeInterval = DateUtils.timer();
        for (int i = 0; i < splitSql.length; i++) {
            String singleSql = splitSql[i];
            if (StrUtil.isBlank(singleSql) || singleSql.length() < 5) {
                continue;
            }

            MultiSqlExecResult multiSqlExecResult = new MultiSqlExecResult();
            multiSqlExecResult.setStartTime(timeInterval.start());
            List<JSONObject> singleResult;
            boolean isPage = false;
            try {
                String lowerCaseSql = singleSql.trim().toLowerCase();
                if (pageIPO != null && lowerCaseSql.startsWith("select ")
                        // 忽略已经包含分页的 sql
                        && !lowerCaseSql.contains("limit ")
                        && !lowerCaseSql.contains("offset ")
                        // 忽略 select for update 语句
                        && !lowerCaseSql.contains("for update")
                        // 忽略 count、sum 等聚合函数
                        && !lowerCaseSql.contains("count(")
                        && !lowerCaseSql.contains("sum(")
                        && !lowerCaseSql.contains("avg(")
                        && !lowerCaseSql.contains("max(")
                        && !lowerCaseSql.contains("min(")
                ) {
                    isPage = true;
                    PageHelper.startPage(pageIPO.getPageNum(), pageIPO.getPageSize());
                    if (pageIPO.getOrderBy() != null) {
                        PageHelper.orderBy(pageIPO.getOrderBy());
                    }
                }
                singleResult = sqlMapper.queryForJsonList(dsName, singleSql, param);
                multiSqlExecResult.setIsSuccess(true);
            } catch (Exception e) {
                multiSqlExecResult.setIsSuccess(false);
                String errorMsg;
                if (e instanceof BadSqlGrammarException) {
                    errorMsg = ((BadSqlGrammarException) e).getSQLException().getMessage();
                } else {
                    errorMsg = e.getMessage();
                }

                singleResult = List.of(JSONObject.of("error", errorMsg));
            }
            multiSqlExecResult.setIntervalMs(timeInterval.intervalMs());
            multiSqlExecResult.setEndTime(timeInterval.start());

            if (isPage && multiSqlExecResult.getIsSuccess()) {
                multiSqlExecResult.setIsPage(true);
                multiSqlExecResult.setResultPage(PageInfo.of(singleResult));
            } else {
                multiSqlExecResult.setIsPage(false);
                multiSqlExecResult.setResultList(singleResult);
            }
            multiResult.add(multiSqlExecResult);
        }

        return multiResult;
    }

}
