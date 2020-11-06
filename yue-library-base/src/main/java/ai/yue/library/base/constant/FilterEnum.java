package ai.yue.library.base.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 筛选方式
 * <p>依赖 {@link CompareEnum} 标准扩展
 * 
 * @author	ylyue
 * @since	2018年9月18日
 */
@Getter
@AllArgsConstructor
public enum FilterEnum {
	
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
	GE(" >= "),
	/** 包含 */
	LIKE(" LIKE ") {
		@Override
		public String processFilterSqlCode(String value) {
			return value = filterSqlCode + "'%" + value + "%'";
		}
	},
	/** 不包含 */
	NOTLIKE(" NOT LIKE ") {
		@Override
		public String processFilterSqlCode(String value) {
			return value = filterSqlCode + "'%" + value + "%'";
		}
	};
	// NULL NOTNULL
	
	/**
	 * 筛选SQL代码
	 */
	String filterSqlCode;
	
	/**
	 * 加工筛选SQL代码
	 * <p>需要加工的枚举：{@linkplain #LIKE}，{@linkplain #NOTLIKE}
	 * 
	 * @param value 处理的值
	 * @return 筛选SQL代码
	 */
	public String processFilterSqlCode(String value) {
		return filterSqlCode;
	}
	
}
