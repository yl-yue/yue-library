package ai.yue.library.test.service.district;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.yue.library.base.view.Result;
import ai.yue.library.base.view.ResultInfo;
import ai.yue.library.test.dao.district.DistrictDAO;
import ai.yue.library.test.dataobject.district.DistrictDO;

/**
 * @author  孙金川
 * @version 创建时间：2018年7月10日
 */
@Service
public class DistrictService {

	@Autowired
	DistrictDAO districtDAO;

	/**
	 * 获得省份
	 * @return
	 */
	public Result<List<DistrictDO>> listProvince() {
		return ResultInfo.success(districtDAO.listProvince());
	}

	/**
	 * 获得城市
	 * @param provinceId
	 * @return
	 */
	public Result<List<DistrictDO>> listCity(String provinceId) {
		return ResultInfo.success(districtDAO.listCity(provinceId));
	}

}
