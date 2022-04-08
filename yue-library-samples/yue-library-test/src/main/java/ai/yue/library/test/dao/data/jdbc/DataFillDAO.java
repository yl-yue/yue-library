package ai.yue.library.test.dao.data.jdbc;

import ai.yue.library.data.jdbc.dao.AbstractRepository;
import ai.yue.library.test.dataobject.jdbc.DataAuditDO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

/**
 * 数据审计
 *
 * @author ylyue
 * @since 2021/7/26
 */
@Repository
public class DataFillDAO extends AbstractRepository<DataAuditDO> {

    @Override
    protected String tableName() {
        return "data_fill";
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

}
