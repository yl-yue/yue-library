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
	
	/**
	 * <h2>异常消息构造</h2>
	 * {@linkplain R#errorPrompt(String)} 的便捷方式
	 */
	public <T> ResultException(String msg) {
		super(msg);
		this.result = R.errorPrompt(msg);
	}
	
	/**
	 * <h2>异常消息格式化构造</h2>
	 * <p>{@linkplain R#errorPrompt(String)} 的便捷方式
	 * <p>msg支持文本模板格式化，{} 表示占位符
	 * <pre class="code">例：("this is {} for {}", "a", "b") = this is a for b</pre>
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
