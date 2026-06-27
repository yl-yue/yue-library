package ai.yue.library.webflux.config.exception;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Sa-Token 异常精细处理
 * <p>使用最高优先级 {@link Ordered#HIGHEST_PRECEDENCE}，确保 Sa-Token 异常在精细层被框架强制处理，
 * 不被业务项目的 {@code @ExceptionHandler(Exception.class)} 兜底抢占。
 * <p>实际处理委托 {@link R#getResult(Throwable)}，复用统一的异常识别逻辑。
 * <p>权限异常属于安全控制，统一返回模糊的"无权限"(403)，不向前端泄露角色/权限标识。
 *
 * @author	ylyue
 * @since	2026年6月27日
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class SaTokenExceptionHandler {

	/**
	 * Sa-Token 登录认证异常（未登录/token失效等）-401
	 */
	@ResponseBody
	@ExceptionHandler(NotLoginException.class)
	public Result<?> notLoginExceptionHandler(NotLoginException e) {
		return R.getResult(e);
	}

	/**
	 * Sa-Token 角色校验异常（@SaCheckRole 失败）-403
	 */
	@ResponseBody
	@ExceptionHandler(NotRoleException.class)
	public Result<?> notRoleExceptionHandler(NotRoleException e) {
		return R.getResult(e);
	}

	/**
	 * Sa-Token 权限校验异常（@SaCheckPermission 失败）-403
	 */
	@ResponseBody
	@ExceptionHandler(NotPermissionException.class)
	public Result<?> notPermissionExceptionHandler(NotPermissionException e) {
		return R.getResult(e);
	}

}
