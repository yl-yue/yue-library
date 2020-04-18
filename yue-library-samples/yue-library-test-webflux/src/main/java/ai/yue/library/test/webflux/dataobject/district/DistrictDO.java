package ai.yue.library.test.webflux.dataobject.district;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

/**
 * @author  孙金川
 * @version 创建时间：2018年7月10日
 */
@Data
public class DistrictDO {

	Integer adcode;
	String citycode;
	String name;
	JSONObject location;
	String level;

	public void setLocation(String location) {
		this.location = JSONObject.parseObject(location);
	}

}
