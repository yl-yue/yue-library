package ai.yue.library.test.service;

import ai.yue.library.data.mybatis.service.BaseService;
import ai.yue.library.test.entity.User;
import ai.yue.library.test.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService<UserMapper, User> {

}
