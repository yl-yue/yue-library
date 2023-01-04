package ai.yue.library.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * yue-library测试用例
 * 区别于maven测试用例
 *
 * @author ylyue
 * @since 2018年6月8日
 */
@SpringBootApplication
@MapperScan("ai.yue.library.test.mapper")
public class TestApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TestApplication.class, args);
    }

}
