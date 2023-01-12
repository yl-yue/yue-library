package ai.yue.library.template.boot.service;

import ai.yue.library.base.validation.Validator;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultPrompt;
import ai.yue.library.data.mybatis.service.BaseService;
import ai.yue.library.template.boot.mapper.UserMapper;
import ai.yue.library.template.boot.entity.User;
import ai.yue.library.template.boot.ipo.UserIPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户CRUD实体示例
 *
 * @author	ylyue
 * @since	2019年9月25日
 */
@Service
public class UserService extends BaseService<UserMapper, User> {

	@Autowired
	Validator validator;
	
	/**
	 * 插入数据
	 * 
	 * @param userIPO
	 * @return
	 */
	public Result<?> insert(UserIPO userIPO) {
		// 1. 确认用户是否存在
		if (baseMapper.isUser(userIPO.getCellphone())) {
			return R.errorPrompt(ResultPrompt.USER_EXIST);
		}
		
		// 2. 插入数据并返回结果
		return insert(userIPO);
	}
	
	/**
	 * 登录
	 * 
	 * @param cellphone
	 * @param password
	 * @return
	 */
	public Result<?> login(String cellphone, String password) {
		// 1. 查询用户
		User userDO = baseMapper.get(cellphone, password);
		if (userDO == null) {
			return R.errorPrompt(ResultPrompt.USERNAME_OR_PASSWORD_ERROR);
		}
		
		// 2. 返回结果
		userDO.setUserId(userDO.getId());
		return R.success(userDO);
	}
	
}
