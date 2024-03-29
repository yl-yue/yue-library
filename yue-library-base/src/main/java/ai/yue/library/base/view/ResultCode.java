package ai.yue.library.base.view;

/**
 * 响应状态码定义（建议使用枚举实现此接口）
 */
public interface ResultCode {

    /**
     * 响应状态码（建议自定义 code &gt; 600）
     */
    Integer getCode();

    /**
     * 响应提示
     */
    String getMsg();

}
