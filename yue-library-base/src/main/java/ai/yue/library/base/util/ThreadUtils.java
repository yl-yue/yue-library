package ai.yue.library.base.util;

import ai.yue.library.base.dto.CurrentLineInfo;
import cn.hutool.core.thread.ThreadUtil;

/**
 * 线程工具类
 * 
 * @author	ylyue
 * @since	2017年10月24日
 */
public class ThreadUtils extends ThreadUtil {

	private static int originStackIndex = 2;

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

}
