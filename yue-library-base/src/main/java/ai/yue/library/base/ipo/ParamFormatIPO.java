package ai.yue.library.base.ipo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参数美化IPO
 * 
 * @author	孙金川
 * @since	2018年6月22日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParamFormatIPO {
	
	String key;
	Class<?> clazz;
	
}
