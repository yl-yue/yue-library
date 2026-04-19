package ai.yue.library.base.view;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.exception.*;
import ai.yue.library.base.util.ExceptionUtils;
import ai.yue.library.base.util.I18nUtils;
import ai.yue.library.base.util.SpringUtils;
import cn.hutool.v7.core.array.ArrayUtil;
import cn.hutool.v7.core.classloader.ClassLoaderUtil;
import cn.hutool.v7.core.convert.ConvertException;
import cn.hutool.v7.core.exception.ValidateException;
import cn.hutool.v7.core.lang.Console;
import cn.hutool.v7.core.reflect.FieldUtil;
import cn.hutool.v7.core.reflect.method.MethodUtil;
import cn.hutool.v7.core.text.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;
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

    private static final String ServletUtils = "ai.yue.library.web.util.ServletUtils";

    // ======web======
    private static final String NoResourceFoundException = "org.springframework.web.servlet.resource.NoResourceFoundException";

    // ======feign======
    private static final String FeignException = "feign.FeignException";

    // ======dao======
    private static final String PageException = "com.github.pagehelper.PageException";
    private static final String DataAccessException = "org.springframework.dao.DataAccessException";
    private static final String TooManyResultsException = "org.apache.ibatis.exceptions.TooManyResultsException";
    private static final String DataIntegrityViolationException = "org.springframework.dao.DataIntegrityViolationException";
    private static final String MyBatisSystemException = "org.mybatis.spring.MyBatisSystemException";

    // ======security======
    private static final String NotLoginException = "cn.dev33.satoken.exception.NotLoginException";
    private static final String AuthenticationException = "org.springframework.security.core.AuthenticationException";
    private static final String AccessDeniedException = "org.springframework.security.access.AccessDeniedException";

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

    private static Result<?> error(Integer code, ResultEnum resultEnum) {
        return Result.builder().code(code).msg(I18nUtils.getYue(resultEnum)).flag(false).build();
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

    private static <T> Result<T> error(Integer code, ResultEnum resultEnum, T data) {
        return new Result<T>().toBuilder().code(code).msg(I18nUtils.getYue(resultEnum)).flag(false).data(data).build();
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

    private static Result<?> success(Integer code, ResultEnum resultEnum) {
        return Result.builder()
                .code(code)
                .msg(I18nUtils.getYue(resultEnum))
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
        return success(ResultEnum.SUCCESS.getCode(), I18nUtils.getYue(ResultEnum.SUCCESS));
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
                .msg(I18nUtils.getYue(ResultEnum.SUCCESS))
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
        return success(ResultEnum.LOGGED_IN.getCode(), ResultEnum.LOGGED_IN);
    }

    // 300 - 资源、重定向、定位等提示

    /**
     * 资源已失效-300
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> resourceAlreadyInvalid() {
        return error(ResultEnum.RESOURCE_ALREADY_INVALID.getCode(), ResultEnum.RESOURCE_ALREADY_INVALID);
    }

    /**
     * Moved Permanently-301
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> movedPermanently() {
        return error(ResultEnum.MOVED_PERMANENTLY.getCode(), ResultEnum.MOVED_PERMANENTLY);
    }

    /**
     * 文件上传请求错误，获得文件信息为空，同时文件必须有明确的匹配类型（如文本类型：.txt）-310
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> fileEmpty() {
        return error(ResultEnum.FILE_EMPTY.getCode(), ResultEnum.FILE_EMPTY);
    }

    // 400 - 客户端错误

    /**
     * 未登录或登录已失效-401
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> unauthorized() {
        return error(ResultEnum.UNAUTHORIZED.getCode(), ResultEnum.UNAUTHORIZED);
    }
    
    /**
     * 错误的登录-401
     */
    public static <T> Result<T> unauthorizedError(T data) {
        return error(ResultEnum.UNAUTHORIZED_ERROR.getCode(), ResultEnum.UNAUTHORIZED_ERROR, data);
    }
    
    /**
     * 你已被顶下线-401
     */
    public static Result<?> unauthorizedBeReplaced() {
        return error(ResultEnum.UNAUTHORIZED_BE_REPLACED.getCode(), ResultEnum.UNAUTHORIZED_BE_REPLACED);
    }

    /**
     * 你已被踢下线-401
     */
    public static Result<?> unauthorizedKickOut() {
        return error(ResultEnum.UNAUTHORIZED_KICK_OUT.getCode(), ResultEnum.UNAUTHORIZED_KICK_OUT);
    }

    /**
     * 本次会话已被冻结，请重新登录-401
     */
    public static Result<?> unauthorizedTokenFreeze() {
        return error(ResultEnum.UNAUTHORIZED_TOKEN_FREEZE.getCode(), ResultEnum.UNAUTHORIZED_TOKEN_FREEZE);
    }

    /**
     * 非法访问-402
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> attack() {
        return error(ResultEnum.ATTACK.getCode(), ResultEnum.ATTACK);
    }

    /**
     * 非法访问-402
     *
     * @param <T>  泛型
     * @param data 异常数据
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> attack(T data) {
        return error(ResultEnum.ATTACK.getCode(), ResultEnum.ATTACK, data);
    }

    /**
     * 无权限-403
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> forbidden() {
        return error(ResultEnum.FORBIDDEN.getCode(), ResultEnum.FORBIDDEN);
    }
    
    /**
     * 无权限-403
     */
    public static <T> Result<T> forbidden(T data) {
        return error(ResultEnum.FORBIDDEN.getCode(), ResultEnum.FORBIDDEN, data);
    }
    
    /**
     * Not Found-404
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> notFound() {
        return error(ResultEnum.NOT_FOUND.getCode(), ResultEnum.NOT_FOUND);
    }

    /**
     * 方法不允许（Method Not Allowed）-405
     * <p>客户端使用服务端不支持的 Http Request Method 进行接口调用
     *
     * @param data {@link Result#setData(Object)} 更详细的异常提示信息
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> methodNotAllowed(T data) {
        return error(ResultEnum.METHOD_NOT_ALLOWED.getCode(), ResultEnum.METHOD_NOT_ALLOWED, data);
    }

    /**
     * API接口版本弃用-410
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> gone() {
        return error(ResultEnum.GONE.getCode(), ResultEnum.GONE);
    }

    /**
     * 频繁请求限流-429
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> tooManyRequests() {
        return error(ResultEnum.TOO_MANY_REQUESTS.getCode(), ResultEnum.TOO_MANY_REQUESTS);
    }

    /**
     * qps限流-429
     */
    public static Result<?> qpsLimit(String msg) {
        return error(ResultEnum.TOO_MANY_REQUESTS.getCode(), ResultEnum.TOO_MANY_REQUESTS, msg);
    }

    /**
     * 参数为空-432
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> paramVoid() {
        return error(ResultEnum.PARAM_VOID.getCode(), ResultEnum.PARAM_VOID);
    }

    /**
     * 参数校验未通过，请参照API核对后重试-433
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> paramCheckNotPass() {
        return error(ResultEnum.PARAM_CHECK_NOT_PASS.getCode(), ResultEnum.PARAM_CHECK_NOT_PASS);
    }

    /**
     * 参数校验未通过，请参照API核对后重试-433
     *
     * @param data {@link Result#setData(Object)} 提示信息
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> paramCheckNotPass(T data) {
        return error(ResultEnum.PARAM_CHECK_NOT_PASS.getCode(), ResultEnum.PARAM_CHECK_NOT_PASS, data);
    }

    /**
     * 参数校验未通过，无效的value-434
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> paramValueInvalid() {
        return error(ResultEnum.PARAM_VALUE_INVALID.getCode(), ResultEnum.PARAM_VALUE_INVALID);
    }

    /**
     * 参数校验未通过，无效的value-434
     *
     * @param data {@link Result#setData(Object)} 提示信息
     */
    public static <T> Result<T> paramValueInvalid(T data) {
        return error(ResultEnum.PARAM_VALUE_INVALID.getCode(), ResultEnum.PARAM_VALUE_INVALID, data);
    }

    /**
     * 参数解密错误-435
     */
    public static Result<?> paramDecryptError() {
        return error(ResultEnum.PARAM_DECRYPT_ERROR.getCode(), ResultEnum.PARAM_DECRYPT_ERROR);
    }

    /**
     * 请勿重复操作-436（幂等错误）
     */
    public static <T> Result<T> idempotent(T data) {
        return error(ResultEnum.IDEMPOTENT.getCode(), ResultEnum.IDEMPOTENT, data);
    }

    /**
     * 锁获取失败，锁已被其他线程持有，请重试-437
     */
    public static <T> Result<T> lockAcquireFailure(T data) {
        return error(ResultEnum.LOCK_ACQUIRE_FAILURE.getCode(), ResultEnum.LOCK_ACQUIRE_FAILURE, data);
    }

    // 500 - 服务器错误

    /**
     * 服务器内部错误-500
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> internalServerError() {
        return error(ResultEnum.INTERNAL_SERVER_ERROR.getCode(), ResultEnum.INTERNAL_SERVER_ERROR);
    }

    /**
     * 服务器内部错误-500
     *
     * @param <T>  泛型
     * @param data 异常数据
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> internalServerError(T data) {
        return error(ResultEnum.INTERNAL_SERVER_ERROR.getCode(), ResultEnum.INTERNAL_SERVER_ERROR, data);
    }

    /**
     * 请求错误-501
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> requestError() {
        return error(ResultEnum.REQUEST_ERROR.getCode(), ResultEnum.REQUEST_ERROR);
    }

    /**
     * 请求错误-501
     *
     * @param <T>  泛型
     * @param data 异常数据
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> requestError(T data) {
        return error(ResultEnum.REQUEST_ERROR.getCode(), ResultEnum.REQUEST_ERROR, data);
    }

    /**
     * 服务不可用-503
     * <p>服务目前无法使用（由于超载或停机维护）</p>
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> serviceUnavailable() {
        return error(ResultEnum.SERVICE_UNAVAILABLE.getCode(), ResultEnum.SERVICE_UNAVAILABLE);
    }

    /**
     * 服务不可用（停机维护）-503
     *
     * @param restoreTime 预计恢复时间（如：2020-12-31 08:00:00）
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> serviceUnavailable(LocalDateTime restoreTime) {
        return error(ResultEnum.SERVICE_UNAVAILABLE.getCode(), ResultEnum.SERVICE_UNAVAILABLE, ResultPrompt.serviceUnavailable(restoreTime));
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
        return error(ResultEnum.SERVICE_UNAVAILABLE.getCode(), ResultEnum.SERVICE_UNAVAILABLE, data);
    }

    /**
     * Sql错误，数据完整性违规-505
     *
     * @param data {@link Result#setData(Object)} 提示信息
     */
    public static <T> Result<T> sqlDataIntegrityViolation(@Nullable T data) {
        return error(ResultEnum.SQL_DATA_INTEGRITY_VIOLATION.getCode(), ResultEnum.SQL_DATA_INTEGRITY_VIOLATION, data);
    }

    /**
     * Sql错误，请检查数据-506
     *
     * @param data {@link Result#setData(Object)} 提示信息
     */
    public static <T> Result<T> sqlError(T data) {
        return error(ResultEnum.SQL_ERROR.getCode(), ResultEnum.SQL_ERROR, data);
    }

    /**
     * 哎哟喂！网络开小差了，请稍后重试...-507
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> clientFallback() {
        return error(ResultEnum.CLIENT_FALLBACK.getCode(), ResultEnum.CLIENT_FALLBACK);
    }

    /**
     * 哎哟喂！网络开小差了，请稍后重试...-507
     *
     * @param data {@link Result#setData(Object)} 更详细的异常提示信息
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> clientFallback(T data) {
        return error(ResultEnum.CLIENT_FALLBACK.getCode(), ResultEnum.CLIENT_FALLBACK, data);
    }

    /**
     * 哎哟喂！服务都被您挤爆了...-508
     *
     * @return HTTP请求，最外层响应对象
     */
    public static Result<?> clientFallbackError() {
        return error(ResultEnum.CLIENT_FALLBACK_ERROR.getCode(), ResultEnum.CLIENT_FALLBACK_ERROR);
    }

    /**
     * 哎哟喂！服务都被您挤爆了...-508
     *
     * @param data {@link Result#setData(Object)} 更详细的异常提示信息
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> clientFallbackError(T data) {
        return error(ResultEnum.CLIENT_FALLBACK_ERROR.getCode(), ResultEnum.CLIENT_FALLBACK_ERROR, data);
    }

    /**
     * 类型转换错误-509
     *
     * @param data {@link Result#setData(Object)} 提示信息
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> typeConvertError(T data) {
        return error(ResultEnum.TYPE_CONVERT_ERROR.getCode(), ResultEnum.TYPE_CONVERT_ERROR, data);
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
    public static Result<?> errorPromptI18n(String msgKey, @Nullable Object... values) {
        return errorPrompt(I18nUtils.get(msgKey, values));
    }

    /**
     * <b>错误提示-600</b>
     * <p>适用于i18n资源包定义（messages.properties），遵循SpringBoot默认值规范</p>
     *
     * @param msgKey messages.properties中定义的key，被替换的部分用 {} 表示
     * @param values messages.properties中占位符被替换的值
     * @param data   业务处理数据
     * @return HTTP请求，最外层响应对象
     */
    public static <T> Result<T> errorPromptI18nT(String msgKey, @Nullable T data, @Nullable Object... values) {
        return errorPrompt(I18nUtils.get(msgKey, values), data);
    }

    /**
     * <b>错误提示-自定义code（&gt;600）</b>
     * <p>适用于用户操作提示、业务消息提示、友好的错误提示等场景。
     *
     * @param resultCode 请使用枚举实现自定义的{@link ResultCode}
     */
    public static Result<?> errorPromptCode(ResultCode resultCode) {
        return error(resultCode.getCode(), resultCode.getMsg());
    }

    /**
     * <b>错误提示-自定义code（&gt;600）</b>
     * <p>适用于i18n资源包定义（messages.properties），遵循SpringBoot默认值规范</p>
     *
     * @param resultCode 请使用枚举实现自定义的{@link ResultCode}
     * @param values     messages.properties中 {} 占位符被替换的值
     */
    public static Result<?> errorPromptCodeI18n(ResultCode resultCode, @Nullable Object... values) {
        return error(resultCode.getCode(), I18nUtils.get(resultCode.getMsg(), values));
    }

    // ------ Result exception builder ------

    public static Result<?> getResult(Throwable e) {
        if (e == null) {
            return notFound();
        } else if (e instanceof ResultException) {
            // 异常结果处理
            Result<?> result = ((ResultException) e).getResult();
            if (result.getCode() < 600) {
                log.error("{}", ExceptionUtils.getPrintExceptionToStr(e));
            } else {
                log.warn(result.toString());
            }

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
        } else if (isInstanceofExceptionClass(e, NoResourceFoundException)) {
            // Not Found-404
            log.error("【404异常】信息如下：", e);
            return notFound();
        } else if (e instanceof ApiDeprecatedException) {
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
            HttpStatusCode httpStatus = ((ResponseStatusException) e).getStatusCode();
            int code = httpStatus.value();
            ResultEnum resultEnum = ResultEnum.valueOf(code);
            if (resultEnum != null) {
                return error(resultEnum.getCode(), resultEnum, e.toString());
            }
        } else if (isInstanceofExceptionClass(e, FeignException)) {
            log.error("【服务降级】接口调用失败，FeignException 错误内容如下：", e);
            String contentUTF8 = MethodUtil.invoke(e, "contentUTF8");
            try {
                Result<?> result = Convert.toJavaBean(contentUTF8, Result.class);
                if (result == null) {
                    return clientFallback();
                }
                return result;
            } catch (Exception ex) {
                return clientFallback(contentUTF8);
            }
        } else if (isInstanceofExceptionClass(e, NotLoginException)) {
            String currentType = MethodUtil.invoke(e, "getType");
            Class<?> notLoginExceptionClass = ClassLoaderUtil.loadClass(NotLoginException);
            String[] types = new String[]{
                    // 未能读取到有效 token
                    (String) FieldUtil.getStaticFieldValue(FieldUtil.getField(notLoginExceptionClass, "NOT_TOKEN")),
                    // token 无效
                    (String) FieldUtil.getStaticFieldValue(FieldUtil.getField(notLoginExceptionClass, "INVALID_TOKEN")),
                    // 未按照指定前缀提交 token
                    (String) FieldUtil.getStaticFieldValue(FieldUtil.getField(notLoginExceptionClass, "NO_PREFIX"))
            };

            // 日志等级
            if (ArrayUtil.contains(types, currentType)) {
                log.error("NotLoginException: type = " + currentType, e);
            } else {
                /**
                 * 1. token 已被顶下线
                 * 2. token 已被踢下线
                 * 3. token 已被冻结
                 * 4. token 已过期
                 * 5. 当前会话未登录
                 */
                log.warn(e.getMessage());
            }

            // 异常提示
            if (currentType.equals(FieldUtil.getStaticFieldValue(FieldUtil.getField(notLoginExceptionClass, "BE_REPLACED")))) {
                // token 已被顶下线
                return unauthorizedBeReplaced();
            } else if (currentType.equals(FieldUtil.getStaticFieldValue(FieldUtil.getField(notLoginExceptionClass, "KICK_OUT")))) {
                // token 已被踢下线
                return unauthorizedKickOut();
            } else if (currentType.equals(FieldUtil.getStaticFieldValue(FieldUtil.getField(notLoginExceptionClass, "TOKEN_FREEZE")))) {
                // token 已被冻结
                return unauthorizedTokenFreeze();
            }

            return unauthorizedError(e.getMessage());
        } else if (isInstanceofExceptionClass(e, AuthenticationException)) {
            String authToken = "";
            if (ClassLoaderUtil.isPresent(ServletUtils)) {
                Class<?> ServletUtilsClass = ClassLoaderUtil.loadClass(ServletUtils);
                Method getAuthToken = MethodUtil.getPublicMethod(ServletUtilsClass,true, "getAuthToken");
                authToken = "authToken=" + MethodUtil.invokeStatic(getAuthToken);
            }

            String msg = I18nUtils.getYueDefault(ResultEnum.UNAUTHORIZED.getMsg());
            log.warn("【认证异常】{}, {}", msg, authToken);
            return unauthorized();
        } else if (isInstanceofExceptionClass(e, AccessDeniedException)) {
            String authToken = "";
            if (ClassLoaderUtil.isPresent(ServletUtils)) {
                Class<?> ServletUtilsClass = ClassLoaderUtil.loadClass(ServletUtils);
                Method getAuthToken = MethodUtil.getPublicMethod(ServletUtilsClass,true, "getAuthToken");
                authToken = "authToken=" + MethodUtil.invokeStatic(getAuthToken);
            }

            String msg = I18nUtils.getYueDefault(ResultEnum.FORBIDDEN.getMsg());
            log.error("【权限异常】{}, {}, {}", msg, e.getMessage(), authToken);
            return forbidden();
        } else if (isInstanceofExceptionClass(e, PageException)) {
            return paramCheckNotPass(e.getMessage());
        }
        // Sql错误，请检查数据-506
        else if (isInstanceofExceptionClass(e, TooManyResultsException)) {
            return sqlError(StrUtil.format("TooManyResultsException: {}", e.getMessage()));
        }
        // 数据库访问异常统一处理
        else if (isInstanceofExceptionClass(e, DataAccessException)) {
            // 处理MyBatis拦截器中抛出的嵌套ResultException
            if (isInstanceofExceptionClass(e, MyBatisSystemException)) {
                try {
                    if (e.getCause().getCause() instanceof ResultException) {
                        return ((ResultException) e.getCause().getCause()).getResult();
                    }
                } catch (Exception ignore) {
                }
            }

            // ===处理数据库操作异常===
            log.error("【数据库访问异常】信息如下：", e);

            // 是否开启debug日志等级 或 是否开发环境
            String message = null;
            String[] activeProfiles = SpringUtils.getActiveProfiles();
            if (log.isDebugEnabled() || (activeProfiles != null && ArrayUtil.containsIgnoreCase(activeProfiles, "dev"))) {
                message = e.getMessage();
            }

            // 数据完整性违规异常-505
            if (isInstanceofExceptionClass(e, DataIntegrityViolationException)) {
                return sqlDataIntegrityViolation(message);
            }

            // 数据访问异常-506
            return sqlError(message);
        }

        // 处理所有未处理异常-500
        log.error("【未处理异常】信息如下：", e);
        return internalServerError(ExceptionUtils.getPrintExceptionToJson(e));
    }

    private static boolean isInstanceofExceptionClass(Throwable e, String exceptionClass) {
        return ClassLoaderUtil.isPresent(exceptionClass) && ClassLoaderUtil.loadClass(exceptionClass).isAssignableFrom(e.getClass());
    }

}
