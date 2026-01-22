package ai.yue.library.base.util;

import ai.yue.library.base.dto.CurrentLineInfo;
import cn.hutool.v7.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 线程工具类
 * 
 * @author	ylyue
 * @since	2017年10月24日
 */
@Slf4j
public class ThreadUtils extends ThreadUtil {

	private static int originStackIndex = 2;
    /* For autonumbering anonymous threads. */
    private static int threadInitNumber;
    private static synchronized int nextThreadNum() {
        return threadInitNumber++;
    }

    /**
     * 获取当前线程所执行行，线程信息
     */
    public static CurrentLineInfo getCurrentLineInfo() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[originStackIndex];
        CurrentLineInfo currentLineInfo = new CurrentLineInfo();
        currentLineInfo.setFileName(stackTraceElement.getFileName());
        currentLineInfo.setClassName(stackTraceElement.getClassName());
        currentLineInfo.setMethodName(stackTraceElement.getMethodName());
        currentLineInfo.setLineNumber(stackTraceElement.getLineNumber());
        return currentLineInfo;
    }

    /**
     * 循环执行线程
     * <p>- 若用于监听消费，此代码只能执行一次，否则会重复创建循环线程</p>
     * <p>- 若需要多次执行，需自行维护线程的创建与销毁，避免创建的线程数无穷大</p>
     *
     * @param task 执行函数
     * @return thread - 执行线程
     */
    public static Thread execLoopThread(Runnable task) {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    task.run();
                } catch (Exception e) {
                    log.error("循环线程执行异常", e);
                }
            }
        }, "LoopThread-" + nextThreadNum());
        thread.start();
        return thread;
    }

}
