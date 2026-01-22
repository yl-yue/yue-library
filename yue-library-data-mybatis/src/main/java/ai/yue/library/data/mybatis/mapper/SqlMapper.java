package ai.yue.library.data.mybatis.mapper;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import jakarta.annotation.Nullable;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Sql Mapper
 */
@Mapper
@DS("#dsName")
public interface SqlMapper {

    /**
     * <b>查询一行数据</b>
     *
     * @param sql       要执行的SQL查询
     * @param param     要绑定到查询的参数映射
     * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
     */
    JSONObject queryForJson(String dsName, String sql, @Nullable Map<String, Object> param);

    /**
     * <b>查询多行数据</b>
     *
     * @param sql       要执行的查询SQL
     * @param param     要绑定到查询的参数映射
     * @return 多行查询结果
     */
    List<JSONObject> queryForJsonList(String dsName, String sql, @Nullable Map<String, Object> param);

    /**
     * <b>查询一行数据</b>
     *
     * @param sql         要执行的SQL查询
     * @param param       要绑定到查询的参数映射
     * @return 可以是一个正确的单行查询结果、或null、或查询结果是多条数据而引发的预期错误异常
     */
    String queryForStr(String dsName, String sql, @Nullable Map<String, Object> param);

}
