package ai.yue.library.base.util;

/**
 * @author  孙金川
 * @version 创建时间：2018年9月9日
 */
public class ExceptionUtils {

	public synchronized static void printException(Exception e) {
    	System.err.println(e);
    	StackTraceElement[] stackTraceElementArray = e.getStackTrace();
		for (int i = 0; i < 4; i++) {
			StackTraceElement stackTraceElement = stackTraceElementArray[i];
			String fileName = stackTraceElement.getFileName();
			String className = stackTraceElement.getClassName();
			String methodName = stackTraceElement.getMethodName();
			int lineNumber = stackTraceElement.getLineNumber();
			System.err.println("	at " + className + "." + methodName + "(" + fileName + ":" + lineNumber + ")");
		}
	}
	
}
