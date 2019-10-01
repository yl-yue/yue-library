package ai.yue.library.template.simple.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.crypto.client.SecureSingleton;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import ai.yue.library.base.view.ResultPrompt;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.template.simple.dao.user.UserDAO;
import ai.yue.library.template.simple.dataobject.UserDO;

/**
 * @author	ylyue
 * @since	2019年9月25日
 */
@Service
public class UserService {

	@Autowired
	UserDAO userDAO;
	
	/**
	 * 插入数据
	 * 
	 * @param paramJson
	 * @return
	 */
	public Result<?> insert(JSONObject paramJson) {
		return ResultInfo.success(userDAO.insert(paramJson));
	}
	
	/**
	 * 单个
	 * 
	 * @param id
	 * @return
	 */
	public Result<?> get(Long id) {
		return ResultInfo.success(userDAO.get(id));
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
		password = SecureSingleton.getAES().encryptBase64(password);
		UserDO userDO = userDAO.get(cellphone, password);
		if (userDO == null) {
			return ResultInfo.dev_defined(ResultPrompt.USERNAME_OR_PASSWORD_ERROR);
		}
		
		// 2. 返回结果
		return ResultInfo.success(userDO);
	}
	
	/**
	 * 分页
	 * 
	 * @param paramJson
	 * @return
	 */
	public Result<?> page(JSONObject paramJson) {
		return ResultInfo.success(userDAO.page(PageIPO.parsePageIPO(paramJson)));
	}
	
	/**
	 * 列表-全部
	 * 
	 * @return
	 */
	public Result<?> listAll() {
		return ResultInfo.success(userDAO.listAll());
	}
	
	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	public Result<?> delete(Long id) {
		userDAO.delete(id);
		return ResultInfo.success();
	}
	
}
