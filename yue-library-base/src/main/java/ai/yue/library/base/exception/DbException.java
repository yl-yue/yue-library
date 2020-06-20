package ai.yue.library.base.exception;

import lombok.NoArgsConstructor;

/**
 * <h3>Db异常</h3><br>
 * 
 * Created by sunJinChuan on 2016/6/8.
 */
@NoArgsConstructor
public class DbException extends RuntimeException {
	
	private static final long serialVersionUID = 5869945193750586067L;
	
	/**
	 * 统一异常处理后是否显示异常提示
	 */
	private boolean showMsg = false;
	
	public DbException(String msg) {
        super(msg);
    }
	
	/**
	 * DB异常
	 * 
	 * @param msg 异常提示
	 * @param showMsg 统一异常处理后是否显示异常提示
	 */
	public DbException(String msg, boolean showMsg) {
		super(msg);
		this.showMsg = showMsg;
	}
	
	/**
	 * 统一异常处理后是否显示异常提示
	 * 
	 * @return 是否显示
	 */
	public boolean isShowMsg() {
		return showMsg;
	}
	
	/**
	 * 设置统一异常处理后是否显示异常提示
	 * @param showMsg 是否显示
	 */
	public void setShowMsg(boolean showMsg) {
		this.showMsg = showMsg;
	}
	
}
