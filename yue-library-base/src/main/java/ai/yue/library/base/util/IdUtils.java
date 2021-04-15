package ai.yue.library.base.util;

import cn.hutool.core.util.IdUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Id工具类
 * 
 * @author	ylyue
 * @since	2018年1月19日
 */
public class IdUtils extends IdUtil {

	/**
	 * 获得小写的32位UUID（去掉中划线）
	 *
	 * @return 小写32位UUID
	 */
	public static String getSimpleUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 获得大写的32位UUID（去掉中划线）
	 *
	 * @return 大写32位UUID
	 */
	public static String getSimpleUUIDToUpperCase() {
		return getSimpleUUID().toUpperCase();
	}

	/**
	 * 获得16纯数字随机单号
	 *
	 * @return 16纯数字随机单号
	 */
	public static String getOrderNo16() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		sb.append(System.currentTimeMillis());
		sb.append(random.nextInt(10));
		sb.append(random.nextInt(10));
		sb.append(random.nextInt(10));
		return sb.toString();
	}

	/**
	 * 获得19纯数字随机单号
	 *
	 * @return 19纯数字随机单号
	 */
	public static String getOrderNo19() {
		String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String trandNo = String.valueOf((Math.random() * 9 + 1));
		orderNo += trandNo.substring(5, 10);
		return orderNo;
	}

	/**
	 * 获得随机生成n位数字编码（纯数字）
	 *
	 * @param length 长度
	 * @return 对应长度的数字编码（纯数字）
	 */
    public static String getRandomNumberCode(int length) {
        //字符源，可以根据需要删减
        String randomCodeSource = "0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            //循环随机获得当次字符
            code.append(randomCodeSource.charAt((int) Math.floor(Math.random() * randomCodeSource.length())));
        }

        return code.toString();
    }

    /**
	 * 获得随机生成n位字符编码（数字+字母）
	 *
	 * @param length 长度
	 * @return 对应长度的字符编码（数字+字母）
	 */
    public static String getRandomCode(int length) {
        //字符源，可以根据需要删减
        String randomCodeSource = "23456789abcdefghgklmnpqrstuvwxyzABCDEFGHGKLMNPQRSTUVWXYZ";//去掉1和i ，0和o
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            //循环随机获得当次字符
            code.append(randomCodeSource.charAt((int) Math.floor(Math.random() * randomCodeSource.length())));
        }

        return code.toString();
    }

	/**
	 * 获得随机生成n位大写字母编码（数字+大写字母）
	 *
	 * @param length 长度
	 * @return 对应长度的大写字母编码（数字+大写字母）
	 */
	public static String getRandomCodeToUpperCase(int length) {
		//字符源，可以根据需要删减
		String randomCodeSource = "23456789ABCDEFGHGKLMNPQRSTUVWXYZ";//去掉1和i ，0和o
		StringBuilder code = new StringBuilder();
		for (int i = 0; i < length; i++) {
			//循环随机获得当次字符
			code.append(randomCodeSource.charAt((int) Math.floor(Math.random() * randomCodeSource.length())));
		}

		return code.toString();
	}

}
