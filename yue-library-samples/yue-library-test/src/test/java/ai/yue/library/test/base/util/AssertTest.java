package ai.yue.library.test.base.util;

import ai.yue.library.base.exception.ResultException;
import ai.yue.library.base.util.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Sql拼接测试
 *
 * @author ylyue
 * @since 2021/5/19
 */
public class AssertTest {

    @Test
    public void assertException() {
        // 执行此代码段，不会抛出异常
        Assert.notThrow("不抛出异常", () -> {
            System.out.println("不抛出异常");
        });

        // 执行此代码段，会抛出异常
        ResultException resultException = Assertions.assertThrows(ResultException.class, () -> {
            Assert.notThrow("抛出异常", () -> {
                int i = 1 / 0;
                System.out.println(i);
            });
        }, "抛出异常");
        System.out.println();
        System.out.println(resultException);
        Assertions.assertEquals(resultException.getResult().getCode(), 600);
        Assertions.assertEquals(resultException.getResult().getMsg(), "抛出异常");

        // 执行此代码段，会打印异常消息
        Assert.notThrowIfErrorPrintMsg("打印异常消息", System.out::println);
        Assert.notThrowIfErrorPrintMsg("打印异常消息", () -> {
            int i = 1 / 0;
        });

        // 执行此代码段，会打印异常堆栈
        Assert.notThrowIfErrorPrintStackTrace("打印异常堆栈", System.out::println);
        Assert.notThrowIfErrorPrintStackTrace("打印异常堆栈", () -> {
            int i = 1 / 0;
        });
    }

}
