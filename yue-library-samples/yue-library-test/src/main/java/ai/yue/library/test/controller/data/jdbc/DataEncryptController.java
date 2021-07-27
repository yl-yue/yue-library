package ai.yue.library.test.controller.data.jdbc;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.dao.data.jdbc.DataEncryptDAO;
import ai.yue.library.test.dao.data.jdbc.DataEncryptDAO2;
import ai.yue.library.test.dataobject.jdbc.DataEncryptDO;
import ai.yue.library.test.dataobject.jdbc.DataEncryptDO2;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 数据脱敏测试
 *
 * @author ylyue
 * @since 2021/7/26
 */
@RestController
@RequestMapping("/dataEncrypt")
public class DataEncryptController {

    @Autowired
    DataEncryptDAO encryptDAO;
    @Autowired
    DataEncryptDAO2 encryptDAO2;

    // 1

    @PostMapping("/insert")
    public Result<?> insert(DataEncryptDO userEncryptDO) {
        return R.success(encryptDAO.insert(userEncryptDO));
    }

    @DeleteMapping("/deleteByCellphone")
    public Result<?> deleteByCellphone(String cellphone) {
        return R.success(encryptDAO.deleteByCellphone(cellphone));
    }

    @PutMapping("/updateByCellphone")
    public Result<?> updateByCellphone(JSONObject paramJson) {
        return R.success(encryptDAO.updateByCellphone(paramJson));
    }

    @GetMapping("/get")
    public Result<?> get(String cellphone) {
        return R.success(encryptDAO.get(cellphone));
    }

    // 2

    @PostMapping("/insert2")
    public Result<?> insert2(DataEncryptDO2 userEncryptDO2) {
        return R.success(encryptDAO2.insert(userEncryptDO2));
    }

    @DeleteMapping("/deleteByEmail2")
    public Result<?> deleteByEmail2(String email) {
        return R.success(encryptDAO2.deleteByEmail(email));
    }

    @PutMapping("/updateByEmail2")
    public Result<?> updateByEmail2(JSONObject paramJson) {
        return R.success(encryptDAO2.updateByEmail(paramJson));
    }

    @GetMapping("/get2")
    public Result<?> get2(String email) {
        return R.success(encryptDAO2.get(email));
    }

}
