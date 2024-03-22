package ai.yue.library.test.dto;

import ai.yue.library.test.constant.TestEnum;
import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author	ylyue
 * @since	2020年8月4日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FastJsonHttpMessageConverterDTO implements Serializable {

	int inta;
	Integer intb;
	long longa;
	Long longb;
	boolean booleana;
	Boolean booleanb;
	String str;
	Map<?, ?> map;
	Map<?, ?> map2;
	JSONObject jsonObject;
	JSONObject jsonObject2;
	String[] arrayStr;
	long[] arrayLong;
	List<?> list;
	List<?> list2;
	TestEnum testEnum;
	Date date;
	DateTime dateTime;
	LocalDate localDate;
	LocalTime localTime;
	LocalDateTime localDateTime;
	
}
