package ai.yue.library.base.exception;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * {@linkplain Result} 结果异常定义
 * 
 * @author	ylyue
 * @since	2018年2月3日
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResultException extends RuntimeException {
	
	private static final long serialVersionUID = -4332073495864145387L;
	
	/**
	 * 业务定位标识
	 * <p>用于上层捕获异常（如：多重try_catch）时，方便业务定位
	 */
	private int businessId;
	private Result<?> result;
	
	/**
	 * <h2>异常消息构造</h2>
	 * {@linkplain R#errorPrompt(String)} 的便捷方式
	 */
	public ResultException(String msg) {
		super(msg);
		this.result = R.errorPrompt(msg);
	}
	
	/**
	 * <h2>异常消息格式化构造</h2>
	 * <p>{@linkplain R#errorPromptFormat(String, Object...)} 的便捷方式
	 * <p>msg支持文本模板格式化，{} 表示占位符
	 * <pre class="code">例：("this is {} for {}", "a", "b") = this is a for b</pre>
	 * 
	 * @param msg    文本模板，被替换的部分用 {} 表示
	 * @param values 文本模板中占位符被替换的值
	 */
	public ResultException(String msg, Object... values) {
		this(StrUtil.format(msg, values));
	}
	
	public ResultException(Result<?> result) {
		super(result.getMsg());
		this.result = result;
	}
	
	/**
	 * <h2>异常消息构造</h2>
	 * {@linkplain R#errorPrompt(String)} 的便捷方式
	 */
	public ResultException(int businessId, String msg) {
		super(msg);
		this.businessId = businessId;
		this.result = R.errorPrompt(msg);
	}
	
	public ResultException(int businessId, Result<?> result) {
		super(result.getMsg());
		this.businessId = businessId;
		this.result = result;
	}

}
