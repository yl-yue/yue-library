package ai.yue.library.base.util;

/**
 * 线程信息获取工具类
 * @author  孙金川
 * @version 创建时间：2017年10月24日
 */
public class CurrentLineInfo {

	private static int originStackIndex = 2;

    public static String getFileName() {
        return Thread.currentThread().getStackTrace()[originStackIndex].getFileName();
    }

    /**
     * 得到当前线程所在的类名称
     * @return 类名称
     */
    public static String getClassName() {
        return Thread.currentThread().getStackTrace()[originStackIndex].getClassName();
    }

    /**
     * 得到当前线程所在的方法名称
     * @return 方法名称
     */
    public static String getMethodName() {
        return Thread.currentThread().getStackTrace()[originStackIndex].getMethodName();
    }

    /**
     * 得到当前线程在第几行
     * @return 第几行
     */
    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[originStackIndex].getLineNumber();
    }
    
}
