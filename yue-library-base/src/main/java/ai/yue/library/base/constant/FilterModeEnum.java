package ai.yue.library.base.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 筛选方式枚举
 * 
 * @author  孙金川
 * @version 创建时间：2018年9月18日
 */
@Getter
@AllArgsConstructor
public enum FilterModeEnum {
	
	小于(" < "),
	大于(" > "),
	等于(" = "),
	小于等于(" <= "),
	大于等于(" >= "),
	不等于(" != "),
	包含(" LIKE ") {
		@Override
		public String processFilterSqlCode(String value) {
			return value = filterSqlCode + "'%" + value + "%'";
		}
	},
	不包含(" NOT LIKE ") {
		@Override
		public String processFilterSqlCode(String value) {
			return value = filterSqlCode + "'%" + value + "%'";
		}
	};
	
	/**
	 * 筛选SQL代码
	 */
	String filterSqlCode;
	
	/**
	 * 加工筛选SQL代码
	 * <p>需要加工的枚举：{@linkplain #包含}，{@linkplain #不包含}
	 * @param value 处理的值
	 */
	public String processFilterSqlCode(String value) {
		return filterSqlCode;
	}
	
}
