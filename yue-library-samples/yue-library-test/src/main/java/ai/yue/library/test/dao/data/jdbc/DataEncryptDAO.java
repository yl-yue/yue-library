package ai.yue.library.test.dao.data.jdbc;

import ai.yue.library.data.jdbc.dao.AbstractRepository;
import ai.yue.library.test.dataobject.jdbc.DataEncryptDO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Repository;

/**
 * 数据脱敏
 *
 * @author ylyue
 * @since 2021/7/26
 */
@Repository
public class DataEncryptDAO extends AbstractRepository<DataEncryptDO> {

    @Override
    protected String tableName() {
        return "data_encrypt";
    }

    /**
     * 删除
     *
     * @param cellphone 手机号
     * @return
     */
    public long deleteByCellphone(String cellphone) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("cellphone", cellphone);
        return db.delete(tableName(), paramJson);
    }

    /**
     * 更新-ByCellphone
     *
     * @param paramJson 更新所用到的参数（where条件参数不会用于set值的更新）
     * @return
     */
    public Long updateByCellphone(JSONObject paramJson) {
        String[] conditions = {"cellphone"};
        return db.update(tableName(), paramJson, conditions);
    }

    /**
     * 单个
     *
     * @param cellphone
     */
    public DataEncryptDO get(String cellphone) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("cellphone", cellphone);
        return db.get(tableName, paramJson, mappedClass);
    }

}
