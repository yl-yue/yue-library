package ai.yue.library.test.webflux.dao.district;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;

import ai.yue.library.base.util.StringUtils;
import ai.yue.library.data.jdbc.dao.AbstractRepository;
import ai.yue.library.test.webflux.dataobject.district.DistrictDO;

/**
 * @author  孙金川
 * @version 创建时间：2018年7月5日
 */
@Repository
public class DistrictDAO extends AbstractRepository<DistrictDO> {

	@Override
	protected String tableName() {
		return null;
	}
	
	/**
	 * 获得省份
	 * @return
	 */
	public List<DistrictDO> listProvince() {
		return db.queryAll("district_province", DistrictDO.class);
	}

	/**
	 * 获得城市
	 * @param provinceId
	 * @return
	 */
	public List<DistrictDO> listCity(String provinceId) {
		if (StringUtils.isEmpty(provinceId)) {
			return db.queryAll("district_city", DistrictDO.class);
		}

		String adcode = provinceId.substring(0, 2);
		JSONObject paramJson = new JSONObject();
		paramJson.put("adcode", adcode);
		String sql = "SELECT * FROM district_city WHERE adcode LIKE CONCAT(:adcode, '%')";
		return db.queryForList(sql, paramJson, DistrictDO.class);
	}

}
