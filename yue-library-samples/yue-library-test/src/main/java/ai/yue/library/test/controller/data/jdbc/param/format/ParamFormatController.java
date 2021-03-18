package ai.yue.library.test.controller.data.jdbc.param.format;

import ai.yue.library.base.util.ListUtils;
import ai.yue.library.base.util.MapUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.jdbc.client.Db;
import ai.yue.library.data.jdbc.constant.DbConstant;
import ai.yue.library.data.jdbc.constant.DbUpdateEnum;
import ai.yue.library.test.ipo.ParamFormatIPO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC参数美化测试
 *
 * @author ylyue
 * @since 2021/1/13
 */
@RestController
@RequestMapping("/paramFormat")
public class ParamFormatController {

    @Autowired
    Db db;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    String tableName = "param_format";
    
    @PostMapping("/insertNotParamFormat")
    public Result<?> insertNotParamFormat(ParamFormatIPO paramFormatIPO) {
        // 1. 移除空对象
        JSONObject paramJson = getParamJson(paramFormatIPO);
        MapUtils.removeEmpty(paramJson);

        // 2. 插入源初始化
        tableName = db.getDialect().getWrapper().wrap(tableName);
        paramJson = db.getDialect().getWrapper().wrap(paramJson);

        // 2. 创建JdbcInsert实例
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        // 设置表名
        simpleJdbcInsert.setTableName(tableName);
        // 设置主键名，添加成功后返回主键的值
        simpleJdbcInsert.setGeneratedKeyName(DbConstant.PRIMARY_KEY);

        // 3. 设置ColumnNames
        List<String> keys = MapUtils.keyList(paramJson);
        List<String> columnNames = ListUtils.toList(db.getMetaData(tableName).getColumnNames());
        List<String> insertColumn = ListUtils.keepSameValue(keys, (List<String>) db.getDialect().getWrapper().wrap(columnNames));
        simpleJdbcInsert.setColumnNames(insertColumn);

        // 3. 执行
        return R.success(simpleJdbcInsert.executeAndReturnKey(paramJson).longValue());
    }

    @PostMapping("/insert")
    public Result<?> insert(ParamFormatIPO paramFormatIPO) {
        JSONObject paramJson = getParamJson(paramFormatIPO);
        return R.success(db.insert(tableName, paramJson));
    }

    @PutMapping("/updateByIdNotParamFormat")
    public Result<?> updateByIdNotParamFormat(ParamFormatIPO paramFormatIPO) {
        JSONObject paramJson = getParamJson(paramFormatIPO);
        List<JSONObject> paramList = new ArrayList<>();
        paramList.add(paramJson);
        db.updateByIdNotParamFormat(tableName, ListUtils.toJsons(paramList), DbUpdateEnum.NORMAL);
        return R.success();
    }

    @PutMapping("/updateById")
    public Result<?> updateById(ParamFormatIPO paramFormatIPO) {
        JSONObject paramJson = getParamJson(paramFormatIPO);
        db.updateById(tableName, paramJson);
        return R.success();
    }

    private JSONObject getParamJson(ParamFormatIPO paramFormatIPO) {
        Long id = paramFormatIPO.getId();
        Character character = paramFormatIPO.getCharacter();
        LocalDateTime localDateTime = paramFormatIPO.getLocalDateTime();
        JSONObject jsonObject = paramFormatIPO.getJsonObject();
        JSONObject paramJson = new JSONObject();
        if (id != null) {
            paramJson.put("id", id);
        }
        paramJson.put("character", character);
        paramJson.put("localDateTime", localDateTime);
        paramJson.put("jsonObject", jsonObject);
        return paramJson;
    }

}
