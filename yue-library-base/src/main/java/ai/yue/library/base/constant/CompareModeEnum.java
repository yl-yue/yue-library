package ai.yue.library.base.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 比较方式枚举
 * 
 * @author  孙金川
 * @version 创建时间：2018年9月18日
 */
@Getter
@AllArgsConstructor
public enum CompareModeEnum {
	
	小于("<"),
	大于(">"),
	等于("="),
	小于等于("<="),
	大于等于(">="),
	不等于("!=");
	
	/**
	 * 关系运算符
	 */
	String relationalOperator;
	
}
