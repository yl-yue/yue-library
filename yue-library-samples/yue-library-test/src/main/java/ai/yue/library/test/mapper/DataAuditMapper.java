package ai.yue.library.test.mapper;

import ai.yue.library.test.entity.DataAudit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据审计
 *
 * @author ylyue
 * @since 2021/7/26
 */
@Mapper
public interface DataAuditMapper extends BaseMapper<DataAudit> {

}
