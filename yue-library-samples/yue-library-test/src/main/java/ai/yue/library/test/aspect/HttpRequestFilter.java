package ai.yue.library.test.aspect;

import ai.yue.library.base.util.StrUtils;
import ai.yue.library.data.mybatis.constant.DbConstant;
import ai.yue.library.web.util.ServletUtils;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * HTTP请求日志
 *
 * @author 石昊
 * @since 2019年9月25日
 */
@Slf4j
public class HttpRequestFilter extends OncePerRequestFilter {

    public static final String ACCEPT_LANGUAGE = HttpHeaders.ACCEPT_LANGUAGE;
    public static final String TENANT_CO_ID = DbConstant.CLASS_TENANT_CO_ID;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 获取request接口信息
        String requestIp = ServletUtils.getClientIP(request);
        String requestUri = request.getMethod() + " " + request.getRequestURI();

        // 2. 配置请求上下文
        String acceptLanguage = request.getHeader(ACCEPT_LANGUAGE);
        String tenantCoId = request.getHeader(TENANT_CO_ID);
        if (StrUtils.isNotEmpty(acceptLanguage)) {
            MDC.put(ACCEPT_LANGUAGE, acceptLanguage);
        }
        if (StrUtils.isNotEmpty(tenantCoId)) {
            MDC.put(TENANT_CO_ID, tenantCoId);
        }

        // 3. 打印日志
        JSONObject printParam = new JSONObject();
        printParam.put("requestIp", requestIp);
        printParam.put("requestUri", requestUri);
        log.info("request: {}", printParam);

        // 4. 执行后续逻辑
        filterChain.doFilter(request, response);
    }

}
