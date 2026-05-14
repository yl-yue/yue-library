package ai.yue.library.data.log.mapper;

import ai.yue.library.data.log.entity.OperLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志 Mapper
 *
 * @author ylyue
 * @since 2025/5/13
 */
@Mapper
public interface OperLogMapper extends BaseMapper<OperLogEntity> {

}
