//package ai.yue.library.test.dao.data.jdbc;
//
//import ai.yue.library.data.jdbc.dao.AbstractRepository;
//import ai.yue.library.test.dataobject.jdbc.DataAuditDO;
//import com.alibaba.fastjson.JSONObject;
//import org.springframework.stereotype.Repository;
//
//import javax.annotation.PostConstruct;
//import java.util.List;
//
///**
// * 数据审计
// *
// * @author ylyue
// * @since 2021/7/26
// */
//@Repository
//public class DataAuditDAO extends AbstractRepository<DataAuditDO> {
//
//    @Override
//    protected String tableName() {
//        return "data_audit";
//    }
//
//    @PostConstruct
//    private void init() {
//        db = db.clone();
//        db.getJdbcProperties().setEnableLogicDeleteFilter(true);
//    }
//
//    /**
//     * 删除
//     *
//     * @param cellphone 手机号
//     * @return
//     */
//    public long deleteByCellphone(String cellphone) {
//        JSONObject paramJson = new JSONObject();
//        paramJson.put("cellphone", cellphone);
//        return db.deleteLogic(tableName(), paramJson);
//    }
//
//    /**
//     * 删除
//     *
//     * @param cellphones 手机号
//     */
//    public void deleteByCellphones(List<String> cellphones) {
//        JSONObject[] paramJsons = new JSONObject[cellphones.size()];
//        for (int i = 0; i < paramJsons.length; i++) {
//            paramJsons[i] = new JSONObject().fluentPut("cellphone", cellphones.get(i));
//        }
//
//        db.deleteBatchLogic(tableName, paramJsons);
//    }
//
//    /**
//     * 更新-ByCellphone
//     *
//     * @param paramJson 更新所用到的参数（where条件参数不会用于set值的更新）
//     * @return
//     */
//    public Long updateByCellphone(JSONObject paramJson) {
//        String[] conditions = {"cellphone"};
//        return db.update(tableName(), paramJson, conditions);
//    }
//
//    /**
//     * 单个
//     *
//     * @param cellphone
//     * @return
//     */
//    public DataAuditDO get(String cellphone) {
//        JSONObject paramJson = new JSONObject();
//        paramJson.put("cellphone", cellphone);
//        return db.get(tableName, paramJson, mappedClass);
//    }
//
//}
