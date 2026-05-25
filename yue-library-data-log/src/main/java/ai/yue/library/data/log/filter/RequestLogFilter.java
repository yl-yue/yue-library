package ai.yue.library.data.log.filter;

import ai.yue.library.data.log.config.LogProperties;
import ai.yue.library.web.util.ServletUtils;
import cn.hutool.v7.core.data.id.IdUtil;
import cn.hutool.v7.core.text.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * 请求日志拦截器
 * <p>MDC 注入 traceId / Accept-Language，打印请求信息</p>
 *
 * @author ylyue
 * @since 2025/5/25
 */
@Slf4j
@RequiredArgsConstructor
public class RequestLogFilter extends OncePerRequestFilter {

    private final LogProperties logProperties;

    private static final String MDC_TRACE_ID = "traceId";

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 1. traceId 自适应：优先复用 MDC 已有的（micrometer-tracing / plumelog 生成），否则生成 requestId
        String traceId = MDC.get(MDC_TRACE_ID);
        boolean traceIdInjected = false;
        if (StrUtil.isBlank(traceId)) {
            traceId = IdUtil.fastSimpleUUID();
            MDC.put(MDC_TRACE_ID, traceId);
            traceIdInjected = true;
        }

        // 2. Accept-Language 注入 MDC
        String acceptLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        boolean languageInjected = false;
        if (StrUtil.isNotBlank(acceptLanguage)) {
            MDC.put(HttpHeaders.ACCEPT_LANGUAGE, acceptLanguage);
            languageInjected = true;
        }

        // 3. 打印请求信息（不打印敏感 header）
        String requestIp = ServletUtils.getClientIP(request);
        String requestUri = request.getMethod() + " " + request.getRequestURI();
        JSONObject printParam = new JSONObject();
        printParam.put("requestIp", requestIp);
        printParam.put("requestUri", requestUri);
        printParam.put("acceptLanguage", acceptLanguage);
        log.info("request: {}", printParam);

        // 4. 执行后续逻辑
        try {
            filterChain.doFilter(request, response);
        } finally {
            // 5. MDC 清理（仅清理本 Filter 注入的 key）
            if (traceIdInjected) {
                MDC.remove(MDC_TRACE_ID);
            }
            if (languageInjected) {
                MDC.remove(HttpHeaders.ACCEPT_LANGUAGE);
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        List<String> excludePaths = logProperties.getRequestLog().getExcludePaths();
        if (excludePaths == null || excludePaths.isEmpty()) {
            return false;
        }
        return excludePaths.stream().anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }

}
