package ai.yue.library.base.util;

/**
 * 异常打印工具类
 * 
 * @author	ylyue
 * @since	2018年9月9日
 */
public class ExceptionUtils {

	public synchronized static void printException(Throwable e) {
    	System.err.println(e);
    	StackTraceElement[] stackTraceElementArray = e.getStackTrace();
		int length = stackTraceElementArray.length;
		length = (length > 4) ? 4 : length;
		for (int i = 0; i < length; i++) {
			StackTraceElement stackTraceElement = stackTraceElementArray[i];
			String fileName = stackTraceElement.getFileName();
			String className = stackTraceElement.getClassName();
			String methodName = stackTraceElement.getMethodName();
			int lineNumber = stackTraceElement.getLineNumber();
			System.err.println("	at " + className + "." + methodName + "(" + fileName + ":" + lineNumber + ")");
		}
	}
	
}
