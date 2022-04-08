package ai.yue.library.test.controller.data.jdbc;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.jdbc.config.properties.JdbcProperties;
import ai.yue.library.data.jdbc.dto.DataFillDTO;
import ai.yue.library.data.jdbc.provider.FillDataProvider;
import ai.yue.library.test.dao.data.jdbc.DataFillDAO;
import ai.yue.library.test.dataobject.jdbc.DataEncryptDO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据填充测试
 *
 * @author ylyue
 * @since 2021/7/26
 */
@RestController
@RequestMapping("/dataFill")
public class DataFillController {

    @Autowired
    DataFillDAO dataFillDAO;
    @Autowired
    JdbcProperties jdbcProperties;

    @Bean
    public FillDataProvider fillDataProvider() {
        return new FillDataProvider() {
            @Override
            public List<DataFillDTO> getInsertFillDTO() {
                return super.getInsertFillDTO();
            }

            @Override
            public List<DataFillDTO> getUpdateFillDTO() {
                List<DataFillDTO> updateFillDTOList = super.getUpdateFillDTO();
                updateFillDTOList.add(new DataFillDTO(jdbcProperties.getDataAuditProperties().getFieldNameUpdateUser(), "ylyue"));
                return updateFillDTOList;
            }
        };
    }

    @PostMapping("/insert")
    public Result<?> insert(DataEncryptDO userEncryptDO) {
        return R.success(dataFillDAO.insert(userEncryptDO));
    }

    @PutMapping("/updateByCellphone")
    public Result<?> updateByCellphone(JSONObject paramJson) {
        return R.success(dataFillDAO.updateByCellphone(paramJson));
    }

}
