package ai.yue.library.data.jdbc.dto;

import com.alibaba.fastjson.JSONObject;

import lombok.Builder;
import lombok.Getter;

/**
 * 分页处理传输对象
 * 
 * @author	ylyue
 * @since	2018年7月24日
 */
@Getter
@Builder
public class PageDTO {
	
	Long count;
	String querySql;
	JSONObject paramJson;
	
}
