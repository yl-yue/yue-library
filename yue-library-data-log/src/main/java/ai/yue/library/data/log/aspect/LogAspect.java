package ai.yue.library.data.log.aspect;

import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.log.annotation.Log;
import ai.yue.library.data.log.config.LogProperties;
import ai.yue.library.data.log.constant.LogTypeEnum;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.service.OperLogService;
import ai.yue.library.data.log.spi.LogUserProvider;
import ai.yue.library.data.log.util.LogUtils;
import ai.yue.library.data.mybatis.constant.DbCrudEnum;
import ai.yue.library.web.util.ServletUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import cn.hutool.v7.core.text.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 日志 AOP 切面
 * <p>拦截 @Log 注解方法，自动记录操作日志</p>
 *
 * @author ylyue
 * @since 2025/5/13
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Resource
    private LogProperties logProperties;

    @Resource
    private OperLogService operLogService;

    @Around("@annotation(logAnnotation)")
    public Object around(ProceedingJoinPoint point, Log logAnnotation) throws Throwable {
        Object result = null;
        Throwable exception = null;

        try {
            result = point.proceed();
        } catch (Throwable t) {
            exception = t;
        }

        try {
            if (logAnnotation.type() == LogTypeEnum.OPER) {
                recordOperLog(logAnnotation, result, exception);
            }
        } catch (Exception e) {
            log.warn("【日志切面】记录日志异常：{}", e.getMessage());
        }

        if (exception != null) {
            throw exception;
        }

        return result;
    }

    private void recordOperLog(Log logAnnotation, Object result, Throwable exception) {
        OperLogEntity entity = OperLogEntity.builder()
                .title(logAnnotation.title())
                .description(logAnnotation.description())
                .bizType(logAnnotation.bizType())
                .operType(inferOperType(logAnnotation))
                .username(getUsername())
                .ip(getClientIP())
                .operTime(System.currentTimeMillis())
                .requestUrl(getRequestUrl())
                .requestMethod(getHttpMethod())
                .requestParam(logAnnotation.saveRequestData() ? captureRequestParam() : "")
                .responseResult(logAnnotation.saveResponseData() ? captureResponseResult(result) : "")
                .status(inferStatus(result, exception))
                .build();

        Set<String> excludeParamNames = resolveExcludeParamNames(logAnnotation);
        operLogService.recordOper(entity, excludeParamNames);
    }

    private Set<String> resolveExcludeParamNames(Log logAnnotation) {
        String[] names = logAnnotation.excludeParamNames();
        if (names == null || names.length == 0) {
            return Set.of();
        }
        return new HashSet<>(Arrays.asList(names));
    }

    private String inferOperType(Log logAnnotation) {
        DbCrudEnum operType = logAnnotation.operType();
        if (operType != null && operType != DbCrudEnum.R) {
            return operType.name();
        }

        String httpMethod = getHttpMethod();
        DbCrudEnum inferred = LogUtils.getOperTypeFromHttpMethod(httpMethod);
        return inferred.name();
    }

    private Integer inferStatus(Object result, Throwable exception) {
        if (exception != null) {
            return 0;
        }

        if (result instanceof Result<?> r) {
            return r.isFlag() ? 1 : 0;
        }

        return 1;
    }

    private String getUsername() {
        try {
            LogUserProvider provider = SpringUtils.getBean(LogUserProvider.class);
            return provider.getUsername();
        } catch (Exception e) {
            return "";
        }
    }

    private String getClientIP() {
        try {
            return ServletUtils.getClientIP();
        } catch (Exception e) {
            return "";
        }
    }

    private String getRequestUrl() {
        try {
            return ServletUtils.getRequest().getRequestURI();
        } catch (Exception e) {
            return "";
        }
    }

    private String getHttpMethod() {
        try {
            return ServletUtils.getRequest().getMethod();
        } catch (Exception e) {
            return "";
        }
    }

    private String captureRequestParam() {
        try {
            JSONObject paramJson = ServletUtils.getParamToJson();
            if (paramJson == null || paramJson.isEmpty()) {
                return "";
            }

            String param = paramJson.toJSONString();
            int maxParamSize = logProperties.getMaxParamSize();
            if (param.length() > maxParamSize) {
                return param.substring(0, maxParamSize);
            }

            return param;
        } catch (Exception e) {
            return "";
        }
    }

    private String captureResponseResult(Object result) {
        if (result == null) {
            return "";
        }

        try {
            if (!(result instanceof Result<?>)) {
                return "";
            }

            String jsonResult = JSON.toJSONString(result);
            if (StrUtil.isBlank(jsonResult) || !jsonResult.trim().startsWith("{")) {
                return "";
            }

            int maxResponseSize = logProperties.getMaxResponseSize();
            if (jsonResult.length() > maxResponseSize) {
                return "";
            }

            return jsonResult;
        } catch (Exception e) {
            return "";
        }
    }

    }