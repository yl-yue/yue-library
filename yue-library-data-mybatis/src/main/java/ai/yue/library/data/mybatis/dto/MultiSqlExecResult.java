package ai.yue.library.data.mybatis.dto;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;

/**
 * 多 SQL 执行结果
 */
@Data
public class MultiSqlExecResult {

    /**
     * 执行开始时间
     */
    private Long startTime;
    /**
     * 执行结束时间
     */
    private Long endTime;
    /**
     * 执行耗时（毫秒）
     */
    private Long intervalMs;
    /**
     * 是否执行成功
     */
    private Boolean isSuccess;
    /**
     * 是否分页
     */
    private Boolean isPage;
    /**
     * 执行结果
     */
    private List<JSONObject> resultList;
    /**
     * 执行结果（分页）
     */
    private PageInfo<JSONObject> resultPage;

}
