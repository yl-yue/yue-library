package ai.yue.library.template.boot.service.user;

import ai.yue.library.base.util.ParamUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.jdbc.ipo.PageIPO;
import ai.yue.library.template.boot.constant.user.UserStatusEnum;
import ai.yue.library.template.boot.dao.user.UserJsonDAO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户CRUD JSON示例
 * @author	ylyue
 * @since	2020年2月13日
 */
@Service
public class UserJsonService {

	@Autowired
	UserJsonDAO userJsonDAO;
	
	/**
	 * 插入数据
	 * 
	 * @param paramJson
	 * @return
	 */
	public Result<?> insert(JSONObject paramJson) {
		String[] mustContainKeys = {"cellphone", "password"};
		String[] canContainKeys = {"nickname", "email", "head_img", "sex", "birthday"};
		ParamUtils.paramValidate(paramJson, mustContainKeys, canContainKeys);
		paramJson.put("user_status", UserStatusEnum.normal.name());
		return R.success(userJsonDAO.insert(paramJson));
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
		return userJsonDAO.page(PageIPO.parsePageIPO(paramJson)).toResult();
	}
	
}
