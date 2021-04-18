package ai.yue.library.test.controller.data.jdbc.query.map;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author	ylyue
 * @since	2020年11月2日
 */
@RestController
@RequestMapping("/queryMap/dataConvert")
public class DataConvertController {

	@Autowired
	PersonDAO personDAO;
	
	@GetMapping("/{id}")
	public Result<?> get(@PathVariable Long id) {
		PersonDO personDO = personDAO.get(id);
		System.out.println(personDO);
		PersonVO personVO = Convert.toJavaBean(personDO, PersonVO.class);
		System.out.println(personVO);
		PersonVO personVO2 = personDAO.getPersonVO(id);
		System.out.println(personVO2);
//		return R.success(personDO);
//		return R.success(personVO);
		return R.success(personVO2);
	}

}
