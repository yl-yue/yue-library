package ai.yue.library.base.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 比较方式
 * 
 * @author	ylyue
 * @since	2018年9月18日
 */
@Getter
@AllArgsConstructor
public enum CompareEnum {
	
	/** 等于（equal to） */
	EQ(" = "),
	/** 不等于（not equal to） */
	NE(" != "),
	/** 小于（less than） */
	LT(" < "),
	/** 小于等于（less than or equal to） */
	LE(" <= "),
	/** 大于（greater than） */
	GT(" > "),
	/** 大于等于（greater than or equal to） */
	GE(" >= ");
	
	/**
	 * 代码值
	 */
	String code;
	
}
