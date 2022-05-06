package ai.yue.library.data.jdbc.provider;

import ai.yue.library.base.constant.MatchEnum;
import ai.yue.library.base.util.IdUtils;
import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import ai.yue.library.data.jdbc.dto.DataFillDTO;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动填充数据提供
 * <p>继承此类重写方法并配置为Bean即可</p>
 *
 * @author ylyue
 * @since 2021/7/27
 */
@Slf4j
public abstract class FillDataProvider {

    private static FillDataProvider getFillDataProvider(JdbcProperties jdbcProperties, String tableName) {
        // 获得Bean
        FillDataProvider fillDataProvider = null;
        try {
            fillDataProvider = SpringUtils.getBean(FillDataProvider.class);
        } catch (Exception e) {
            log.debug("【数据填充】未配置{}，数据填充特性未生效。", FillDataProvider.class);
            return fillDataProvider;
        }

        // 执行匹配方式
        MatchEnum dataFillTableNameMatchEnum = jdbcProperties.getDataFillTableNameMatchEnum();
        List<String> dataFillTableNames = jdbcProperties.getDataFillTableNames();
        boolean matchResult = dataFillTableNameMatchEnum.getExecResult(tableName, dataFillTableNames);

        /*
         * 匹配方式：匹配到就执行（逻辑教给匹配器控制）
         * 排除方式：匹配到就不执行（逻辑教给匹配器控制）
         */
        if (matchResult == true) {
            return fillDataProvider;
        } else {
            return null;
        }
    }

    /**
     * 获得插入填充参数
     */
    public static JSONObject getInsertParamJson(JdbcProperties jdbcProperties, String tableName) {
        JSONObject paramJson = new JSONObject();
        FillDataProvider fillDataProvider = getFillDataProvider(jdbcProperties, tableName);

        if (fillDataProvider == null) {
            return paramJson;
        }

        List<DataFillDTO> insertFillDTOList = fillDataProvider.getInsertFillDTO(jdbcProperties);
        insertFillDTOList.forEach(insertFillDTO -> {
            paramJson.put(insertFillDTO.getFillFieldName(), insertFillDTO.getFillFieldValue());
        });

        return paramJson;
    }

    /**
     * 获得更新填充参数
     */
    public static JSONObject getUpdateParamJson(JdbcProperties jdbcProperties, String tableName) {
        JSONObject paramJson = new JSONObject();
        FillDataProvider fillDataProvider = getFillDataProvider(jdbcProperties, tableName);

        if (fillDataProvider == null) {
            return paramJson;
        }

        List<DataFillDTO> updateFillDTOList = fillDataProvider.getUpdateFillDTO(jdbcProperties);
        updateFillDTOList.forEach(updateFillDTO -> {
            paramJson.put(updateFillDTO.getFillFieldName(), updateFillDTO.getFillFieldValue());
        });

        return paramJson;
    }

    /**
     * 插入填充（每次动态获取）
     */
    public List<DataFillDTO> getInsertFillDTO(JdbcProperties jdbcProperties) {
        List<DataFillDTO> insertFillDTOList = new ArrayList<>();
        insertFillDTOList.add(new DataFillDTO(jdbcProperties.getFieldDefinitionUuid(), IdUtils.getSimpleUUID()));
        insertFillDTOList.add(new DataFillDTO(jdbcProperties.getFieldDefinitionSortIdx(), 0));
        return insertFillDTOList;
    }

    /**
     * 更新填充（每次动态获取）
     */
    public List<DataFillDTO> getUpdateFillDTO(JdbcProperties jdbcProperties) {
        List<DataFillDTO> updateFillDTOList = new ArrayList<>();
        return updateFillDTOList;
    }

}
