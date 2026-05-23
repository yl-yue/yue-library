package ai.yue.library.data.log.service;

import org.springframework.stereotype.Service;

import ai.yue.library.base.util.AsyncUtils;
import ai.yue.library.data.log.config.LogProperties;
import ai.yue.library.data.log.entity.OperLogEntity;
import ai.yue.library.data.log.ipo.OperLogPageIPO;
import ai.yue.library.data.log.mapper.OperLogMapper;
import ai.yue.library.data.log.spi.LogStorageProvider;
import ai.yue.library.web.util.ServletUtils;
import cn.hutool.v7.core.text.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class OperLogService {

	@Resource
	private LogStorageProvider logStorageProvider;

	@Resource
	private LogProperties logProperties;

	@Resource(name = "logMaskService")
	private LogMaskService logMaskService;

	@Resource
	private OperLogMapper operLogMapper;

	public void recordOper(OperLogEntity entity) {
		recordOper(entity, Set.of());
	}

	/**
	 * 记录操作日志
	 *
	 * @param entity              操作日志实体
	 * @param excludeParamNames   注解级追加排除的参数名集合
	 */
	public void recordOper(OperLogEntity entity, Set<String> excludeParamNames) {
		try {
			if (logProperties.getMaskEnabled() && StrUtil.isNotBlank(entity.getRequestParam())) {
				entity.setRequestParam(logMaskService.maskParam(entity.getRequestParam(), excludeParamNames));
			}

			if (logProperties.getAsync()) {
				AsyncUtils.exec(() -> {
					try {
						logStorageProvider.storeOperLog(entity);
					} catch (Exception e) {
						log.warn("【操作日志】异步记录失败：{}", e.getMessage());
					}
				});
			} else {
				logStorageProvider.storeOperLog(entity);
			}
		} catch (Exception e) {
			log.warn("【操作日志】记录异常：{}", e.getMessage());
		}
	}

	public PageInfo<OperLogEntity> pageOperLog(OperLogPageIPO ipo) {
		PageHelper.startPage(ServletUtils.getRequest());
		LambdaQueryWrapper<OperLogEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.likeRight(StrUtil.isNotBlank(ipo.getTitle()), OperLogEntity::getTitle, ipo.getTitle())
				.eq(StrUtil.isNotBlank(ipo.getBizType()), OperLogEntity::getBizType, ipo.getBizType())
				.eq(StrUtil.isNotBlank(ipo.getOperType()), OperLogEntity::getOperType, ipo.getOperType())
				.likeRight(StrUtil.isNotBlank(ipo.getUsername()), OperLogEntity::getUsername, ipo.getUsername())
				.eq(ipo.getStatus() != null, OperLogEntity::getStatus, ipo.getStatus())
				.ge(ipo.getStartTime() != null, OperLogEntity::getOperTime, ipo.getStartTime())
				.le(ipo.getEndTime() != null, OperLogEntity::getOperTime, ipo.getEndTime())
				.orderByDesc(OperLogEntity::getId);
		List<OperLogEntity> list = operLogMapper.selectList(wrapper);
		return PageInfo.of(list);
	}

	public OperLogEntity getOperLog(Long id) {
		return operLogMapper.selectById(id);
	}
}
