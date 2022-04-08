package ai.yue.library.data.jdbc.provider;

import ai.yue.library.base.util.IdUtils;
import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import ai.yue.library.data.jdbc.dto.DataFillDTO;
import com.alibaba.fastjson.JSONObject;
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
public abstract class FillDataProvider {

    @Autowired
    JdbcProperties jdbcProperties;

    /**
     * 获得插入填充参数
     */
    public static JSONObject getInsertParamJson() {
        JSONObject paramJson = new JSONObject();
        FillDataProvider fillDataProvider = null;
        try {
            fillDataProvider = SpringUtils.getBean(FillDataProvider.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fillDataProvider == null) {
            return paramJson;
        }

        List<DataFillDTO> insertFillDTOList = fillDataProvider.getInsertFillDTO();
        insertFillDTOList.forEach(insertFillDTO -> {
            paramJson.put(insertFillDTO.getFillFieldName(), insertFillDTO.getFillFieldValue());
        });

        return paramJson;
    }

    /**
     * 获得更新填充参数
     */
    public static JSONObject getUpdateParamJson() {
        JSONObject paramJson = new JSONObject();
        FillDataProvider fillDataProvider = null;
        try {
            fillDataProvider = SpringUtils.getBean(FillDataProvider.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fillDataProvider == null) {
            return paramJson;
        }

        List<DataFillDTO> updateFillDTOList = fillDataProvider.getUpdateFillDTO();
        updateFillDTOList.forEach(updateFillDTO -> {
            paramJson.put(updateFillDTO.getFillFieldName(), updateFillDTO.getFillFieldValue());
        });

        return paramJson;
    }

    /**
     * 插入填充（每次动态获取）
     */
    public List<DataFillDTO> getInsertFillDTO() {
        List<DataFillDTO> insertFillDTOList = new ArrayList<>();
        insertFillDTOList.add(new DataFillDTO(jdbcProperties.getFieldDefinitionUuid(), IdUtils.getSimpleUUID()));
        insertFillDTOList.add(new DataFillDTO(jdbcProperties.getFieldDefinitionSortIdx(), 0));
        return insertFillDTOList;
    }

    /**
     * 更新填充（每次动态获取）
     */
    public List<DataFillDTO> getUpdateFillDTO() {
        List<DataFillDTO> updateFillDTOList = new ArrayList<>();
        return updateFillDTOList;
    }

}
