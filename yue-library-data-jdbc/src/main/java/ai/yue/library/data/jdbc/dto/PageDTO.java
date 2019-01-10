package ai.yue.library.data.jdbc.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;

/**
 * @author  孙金川
 * @version 创建时间：2018年7月24日
 */
@Getter
@Builder
public class PageDTO {
	
	Long count;
	String querySql;
	Map<String, Object> paramMap;
	
}
