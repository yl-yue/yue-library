package ai.yue.library.test.jdbc;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import org.junit.jupiter.api.Test;

/**
 * @author ylyue
 * @since 2021/5/19
 */
public class DruidTest {

    @Test
    public void addCondition() {
        String sql1 = "SELECT * FROM tableName";
        String sql2 = "SELECT * FROM tableName WHERE 1=1";
        String sql3 = "SELECT * FROM tableName WHERE 1=1 AND 2=2";
        String sql4 = "SELECT * FROM tableName WHERE delete_time = 0";
        String sql5 = "SELECT * FROM tableName WHERE delete_time = 0 LIMIT 0,10";
        String sql6 = "SELECT a.* FROM tableName a, (SELECT id FROM tableName WHERE 1=1 LIMIT 0,10) b WHERE a.id = b.id";
        System.out.println(SQLUtils.addCondition(sql1, "delete_time = 0", DbType.mysql));
        System.out.println();
        System.out.println(SQLUtils.addCondition(sql2, "delete_time = 0", DbType.mysql));
        System.out.println();
        System.out.println(SQLUtils.addCondition(sql3, "delete_time = 0", DbType.mysql));
        System.out.println();
        System.out.println(SQLUtils.addCondition(sql4, "delete_time = 0", DbType.mysql));
        System.out.println();
        System.out.println(SQLUtils.addCondition(sql5, "delete_time = 0", DbType.mysql));
        System.out.println();
        System.out.println(SQLUtils.addCondition(sql6, "delete_time = 0", DbType.mysql));
    }

}
