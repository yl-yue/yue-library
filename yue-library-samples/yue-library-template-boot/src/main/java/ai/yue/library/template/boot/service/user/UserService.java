package ai.yue.library.template.boot.service.user;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.util.ParamUtils;
import ai.yue.library.base.validation.Validator;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultPrompt;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.template.boot.dao.user.UserDAO;
import ai.yue.library.template.boot.dataobject.user.UserDO;
import ai.yue.library.template.boot.ipo.user.UserIPO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户CRUD实体示例
 *
 * @author	ylyue
 * @since	2019年9月25日
 */
@Service
public class UserService {

	@Autowired
	Validator validator;
	@Autowired
	UserDAO userDAO;
	
	/**
	 * 插入数据
	 * 
	 * @param userIPO
	 * @return
	 */
	public Result<?> insert(UserIPO userIPO) {
		// 1. 确认用户是否存在
		if (userDAO.isUser(userIPO.getCellphone())) {
			return R.errorPrompt(ResultPrompt.USER_EXIST);
		}
		
		// 2. 插入数据并返回结果
		return R.success(userDAO.insert(Convert.toJSONObject(userIPO)));
	}
	
	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	public Result<?> delete(Long id) {
		userDAO.delete(id);
		return R.success();
	}
	
	/**
	 * 单个
	 * 
	 * @param id
	 * @return
	 */
	public Result<?> get(Long id) {
		return R.success(userDAO.get(id));
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
		UserDO userDO = userDAO.get(cellphone, password);
		if (userDO == null) {
			return R.errorPrompt(ResultPrompt.USERNAME_OR_PASSWORD_ERROR);
		}
		
		// 2. 返回结果
		userDO.setUserId(userDO.getId());
		return R.success(userDO);
	}
	
	/**
	 * 分页
	 * 
	 * @param paramJson
	 * @return
	 */
	public Result<?> page(JSONObject paramJson) {
		String[] mustContainKeys = {"page", "limit"};
		String[] canContainKeys = {"user_status"};
		ParamUtils.paramValidate(paramJson, mustContainKeys, canContainKeys);
		return userDAO.page(PageIPO.parsePageIPO(paramJson)).toResult();
	}
	
	/**
	 * 列表-全部
	 * 
	 * @return
	 */
	public Result<?> listAll() {
		return R.success(userDAO.listAll());
	}

}
