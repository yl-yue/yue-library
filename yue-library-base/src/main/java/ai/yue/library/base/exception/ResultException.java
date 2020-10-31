package ai.yue.library.base.exception;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * {@linkplain Result} 结果异常定义
 * 
 * @author	ylyue
 * @since	2018年2月3日
 */
@Getter
public class ResultException extends RuntimeException {
	
	private static final long serialVersionUID = -4332073495864145387L;
	
	private Result<?> result;
	
	public <T> ResultException(String msg) {
		super(msg);
		this.result = R.errorPrompt(msg);
	}
	
	/**
	 * 异常消息格式化构造<br>
	 * 当传入msg无"{}"时，被认为非模板，直接打印多个参数以空格分隔
	 * 
	 * @param msg    文本模板，被替换的部分用 {} 表示
	 * @param values 文本模板中占位符被替换的值
	 */
	public <T> ResultException(String msg, Object... values) {
		this(StrUtil.format(msg, values));
	}
	
	public <T> ResultException(Result<T> result) {
		super(result.getMsg());
		this.result = result;
	}
	
}
