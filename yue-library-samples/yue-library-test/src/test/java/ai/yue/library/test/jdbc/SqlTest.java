package ai.yue.library.test.jdbc;

import ai.yue.library.base.util.Sql;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Sql拼接测试
 *
 * @author ylyue
 * @since 2021/5/19
 */
public class SqlTest {

    @Test
    public void sqlAppend() {
        String condition1 = "";
        String sql1 = Sql.sql("SELECT * FROM tableName")
                .append(" WHERE 1=1 ", true)
                .append(" AND 2=2 ", condition1)
                .append(" AND delete_time = 0 ", StrUtil.isEmptyIfStr(condition1))
                .getSqlString();
        System.out.println(sql1);

        StringBuffer sql2 = new StringBuffer();
        Sql.append(sql2, "SELECT * FROM tableName");
        Sql.append(sql2, " WHERE 1=1 ", true);
        Sql.append(sql2, " AND 2=2 ", condition1);
        Sql.append(sql2, " AND delete_time = 0 ", StrUtil.isEmptyIfStr(condition1));
        System.out.println(sql2);

        Assertions.assertEquals(sql1.toString(), sql2.toString());
    }

}
