package ai.yue.library.test.data.log;

import ai.yue.library.data.log.util.LogUtils;
import ai.yue.library.data.mybatis.constant.DbCrudEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * LogUtils 工具类测试
 *
 * @author ylyue
 * @since 2025/5/13
 */
class LogUtilsTest {

    @Test
    void getOperTypeFromHttpMethod_post() {
        assertEquals(DbCrudEnum.C, LogUtils.getOperTypeFromHttpMethod("POST"));
    }

    @Test
    void getOperTypeFromHttpMethod_get() {
        assertEquals(DbCrudEnum.R, LogUtils.getOperTypeFromHttpMethod("GET"));
    }

    @Test
    void getOperTypeFromHttpMethod_put() {
        assertEquals(DbCrudEnum.U, LogUtils.getOperTypeFromHttpMethod("PUT"));
    }

    @Test
    void getOperTypeFromHttpMethod_patch() {
        assertEquals(DbCrudEnum.U, LogUtils.getOperTypeFromHttpMethod("PATCH"));
    }

    @Test
    void getOperTypeFromHttpMethod_delete() {
        assertEquals(DbCrudEnum.D, LogUtils.getOperTypeFromHttpMethod("DELETE"));
    }

    @Test
    void getOperTypeFromHttpMethod_null() {
        assertEquals(DbCrudEnum.R, LogUtils.getOperTypeFromHttpMethod(null));
    }

    @Test
    void getOperTypeFromHttpMethod_empty() {
        assertEquals(DbCrudEnum.R, LogUtils.getOperTypeFromHttpMethod(""));
    }

    @Test
    void getOperTypeFromHttpMethod_unknown() {
        assertEquals(DbCrudEnum.R, LogUtils.getOperTypeFromHttpMethod("OPTIONS"));
    }

    @Test
    void getOperTypeFromHttpMethod_caseInsensitive() {
        assertEquals(DbCrudEnum.C, LogUtils.getOperTypeFromHttpMethod("post"));
        assertEquals(DbCrudEnum.U, LogUtils.getOperTypeFromHttpMethod("put"));
    }

}
