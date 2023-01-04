//package ai.yue.library.test.dao.data.jdbc;
//
//import ai.yue.library.data.jdbc.dao.AbstractRepository;
//import ai.yue.library.test.dataobject.jdbc.DataEncryptDO2;
//import com.alibaba.fastjson.JSONObject;
//import org.springframework.stereotype.Repository;
//
///**
// * 数据脱敏
// *
// * @author ylyue
// * @since 2021/7/26
// */
//@Repository
//public class DataEncryptDAO2 extends AbstractRepository<DataEncryptDO2> {
//
//    @Override
//    protected String tableName() {
//        return "data_encrypt_2";
//    }
//
//    /**
//     * 删除
//     *
//     * @param email 邮箱
//     * @return
//     */
//    public long deleteByEmail(String email) {
//        JSONObject paramJson = new JSONObject();
//        paramJson.put("email", email);
//        return db.delete(tableName(), paramJson);
//    }
//
//    /**
//     * 更新-ByEmail
//     *
//     * @param paramJson 更新所用到的参数（where条件参数不会用于set值的更新）
//     * @return
//     */
//    public Long updateByEmail(JSONObject paramJson) {
//        String[] conditions = {"email"};
//        return db.update(tableName(), paramJson, conditions);
//    }
//
//    /**
//     * 单个
//     *
//     * @param email
//     */
//    public DataEncryptDO2 get(String email) {
//        JSONObject paramJson = new JSONObject();
//        paramJson.put("email", email);
//        return db.get(tableName, paramJson, mappedClass);
//    }
//
//}
