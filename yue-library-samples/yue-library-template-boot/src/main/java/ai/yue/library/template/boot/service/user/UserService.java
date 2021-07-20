package ai.yue.library.template.boot.service.user;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.util.ParamUtils;
import ai.yue.library.base.validation.Validator;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultPrompt;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.template.boot.constant.user.RoleEnum;
import ai.yue.library.template.boot.dao.user.UserDAO;
import ai.yue.library.template.boot.dataobject.user.UserDO;
import ai.yue.library.template.boot.ipo.user.UserIPO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
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
	 * 注册
	 * 
	 * @param paramJson
	 * @return
	 */
	public Result<?> register(JSONObject paramJson) {
		// 1. 校验参数
		String[] mustContainKeys = {"cellphone", "password"};
		String[] canContainKeys = {"nickname", "email", "head_img", "sex", "birthday"};
		ParamUtils.paramValidate(paramJson, mustContainKeys, canContainKeys);

		// 2. 确认用户是否存在
		String cellphone = paramJson.getString("cellphone");
		validator.param(cellphone).cellphone("cellphone");
		if (userDAO.isUser(cellphone)) {
			return R.errorPrompt(ResultPrompt.USER_EXIST);
		}
		
		// 3. 加密密码
		String password = paramJson.getString("password");
		paramJson.replace("password", password);
		
		// 4. 插入数据
		paramJson.put("role", RoleEnum.b2c_买家.name());
		return R.success(userDAO.insert(paramJson));
	}
	
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
