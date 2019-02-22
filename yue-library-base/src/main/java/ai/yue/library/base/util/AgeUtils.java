package ai.yue.library.base.util;

import java.util.Calendar;

/**
 * @author  孙金川
 * @version 创建时间：2018年4月19日
 */
public class AgeUtils {
	
	/**
	 * 根据年月日计算年龄
	 * @param birthDate 出生日期（格式：yyyy-MM-dd）
	 * @return 年龄
	 */
	public static int getAgeFromBirthDate(String birthDate) {
		// 先截取到字符串中的年、月、日
		String strs[] = birthDate.trim().split("-");
		int selectYear = Integer.parseInt(strs[0]);
		int selectMonth = Integer.parseInt(strs[1]);
		int selectDay = Integer.parseInt(strs[2]);
		// 得到当前时间的年、月、日
		Calendar cal = Calendar.getInstance();
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayNow = cal.get(Calendar.DATE);

		// 用当前年月日减去生日年月日
		int yearMinus = yearNow - selectYear;
		int monthMinus = monthNow - selectMonth;
		int dayMinus = dayNow - selectDay;
		
		int age = 0;// 年龄初始值
		if (yearMinus > 0) {
			if (monthMinus <= 0 && dayMinus < 0) {// 今年未过生日
				age = yearMinus - 1;
			}else {// 今年已过生日
				age = yearMinus;
			}
		}
		return age;
	}
	
	/**
	 * 根据年龄获得生日（默认月份日期-01-01）
	 * @param age 年龄
	 * @return 生日（日期格式）
	 */
	public static String getBirthDateFromAge(int age) {
		// 得到当前时间的年、月、日
		Calendar cal = Calendar.getInstance();
		int yearNow = cal.get(Calendar.YEAR);
		int birthdayYear = yearNow - age;
		String birthday = birthdayYear + "-01-01";
		return birthday;
	}
	
}
