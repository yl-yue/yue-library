package ai.yue.library.base.view;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.exception.*;
import ai.yue.library.base.util.ExceptionUtils;
import ai.yue.library.base.util.I18nUtils;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ClassLoaderUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 便捷返回 {@linkplain Result}，构建 {@code RESTful} 风格API结果
 *
 * @author ylyue
 * @since 2017年7月31日
 */
@Slf4j
public class R {

    // ------ Result error builder ------

    /**
     * 失败后调用
     *
     * @param code 状态码
     * @param msg  提示消息
     * @return
     */
    private static Result<?> error(Integer code, String msg) {
        return Result.builder().code(code).msg(msg).flag(false).build();
    }

    /**
     * 失败后调用
     *
     * @param <T>
     * @param code 状态码
     * @param msg  提示消息
     * @param data 异常数据
     * @return
     */
    private static <T> Result<T> error(Integer code, String msg, T data) {
        return new Result<T>().toBuilder().code(code).msg(msg).flag(false).data(data).build();
    }

    // ------ Result success builder ------

    /**
     * 成功后调用，返回的data为null
     *
     * @return HTTP请求，最外层响应对象
     */
    private static Result<?> success(Integer code, String msg) {
        return Result.builder()
                .code(code)
                .msg(msg)
                .flag(true)
                .build();
    }

    // 200 - 正确结果

