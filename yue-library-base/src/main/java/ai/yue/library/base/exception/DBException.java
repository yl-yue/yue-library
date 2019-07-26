package ai.yue.library.base.exception;

import lombok.NoArgsConstructor;

/**
 * Created by sunJinChuan on 2016/6/8.
 */
@NoArgsConstructor
public class DBException extends RuntimeException {
	
	private static final long serialVersionUID = 5869945193750586067L;

	public DBException(String msg) {
        super(msg);
    }
	
}
