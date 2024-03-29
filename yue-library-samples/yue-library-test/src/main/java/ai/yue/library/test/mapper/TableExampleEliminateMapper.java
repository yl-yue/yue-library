package ai.yue.library.test.mapper;

import ai.yue.library.test.entity.TableExampleEliminate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TableExampleEliminateMapper extends BaseMapper<TableExampleEliminate> {

    TableExampleEliminate getDataNotLogicDelete();

    void updateLogicDelete();

    void delLogicDelete();

}
