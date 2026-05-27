package ai.yue.library.test.base.api.version;

import ai.yue.library.base.annotation.ApiVersion;
import ai.yue.library.base.config.properties.BaseProperties;
import ai.yue.library.base.exception.ApiDeprecatedException;
import ai.yue.library.web.config.api.version.ApiVersionRequestCondition;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiVersionRequestConditionTest {

	private final BaseProperties.ApiVersionProperties properties = new BaseProperties().new ApiVersionProperties();

	@Test
	@DisplayName("版本号在第一段，无 context-path — 正常匹配")
	void versionAtFirstSegmentWithoutContextPath() {
		ApiVersion apiVersion = mock(ApiVersion.class);
		when(apiVersion.value()).thenReturn(1.0);
		when(apiVersion.deprecated()).thenReturn(false);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/v2/apiVersion/get");
		when(request.getContextPath()).thenReturn("");

		ApiVersionRequestCondition condition = new ApiVersionRequestCondition(apiVersion, properties, 1);

		assertNotNull(condition.getMatchingCondition(request));
	}

	@Test
	@DisplayName("版本号不在第一段（/auth/{version}/xxx），无 context-path — 正常匹配")
	void versionAtNonFirstSegmentWithoutContextPath() {
		ApiVersion apiVersion = mock(ApiVersion.class);
		when(apiVersion.value()).thenReturn(2.0);
		when(apiVersion.deprecated()).thenReturn(false);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/auth/v3/apiVersion/get");
		when(request.getContextPath()).thenReturn("");

		ApiVersionRequestCondition condition = new ApiVersionRequestCondition(apiVersion, properties, 2);

		assertNotNull(condition.getMatchingCondition(request));
	}

	@Test
	@DisplayName("版本号不在第一段且有 context-path — 修复后应正常匹配（回归 Bug 场景）")
	void versionAtNonFirstSegmentWithContextPath() {
		ApiVersion apiVersion = mock(ApiVersion.class);
		when(apiVersion.value()).thenReturn(2.0);
		when(apiVersion.deprecated()).thenReturn(false);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/ssc-mqtt/auth/v2/apiVersion/get");
		when(request.getContextPath()).thenReturn("/ssc-mqtt");

		ApiVersionRequestCondition condition = new ApiVersionRequestCondition(apiVersion, properties, 2);

		assertNotNull(condition.getMatchingCondition(request));
	}

	@Test
	@DisplayName("版本号在第一段且有 context-path — 修复后应正常匹配")
	void versionAtFirstSegmentWithContextPath() {
		ApiVersion apiVersion = mock(ApiVersion.class);
		when(apiVersion.value()).thenReturn(1.0);
		when(apiVersion.deprecated()).thenReturn(false);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/ssc-mqtt/v2/apiVersion/get");
		when(request.getContextPath()).thenReturn("/ssc-mqtt");

		ApiVersionRequestCondition condition = new ApiVersionRequestCondition(apiVersion, properties, 1);

		assertNotNull(condition.getMatchingCondition(request));
	}

	@Test
	@DisplayName("废弃版本接口 — 抛出 ApiDeprecatedException")
	void deprecatedVersionThrowsException() {
		ApiVersion apiVersion = mock(ApiVersion.class);
		when(apiVersion.value()).thenReturn(1.0);
		when(apiVersion.deprecated()).thenReturn(true);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/v1/apiVersion/get");
		when(request.getContextPath()).thenReturn("");

		ApiVersionRequestCondition condition = new ApiVersionRequestCondition(apiVersion, properties, 1);

		assertThrows(ApiDeprecatedException.class, () -> condition.getMatchingCondition(request));
	}

	@Test
	@DisplayName("版本号低于接口要求 — 返回 null")
	void versionLowerThanRequiredReturnsNull() {
		ApiVersion apiVersion = mock(ApiVersion.class);
		when(apiVersion.value()).thenReturn(3.0);
		when(apiVersion.deprecated()).thenReturn(false);

		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/v2/apiVersion/get");
		when(request.getContextPath()).thenReturn("");

		ApiVersionRequestCondition condition = new ApiVersionRequestCondition(apiVersion, properties, 1);

		assertNull(condition.getMatchingCondition(request));
	}
}
