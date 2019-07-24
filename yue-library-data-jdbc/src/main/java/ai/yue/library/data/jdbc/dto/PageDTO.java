package ai.yue.library.data.jdbc.dto;

import com.alibaba.fastjson.JSONObject;

import lombok.Builder;
import lombok.Getter;

/**
 * @author	孙金川
 * @since	2018年7月24日
 */
@Getter
@Builder
public class PageDTO {
	
	Long count;
	String querySql;
	JSONObject paramJson;
	
}