    /**
     * 成功后调用，返回的data为null
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> success() {
        return success(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg());
    }

    /**
     * 成功后调用，返回的data为一个对象
     *
     * @param <T>  泛型
     * @param data 数据
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>().toBuilder()
                .code(ResultEnum.SUCCESS.getCode())
                .msg(ResultEnum.SUCCESS.getMsg())
                .flag(true)
                .data(data)
                .build();
    }

    /**
     * 会话未注销，无需登录-210
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> loggedIn() {
        return success(ResultEnum.LOGGED_IN.getCode(), ResultEnum.LOGGED_IN.getMsg());
    }

    // 300 - 资源、重定向、定位等提示

    /**
     * 资源已失效-300
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> resourceAlreadyInvalid() {
        return error(ResultEnum.RESOURCE_ALREADY_INVALID.getCode(), ResultEnum.RESOURCE_ALREADY_INVALID.getMsg());
    }

    /**
     * Moved Permanently-301
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> movedPermanently() {
        return error(ResultEnum.MOVED_PERMANENTLY.getCode(), ResultEnum.MOVED_PERMANENTLY.getMsg());
    }

    /**
     * 文件上传请求错误，获得文件信息为空，同时文件必须有明确的匹配类型（如文本类型：.txt）-310
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> fileEmpty() {
        return error(ResultEnum.FILE_EMPTY.getCode(), ResultEnum.FILE_EMPTY.getMsg());
    }

    // 400 - 客户端错误

    /**
     * 未登录或登录已失效-401
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> unauthorized() {
        return error(ResultEnum.UNAUTHORIZED.getCode(), ResultEnum.UNAUTHORIZED.getMsg());
    }

    /**
     * 非法访问-402
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> attack() {
        return error(ResultEnum.ATTACK.getCode(), ResultEnum.ATTACK.getMsg());
    }

    /**
     * 非法访问-402
     *
     * @param <T>  泛型
     * @param data 异常数据
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> attack(T data) {
        return error(ResultEnum.ATTACK.getCode(), ResultEnum.ATTACK.getMsg(), data);
    }

    /**
     * 无权限-403
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> forbidden() {
        return error(ResultEnum.FORBIDDEN.getCode(), ResultEnum.FORBIDDEN.getMsg());
    }

    /**
     * Not Found-404
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> notFound() {
        return error(ResultEnum.NOT_FOUND.getCode(), ResultEnum.NOT_FOUND.getMsg());
    }

    /**
     * 方法不允许（Method Not Allowed）-405
     * <p>客户端使用服务端不支持的 Http Request Method 进行接口调用
     *
     * @param data {@link Result#setData(Object)} 更详细的异常提示信息
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> methodNotAllowed(T data) {
        return error(ResultEnum.METHOD_NOT_ALLOWED.getCode(), ResultEnum.METHOD_NOT_ALLOWED.getMsg(), data);
    }

    /**
     * API接口版本弃用-410
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> gone() {
        return error(ResultEnum.GONE.getCode(), ResultEnum.GONE.getMsg());
    }

    /**
     * 频繁请求限流-429
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> tooManyRequests() {
        return error(ResultEnum.TOO_MANY_REQUESTS.getCode(), ResultEnum.TOO_MANY_REQUESTS.getMsg());
    }

    /**
     * 参数为空-432
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> paramVoid() {
        return error(ResultEnum.PARAM_VOID.getCode(), ResultEnum.PARAM_VOID.getMsg());
    }

    /**
     * 参数校验未通过，请参照API核对后重试-433
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> paramCheckNotPass() {
        return error(ResultEnum.PARAM_CHECK_NOT_PASS.getCode(), ResultEnum.PARAM_CHECK_NOT_PASS.getMsg());
    }

    /**
     * 参数校验未通过，请参照API核对后重试-433
     *
     * @param data {@link Result#setData(Object)} 提示信息
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> paramCheckNotPass(T data) {
        return error(ResultEnum.PARAM_CHECK_NOT_PASS.getCode(), ResultEnum.PARAM_CHECK_NOT_PASS.getMsg(), data);
    }

    /**
     * 参数校验未通过，无效的value-434
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> paramValueInvalid() {
        return error(ResultEnum.PARAM_VALUE_INVALID.getCode(), ResultEnum.PARAM_VALUE_INVALID.getMsg());
    }

    /**
     * 参数校验未通过，无效的value-434
     *
     * @param data {@link Result#setData(Object)} 提示信息
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> paramValueInvalid(T data) {
        return error(ResultEnum.PARAM_VALUE_INVALID.getCode(), ResultEnum.PARAM_VALUE_INVALID.getMsg(), data);
    }

    /**
     * 参数解密错误-435
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> paramDecryptError() {
        return error(ResultEnum.PARAM_DECRYPT_ERROR.getCode(), ResultEnum.PARAM_DECRYPT_ERROR.getMsg());
    }

    // 500 - 服务器错误

    /**
     * 服务器内部错误-500
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> internalServerError() {
        return error(ResultEnum.INTERNAL_SERVER_ERROR.getCode(), ResultEnum.INTERNAL_SERVER_ERROR.getMsg());
    }

    /**
     * 服务器内部错误-500
     *
     * @param <T>  泛型
     * @param data 异常数据
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> internalServerError(T data) {
        return error(ResultEnum.INTERNAL_SERVER_ERROR.getCode(), ResultEnum.INTERNAL_SERVER_ERROR.getMsg(), data);
    }

    /**
     * 请求错误-501
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> requestError() {
        return error(ResultEnum.REQUEST_ERROR.getCode(), ResultEnum.REQUEST_ERROR.getMsg());
    }

    /**
     * 请求错误-501
     *
     * @param <T>  泛型
     * @param data 异常数据
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> requestError(T data) {
        return error(ResultEnum.REQUEST_ERROR.getCode(), ResultEnum.REQUEST_ERROR.getMsg(), data);
    }

    /**
     * 服务不可用-503
     * <p>服务目前无法使用（由于超载或停机维护）</p>
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> serviceUnavailable() {
        return error(ResultEnum.SERVICE_UNAVAILABLE.getCode(), ResultEnum.SERVICE_UNAVAILABLE.getMsg());
    }

    /**
     * 服务不可用（停机维护）-503
     *
     * @param restoreTime 预计恢复时间（如：2020-12-31 08:00:00）
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> serviceUnavailable(LocalDateTime restoreTime) {
        return error(ResultEnum.SERVICE_UNAVAILABLE.getCode(), ResultEnum.SERVICE_UNAVAILABLE.getMsg(), ResultPrompt.serviceUnavailable(restoreTime));
    }

    /**
     * 服务不可用-503
     * <p>服务目前无法使用（由于超载或停机维护）</p>
     *
     * @param <T>  泛型
     * @param data 服务不可用的具体原因，参考：{@link ResultPrompt#serviceUnavailable(int)}
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> serviceUnavailable(T data) {
        return error(ResultEnum.SERVICE_UNAVAILABLE.getCode(), ResultEnum.SERVICE_UNAVAILABLE.getMsg(), data);
    }

    /**
     * 数据结构异常-505
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> dataStructure() {
        return error(ResultEnum.DATA_STRUCTURE.getCode(), ResultEnum.DATA_STRUCTURE.getMsg());
    }

    /**
     * 数据结构异常-505
     * <p><i>不正确的结果大小</i>
     *
     * @param expected 预期值
     * @param actual   实际值
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> dataStructure(int expected, int actual) {
        return error(ResultEnum.DATA_STRUCTURE.getCode(), ResultEnum.DATA_STRUCTURE.getMsg(), ResultPrompt.dataStructure(expected, actual));
    }

    /**
     * 数据结构异常，请检查相应数据结构一致性-506
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> dbError() {
        return error(ResultEnum.DB_ERROR.getCode(), ResultEnum.DB_ERROR.getMsg());
    }

    /**
     * 数据结构异常，请检查相应数据结构一致性-506
     *
     * @param data {@link Result#setData(Object)} 提示信息
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> dbError(T data) {
        return error(ResultEnum.DB_ERROR.getCode(), ResultEnum.DB_ERROR.getMsg(), data);
    }

    /**
     * 哎哟喂！网络开小差了，请稍后重试...-507
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> clientFallback() {
        return error(ResultEnum.CLIENT_FALLBACK.getCode(), ResultEnum.CLIENT_FALLBACK.getMsg());
    }

    /**
     * 哎哟喂！网络开小差了，请稍后重试...-507
     *
     * @param data {@link Result#setData(Object)} 更详细的异常提示信息
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> clientFallback(T data) {
        return error(ResultEnum.CLIENT_FALLBACK.getCode(), ResultEnum.CLIENT_FALLBACK.getMsg(), data);
    }

    /**
     * 哎哟喂！服务都被您挤爆了...-508
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> clientFallbackError() {
        return error(ResultEnum.CLIENT_FALLBACK_ERROR.getCode(), ResultEnum.CLIENT_FALLBACK_ERROR.getMsg());
    }

    /**
     * 哎哟喂！服务都被您挤爆了...-508
     *
     * @param data {@link Result#setData(Object)} 更详细的异常提示信息
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> clientFallbackError(T data) {
        return error(ResultEnum.CLIENT_FALLBACK_ERROR.getCode(), ResultEnum.CLIENT_FALLBACK_ERROR.getMsg(), data);
    }

    /**
     * 类型转换错误-509
     *
     * @param data {@link Result#setData(Object)} 提示信息
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> typeConvertError(T data) {
        return error(ResultEnum.TYPE_CONVERT_ERROR.getCode(), ResultEnum.TYPE_CONVERT_ERROR.getMsg(), data);
    }

    // 600 - 自定义错误提示

    /**
     * <b>错误提示-600</b>
     * <p>适用于用户操作提示、业务消息提示、友好的错误提示等场景。
     * <p>可优先使用 {@linkplain ResultPrompt} 预定义的提示信息，如：{@linkplain ResultPrompt#USERNAME_OR_PASSWORD_ERROR}
     *
     * @param msg 提示消息（如：{@value ResultPrompt#USERNAME_OR_PASSWORD_ERROR}）
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> errorPrompt(String msg) {
        return error(ResultEnum.ERROR_PROMPT.getCode(), msg);
    }

    /**
     * <b>错误提示-600</b>
     * <p>适用于用户操作提示、业务消息提示、友好的错误提示等场景。
     * <p>可优先使用 {@linkplain ResultPrompt} 预定义的提示信息，如：{@linkplain ResultPrompt#USERNAME_OR_PASSWORD_ERROR}
     *
     * @param msg  提示消息（如：{@value ResultPrompt#USERNAME_OR_PASSWORD_ERROR}）
     * @param data 业务处理数据
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> errorPrompt(String msg, T data) {
        return error(ResultEnum.ERROR_PROMPT.getCode(), msg, data);
    }

    /**
     * <b>错误提示-600</b>
     * <p>适用于用户操作提示、业务消息提示、友好的错误提示等场景。
     * <p>可优先使用 {@linkplain ResultPrompt} 预定义的提示信息，如：{@linkplain ResultPrompt#USERNAME_OR_PASSWORD_ERROR}
     * <p>msg支持文本模板格式化，{} 表示占位符
     * <pre class="code">例：("this is {} for {}", "a", "b") = this is a for b</pre>
     *
     * @param msg    文本模板，被替换的部分用 {} 表示
     * @param values 文本模板中占位符被替换的值
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> errorPromptFormat(String msg, Object... values) {
        return errorPrompt(StrUtil.format(msg, values));
    }

    /**
     * <b>错误提示-600</b>
     * <p>适用于i18n资源包定义（messages.properties），遵循SpringBoot默认值规范</p>
     * <p>msg支持文本模板格式化，{} 表示占位符
     * <pre class="code">例：("this is {} for {}", "a", "b") = this is a for b</pre>
     *
     * @param msgKey messages.properties中定义的key，被替换的部分用 {} 表示
     * @param values messages.properties中占位符被替换的值
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> errorPromptI18n(String msgKey, Object... values) {
        return errorPrompt(I18nUtils.get(msgKey, values));
    }

    /**
     * <b>错误提示-600</b>
     * <p>适用于i18n资源包定义（messages.properties），遵循SpringBoot默认值规范</p>
     *
     * @param msgKey messages.properties中定义的key
     * @param data   业务处理数据
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> errorPromptI18nT(String msgKey, T data) {
        return errorPrompt(I18nUtils.get(msgKey), data);
    }

    // ------ Result exception builder ------

    public static Result<?> getResult(Throwable e) {
        if (e == null) {
            return notFound();
        } else if (e instanceof ResultException) {
            // 异常结果处理
            Result<?> result = ((ResultException) e).getResult();
            log.error(result.toString());
            ExceptionUtils.printException(e);
            return result;
        } else if (e instanceof AuthorizeException) {
            // WEB 异常拦截
            // 登录异常（Admin）-301
            ExceptionUtils.printException(e);
            return movedPermanently();
        } else if (e instanceof LoginException) {
            // 登录异常（User）-401
            ExceptionUtils.printException(e);
            return unauthorized();
        } else if (e instanceof AttackException) {
            // 非法请求异常拦截-402
            ExceptionUtils.printException(e);
            return attack(e.getMessage());
        } else if (e instanceof ForbiddenException) {
            // 无权限异常访问处理-403
            ExceptionUtils.printException(e);
            return forbidden();
        } else if (e instanceof ApiVersionDeprecatedException) {
            // API接口版本弃用异常-410
            ExceptionUtils.printException(e);
            return gone();
        } else if (e instanceof ParamVoidException) {
            // 参数效验为空统一处理-432
            return paramVoid();
        } else if (e instanceof ParamException) {
            // 参数效验未通过统一处理-433
            ExceptionUtils.printException(e);
            return paramCheckNotPass(e.getMessage());
        } else if (e instanceof BindException) {
            // {@linkplain Valid} 验证异常统一处理-433
            List<ObjectError> errors = ((BindException) e).getAllErrors();
            JSONObject paramHint = new JSONObject();
            errors.forEach(error -> {
                String str = StrUtil.subAfter(error.getArguments()[0].toString(), "[", true);
                String key = str.substring(0, str.length() - 1);
                String msg = error.getDefaultMessage();
                paramHint.put(key, msg);
                Console.error(key + " " + msg);
            });

            return paramCheckNotPass(paramHint.toString());
        } else if (e instanceof ValidateException) {
            // 验证异常统一处理-433
            ExceptionUtils.printException(e);
            return paramCheckNotPass(e.getMessage());
        } else if (e instanceof ParamDecryptException) {
            // 解密异常统一处理-435
            log.error("【解密错误】错误信息如下：{}", e.getMessage());
            ExceptionUtils.printException(e);
            return paramDecryptError();
        } else if (e instanceof DbException) {
            // DB异常统一处理-506
            e.printStackTrace();
            if (((DbException) e).isShowMsg()) {
                return dbError(e.getMessage());
            }

            return dbError();
        } else if (e instanceof ClientFallbackException) {
            // 服务降级-507
            ExceptionUtils.printException(e);
            return clientFallback();
        } else if (e instanceof ConvertException) {
            // 类型转换异常统一处理-509
            log.error("【类型转换异常】转换类型失败，错误信息如下：{}", e.getMessage());
            ExceptionUtils.printException(e);
            return typeConvertError(e.getMessage());
        } else if (e instanceof ResponseStatusException) {
            ExceptionUtils.printException(e);
            HttpStatus httpStatus = ((ResponseStatusException) e).getStatus();
            int code = httpStatus.value();
            ResultEnum resultEnum = ResultEnum.valueOf(code);
            if (resultEnum != null) {
                return error(resultEnum.getCode(), resultEnum.getMsg(), e.toString());
            }
        } else if (ClassLoaderUtil.isPresent("feign.FeignException")
                && ClassLoaderUtil.loadClass("feign.FeignException").isAssignableFrom(e.getClass())) {
            log.error("【服务降级】接口调用失败，FeignException 错误内容如下：", e);
            String contentUTF8 = ReflectUtil.invoke(e, "contentUTF8");
            try {
                Result<?> result = Convert.toJavaBean(contentUTF8, Result.class);
                if (result == null) {
                    return clientFallback();
                }
                return result;
            } catch (Exception ex) {
                return clientFallback(contentUTF8);
            }
        } else if (ClassLoaderUtil.isPresent("org.mybatis.spring.MyBatisSystemException")
                && ClassLoaderUtil.loadClass("org.mybatis.spring.MyBatisSystemException").isAssignableFrom(e.getClass())) {
            try {
                if (e.getCause().getCause() instanceof ResultException) {
                    return ((ResultException) e.getCause().getCause()).getResult();
                }
            } catch (Exception ignore) {
            }
        } else if (ClassLoaderUtil.isPresent("cn.dev33.satoken.exception.NotLoginException")
                && ClassLoaderUtil.loadClass("cn.dev33.satoken.exception.NotLoginException").isAssignableFrom(e.getClass())) {
            return R.unauthorized();
        }

        // 处理所有未处理异常-500
        log.error(e.getMessage(), e);
        return internalServerError(ExceptionUtils.getPrintExceptionToJson(e));
    }

}
