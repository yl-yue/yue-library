package ai.yue.library.web.grpc.config.exception;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.exception.*;
import ai.yue.library.base.util.ExceptionUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;

import javax.validation.Valid;
import java.util.List;

/**
 * <h2>全局统一异常处理</h2>
 * <b>统一异常响应体使用HTTP状态码与GRPC状态码职责对照：</b>
 * <br><br>
 * <table>
 *     <thead>
 *         <tr>
 *             <th>HTTP状态码</th>
 *             <th>GRPC状态码</th>
 *             <th>异常范围</th>
 *         </tr>
 *     </thead>
 *     <tbody>
 *         <tr>
 *             <td>4xx</td>
 *             <td>3 INVALID_ARGUMENT</td>
 *             <td>客户端错误</td>
 *         </tr>
 *         <tr>
 *             <td>5xx</td>
 *             <td>13 INTERNAL</td>
 *             <td>服务端异常</td>
 *         </tr>
 *         <tr>
 *             <td>600</td>
 *             <td>12 UNIMPLEMENTED</td>
 *             <td>业务提示</td>
 *         </tr>
 *     </tbody>
 * </table>
 *
 * @author ylyue
 * @since 2022年5月20日
 */
@Slf4j
@GrpcAdvice
@ConditionalOnProperty(prefix = "yue.exception-handler", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ResultExceptionHandler {

    // RESTful 异常拦截

    /**
     * 拦截所有未处理异常-13 INTERNAL
     *
     * @param e 异常
     * @return 结果
     */
    @GrpcExceptionHandler(Exception.class)
    public synchronized Status exceptionHandler(Exception e) {
        Status status = Status.INTERNAL;
        ExceptionUtils.printException(e);
        return status.withDescription(R.getResult(e).castToJSONString());
    }

    /**
     * 异常结果处理-synchronized
     *
     * @param e 结果异常
     * @return 结果
     */
	@GrpcExceptionHandler(ResultException.class)
    public synchronized Status resultExceptionHandler(ResultException e) {
        var result = e.getResult();
        log.error(result.toString());
        ExceptionUtils.printException(e);
        Integer code = result.getCode();
        Status status = null;
        if (code >= 400 && code < 500) {
            // 400客户端错误-grpc code 3
            status = Status.INVALID_ARGUMENT;
        } else if (code >= 500 && code < 600) {
            // 500服务端错误-grpc code 13
            status = Status.INTERNAL;
        } else if (code == 600) {
            // 600业务提示-grpc code 12
            status = Status.UNIMPLEMENTED;
        } else {
            // 错误的使用RESTful code
            status = Status.INTERNAL;
            String dataPrompt = StrUtil.format("错误的使用RESTful code：4xx客户端错误、5xx服务端错误、600业务提示，当前code码为{}，不应该在异常响应中出现，请排查。", code);
            result = R.internalServerError(dataPrompt);
        }

        return status.withDescription(result.castToJSONString());
    }

    /**
     * 拦截登录异常（User）
     * <p>Grpc状态码：3 INVALID_ARGUMENT</p>
     * <p>Http状态码：401</p>
     *
     * @param e 登录异常
     * @return 结果
     */
    @GrpcExceptionHandler(LoginException.class)
    public Status loginExceptionHandler(LoginException e) {
        ExceptionUtils.printException(e);
        Status status = Status.INVALID_ARGUMENT;
        return status.withDescription(R.unauthorized().castToJSONString());
    }

    /**
     * 非法请求异常拦截
     * <p>Grpc状态码：3 INVALID_ARGUMENT</p>
     * <p>Http状态码：402</p>
     *
     * @param e 非法请求异常
     * @return 结果
     */
    @GrpcExceptionHandler(AttackException.class)
    public Status attackExceptionHandler(AttackException e) {
        ExceptionUtils.printException(e);
        Status status = Status.INVALID_ARGUMENT;
        return status.withDescription(R.attack(e.getMessage()).castToJSONString());
    }

    /**
     * 无权限异常访问处理
     * <p>Grpc状态码：3 INVALID_ARGUMENT</p>
     * <p>Http状态码：403</p>
     *
     * @param e 无权限异常
     * @return 结果
     */
    @GrpcExceptionHandler(ForbiddenException.class)
    public Status forbiddenExceptionHandler(ForbiddenException e) {
        ExceptionUtils.printException(e);
        Status status = Status.INVALID_ARGUMENT;
        return status.withDescription(R.forbidden().castToJSONString());
    }

    /**
     * 拦截API接口版本弃用异常
     * <p>Grpc状态码：3 INVALID_ARGUMENT</p>
     * <p>Http状态码：410</p>
     *
     * @param e API接口版本弃用异常
     * @return 结果
     */
    @GrpcExceptionHandler(ApiVersionDeprecatedException.class)
    public Status apiVersionDeprecatedExceptionHandler(ApiVersionDeprecatedException e) {
        ExceptionUtils.printException(e);
        Status status = Status.INVALID_ARGUMENT;
        return status.withDescription(R.gone().castToJSONString());
    }

    /**
     * 参数效验为空统一处理
     * <p>Grpc状态码：3 INVALID_ARGUMENT</p>
     * <p>Http状态码：432</p>
     *
     * @return 结果
     */
    @GrpcExceptionHandler(ParamVoidException.class)
    public Status paramVoidExceptionHandler() {
        Status status = Status.INVALID_ARGUMENT;
        return status.withDescription(R.paramVoid().castToJSONString());
    }

    /**
     * 参数效验未通过统一处理
     * <p>Grpc状态码：3 INVALID_ARGUMENT</p>
     * <p>Http状态码：433</p>
     *
     * @param e 参数校验未通过异常
     * @return 结果
     */
    @GrpcExceptionHandler(ParamException.class)
    public Status paramExceptionHandler(ParamException e) {
        ExceptionUtils.printException(e);
        Status status = Status.INVALID_ARGUMENT;
        return status.withDescription(R.paramCheckNotPass(e.getMessage()).castToJSONString());
    }

    /**
     * {@linkplain Valid} 验证异常统一处理
     * <p>Grpc状态码：3 INVALID_ARGUMENT</p>
     * <p>Http状态码：433</p>
     *
     * @param e 验证异常
     * @return 结果
     */
    @GrpcExceptionHandler(BindException.class)
    public Status bindExceptionHandler(BindException e) {
        List<ObjectError> errors = e.getAllErrors();
        JSONObject paramHint = new JSONObject();
        errors.forEach(error -> {
            String str = StrUtil.subAfter(error.getArguments()[0].toString(), "[", true);
            String key = str.substring(0, str.length() - 1);
            String msg = error.getDefaultMessage();
            paramHint.put(key, msg);
            Console.error(key + " " + msg);
        });

        Status status = Status.INVALID_ARGUMENT;
        return status.withDescription(R.paramCheckNotPass(paramHint).castToJSONString());
    }

    /**
     * 验证异常统一处理
     * <p>Grpc状态码：3 INVALID_ARGUMENT</p>
     * <p>Http状态码：433</p>
     *
     * @param e 验证异常
     * @return 结果
     */
    @GrpcExceptionHandler(ValidateException.class)
    public Status validateExceptionHandler(ValidateException e) {
        ExceptionUtils.printException(e);
        Result<?> result = null;
        try {
            result = R.paramCheckNotPass(Convert.toJSONArray(e.getMessage()));
        } catch (Exception exception) {
            result = R.paramCheckNotPass(e.getMessage());
        }

        Status status = Status.INVALID_ARGUMENT;
        return status.withDescription(result.castToJSONString());
    }

    /**
     * DB异常统一处理
     * <p>Grpc状态码：13 INTERNAL</p>
     * <p>Http状态码：506</p>
     *
     * @param e DB异常
     * @return 结果
     */
    @GrpcExceptionHandler(DbException.class)
    public Status dbExceptionHandler(DbException e) {
        e.printStackTrace();
        Result<?> result = null;
        if (e.isShowMsg()) {
            result = R.dbError(e.getMessage());
        } else {
            result = R.dbError();
        }

        Status status = Status.INTERNAL;
        return status.withDescription(result.castToJSONString());
    }

    /**
     * 服务降级
     * <p>Grpc状态码：13 INTERNAL</p>
     * <p>Http状态码：507</p>
     *
     * @param e 服务降级异常
     * @return 结果
     */
    @GrpcExceptionHandler(ClientFallbackException.class)
    public Status clientFallbackExceptionHandler(ClientFallbackException e) {
        ExceptionUtils.printException(e);
        Status status = Status.INTERNAL;
        return status.withDescription(R.clientFallback().castToJSONString());
    }

    /**
     * 服务降级
     * <p>Grpc状态码：13 INTERNAL</p>
     * <p>Http状态码：507</p>
     *
     * @param e grpc请求异常
     * @return 结果
     */
    @GrpcExceptionHandler(StatusRuntimeException.class)
    public Status statusRuntimeExceptionHandler(StatusRuntimeException e) {
        ExceptionUtils.printException(e);
        Status status = Status.INTERNAL;
        return status.withDescription(R.clientFallback().castToJSONString());
    }

    /**
     * 类型转换异常统一处理
     * <p>Grpc状态码：13 INTERNAL</p>
     * <p>Http状态码：509</p>
     *
     * @param e 转换异常
     * @return 结果
     */
    @GrpcExceptionHandler(ConvertException.class)
    public Status convertExceptionHandler(ConvertException e) {
        log.error("【类型转换异常】转换类型失败，错误信息如下：{}", e.getMessage());
        ExceptionUtils.printException(e);
        Status status = Status.INTERNAL;
        return status.withDescription(R.typeConvertError(e.getMessage()).castToJSONString());
    }

//    DuplicateKeyException Status.INVALID_ARGUMENT
//    IllegalArgumentException Status.INVALID_ARGUMENT

}
