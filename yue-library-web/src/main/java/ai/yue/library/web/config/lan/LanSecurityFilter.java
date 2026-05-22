package ai.yue.library.web.config.lan;

import ai.yue.library.base.view.R;
import ai.yue.library.web.util.NetUtils;
import ai.yue.library.web.util.ServletUtils;
import cn.hutool.v7.core.text.StrUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

public class LanSecurityFilter extends OncePerRequestFilter {

	private static final String LAN_PATH_PREFIX = "/lan/";

	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	private final LanSecurityProperties properties;

	private final String expectedCredentials;

	public LanSecurityFilter(LanSecurityProperties properties) {
		this.properties = properties;
		String username = properties.getBasicAuth().getUsername();
		String password = properties.getBasicAuth().getPassword();
		this.expectedCredentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String uri = request.getRequestURI();
		if (!uri.startsWith(LAN_PATH_PREFIX)) {
			return true;
		}
		for (String pattern : properties.getExcludePathPatterns()) {
			if (pathMatcher.match(pattern, uri)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String clientIp = ServletUtils.getClientIP(request);

		if (NetUtils.isInnerIP(request) || isInExtraWhitelist(clientIp)) {
			filterChain.doFilter(request, response);
			return;
		}

		if (!properties.getBasicAuth().isEnabled()) {
			logForbidden(response, request.getRequestURI(), clientIp);
			return;
		}

		String authHeader = request.getHeader("Authorization");
		if (StrUtil.isBlank(authHeader) || !authHeader.startsWith("Basic ")) {
			logUnauthorized(response, request.getRequestURI(), clientIp, "未提供凭据");
			return;
		}

		String credentials = authHeader.substring(6);
		if (!expectedCredentials.equals(credentials)) {
			logUnauthorized(response, request.getRequestURI(), clientIp, "凭据错误");
			return;
		}

		filterChain.doFilter(request, response);
	}

	private boolean isInExtraWhitelist(String ip) {
		for (String cidr : properties.getIpWhitelistExtra()) {
			if (NetUtils.isInRange(ip, cidr)) {
				return true;
			}
		}
		return false;
	}
 
	private void logForbidden(HttpServletResponse response, String uri, String clientIp) {
		logger.warn("【/lan/安全拦截】IP拒绝：uri=%s, clientIp=%s".formatted(uri, clientIp));
		R.forbidden("请求 " + uri + " 路径来自公网IP " + clientIp + "，被安全策略拒绝").response(response);
	}

	private void logUnauthorized(HttpServletResponse response, String uri, String clientIp, String reason) {
		logger.warn("【/lan/安全拦截】认证失败：uri=%s, clientIp=%s, 原因=%s".formatted(uri, clientIp, reason));
		response.setHeader("WWW-Authenticate", "Basic realm=\"LAN API\"");
		R.unauthorizedError("请求 " + uri + " 路径需要 Basic Auth 认证").response(response);
	}

}
