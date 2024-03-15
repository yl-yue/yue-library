package ai.yue.library.base.dto;

import lombok.Data;

/**
 * 当前线程所执行行，线程信息
 */
@Data
public class CurrentLineInfo {

    /**
     * 文件名
     */
    String fileName;

    /**
     * 类名称
     */
    String className;

    /**
     * 方法名称
     */
    String methodName;

    /**
     * 第几行
     */
    int lineNumber;

}
