package ai.yue.library.template.boot.dao;

import ai.yue.library.template.boot.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserMapper基于BaseEntity操作示例
 * 
 * @author	ylyue
 * @since	2019年9月25日
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 确认-用户
     */
    boolean isUser(String cellphone);

    /**
     * 单个
     */
    User get(String cellphone, String password);

}
