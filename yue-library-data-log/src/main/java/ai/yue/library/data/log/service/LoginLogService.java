package ai.yue.library.data.log.service;

import org.springframework.stereotype.Service;

import ai.yue.library.base.util.AsyncUtils;
import ai.yue.library.data.log.config.LogProperties;
import ai.yue.library.data.log.entity.LoginLogEntity;
import ai.yue.library.data.log.ipo.LoginLogPageIPO;
import ai.yue.library.data.log.mapper.LoginLogMapper;
import ai.yue.library.data.log.spi.LogStorageProvider;
import ai.yue.library.web.util.ServletUtils;
import cn.hutool.v7.core.text.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
public class LoginLogService {

	@Resource
	private LogStorageProvider logStorageProvider;

	@Resource
	private LogProperties logProperties;

	@Resource(name = "logMaskService")
	private LogMaskService logMaskService;

	@Resource
	private LoginLogMapper loginLogMapper;

	public void recordLogin(String username, String ip, boolean success, String msg, String requestParam) {
		try {
			LoginLogEntity entity = LoginLogEntity.builder()
					.username(username)
					.ip(ip)
					.loginTime(System.currentTimeMillis())
					.status(success ? 1 : 0)
					.msg(msg)
					.requestParam(maskParam(requestParam))
					.build();

			if (logProperties.getAsync()) {
				AsyncUtils.exec(() -> {
					try {
						logStorageProvider.storeLoginLog(entity);
					} catch (Exception e) {
						log.warn("【登录日志】异步记录失败：{}", e.getMessage());
					}
				});
			} else {
				logStorageProvider.storeLoginLog(entity);
			}
		} catch (Exception e) {
			log.warn("【登录日志】记录异常：{}", e.getMessage());
		}
	}

	public PageInfo<LoginLogEntity> pageLoginLog(LoginLogPageIPO ipo) {
		PageHelper.startPage(ServletUtils.getRequest());
		LambdaQueryWrapper<LoginLogEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.likeRight(StrUtil.isNotBlank(ipo.getUsername()), LoginLogEntity::getUsername, ipo.getUsername())
				.eq(ipo.getStatus() != null, LoginLogEntity::getStatus, ipo.getStatus())
				.ge(ipo.getStartTime() != null, LoginLogEntity::getLoginTime, ipo.getStartTime())
				.le(ipo.getEndTime() != null, LoginLogEntity::getLoginTime, ipo.getEndTime())
				.orderByDesc(LoginLogEntity::getId);
		List<LoginLogEntity> list = loginLogMapper.selectList(wrapper);
		return PageInfo.of(list);
	}

	public LoginLogEntity getLoginLog(Long id) {
		return loginLogMapper.selectById(id);
	}

	private String maskParam(String param) {
		if (!logProperties.getMaskEnabled() || param == null) {
			return param;
		}
		return logMaskService.maskParam(param);
	}
}
