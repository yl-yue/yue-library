package ai.yue.library.data.log.mapper;

import ai.yue.library.data.log.entity.LoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志 Mapper
 *
 * @author ylyue
 * @since 2025/5/13
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLogEntity> {

}
