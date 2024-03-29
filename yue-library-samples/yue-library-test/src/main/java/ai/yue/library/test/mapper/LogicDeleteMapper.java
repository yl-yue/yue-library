package ai.yue.library.test.mapper;

import ai.yue.library.test.entity.TableExampleStandard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 逻辑删除测试
 *
 * @author	ylyue
 * @since	2020年2月21日
 */
@Mapper
public interface LogicDeleteMapper extends BaseMapper<TableExampleStandard> {

    TableExampleStandard getLogicDelete();

    List<TableExampleStandard> listLogicDelete();

    void updateLogicDelete();

    void delLogicDelete();

}
