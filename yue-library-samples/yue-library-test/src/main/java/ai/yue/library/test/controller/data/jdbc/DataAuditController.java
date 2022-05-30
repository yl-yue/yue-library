package ai.yue.library.test.controller.data.jdbc;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.jdbc.provider.AuditUserProvider;
import ai.yue.library.test.dao.data.jdbc.DataAuditDAO;
import ai.yue.library.test.dataobject.jdbc.DataAuditDO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据审计测试
 *
 * @author ylyue
 * @since 2021/7/26
 */
@RestController
@RequestMapping("/dataAudit")
public class DataAuditController {

    @Autowired
    DataAuditDAO dataAuditDAO;

    @Bean
    public AuditUserProvider auditUserProvider() {
        return new AuditUserProvider() {
            // 在你的应用程序中，如何获得当前用户信息，一般从Token中获取
            @Override
            public String getUser() {
                return "ylyue";
            }

            @Override
            public String getUserUuid() {
                return "8fb1e1556cc84ba880d5a794e7b5f9e7";
            }
        };
    }

    @PostMapping("/insert")
    public Result<?> insert(DataAuditDO dataAuditDO) {
        return R.success(dataAuditDAO.insert(dataAuditDO));
    }

    @DeleteMapping("/deleteByCellphone")
    public Result<?> deleteByCellphone(String cellphone) {
        return R.success(dataAuditDAO.deleteByCellphone(cellphone));
    }

    @DeleteMapping("/deleteByCellphones")
    public Result<?> deleteByCellphones(List<String> cellphones) {
        dataAuditDAO.deleteByCellphones(cellphones);
        return R.success();
    }

    @PutMapping("/updateByCellphone")
    public Result<?> updateByCellphone(JSONObject paramJson) {
        return R.success(dataAuditDAO.updateByCellphone(paramJson));
    }

    @GetMapping("/get")
    public Result<?> get(String cellphone) {
        return R.success(dataAuditDAO.get(cellphone));
    }

}
