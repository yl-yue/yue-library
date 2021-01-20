package ai.yue.library.test.controller.data.jdbc.query.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author	ylyue
 * @since	2020年3月18日
 */
@Getter
@AllArgsConstructor
public enum PersonStatusEnum {

	normal("正常"),
	miss("失踪"),
	died("死亡");

	String message;
}
