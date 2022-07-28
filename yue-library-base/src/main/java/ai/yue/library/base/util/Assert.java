package ai.yue.library.base.util;

import ai.yue.library.base.exception.ResultException;
import lombok.extern.slf4j.Slf4j;

/**
 * RESTful断言
 *
 * @author ylyue
 * @since  2022/7/22
 */
@Slf4j
public class Assert extends cn.hutool.core.lang.Assert {

    /**
     * 断言-执行给定代码块不会抛出异常
     * <p>若不是期望的结果，执行代码块时出现错误，将抛出ResultException</p>
     *
     * @param throwMsg  执行代码块错误时抛出的ResultException message
     * @param lambdaRun lambda给定代码块，示例：{@code () -&gt; {代码块}}
     */
    public static void notThrow(String throwMsg, Runnable lambdaRun) {
        try {
            lambdaRun.run();
        } catch (Exception e) {
            throw new ResultException(throwMsg);
        }
    }

    /**
     * 断言-执行给定代码块不会抛出异常
     * <p>若不是期望的结果，执行代码块时出现错误，将打印printMsg</p>
     *
     * @param printMsg  执行代码块错误时，打印的信息
     * @param lambdaRun lambda给定代码块，示例：{@code () -&gt; {代码块}}
     */
    public static void notThrowIfErrorPrintMsg(String printMsg, Runnable lambdaRun) {
        try {
            lambdaRun.run();
        } catch (Exception e) {
            log.warn(printMsg);
        }
    }

    /**
     * 断言-执行给定代码块不会抛出异常
     * <p>若不是期望的结果，执行代码块时出现错误，将打印printMsg与堆栈跟踪信息</p>
     *
     * @param printMsg  执行代码块错误时，打印的信息
     * @param lambdaRun lambda给定代码块，示例：{@code () -&gt; {代码块}}
     */
    public static void notThrowIfErrorPrintStackTrace(String printMsg, Runnable lambdaRun) {
        try {
            lambdaRun.run();
        } catch (Exception e) {
            log.warn(printMsg);
            e.printStackTrace();
        }
    }

}
