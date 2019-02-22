package ai.yue.library.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import cn.hutool.core.util.IdUtil;

/**
 * UUID工具类
 * @author  孙金川
 * @version 创建时间：2018年1月19日
 */
public class UUIDUtils extends IdUtil {

	/**
	 * 大写32位UUID
	 * @return 大写32位UUID
	 */
	public static String upperCaseUUID() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}
	
	/**
	 * 小写32位UUID
	 * @return 小写32位UUID
	 */
	public static String lowerCaseUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 生成19纯数字随机单号
	 * @return 19纯数字随机单号
	 */
	public static String getOrderNo_19() {
		String orderNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String trandNo = String.valueOf((Math.random() * 9 + 1));
		orderNo += trandNo.substring(5, 10);
		return orderNo;
	}
	
	/**
	 * 生成16纯数字随机单号
	 * @return 16纯数字随机单号
	 */
	public static String getOrderNo_16() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		sb.append(System.currentTimeMillis());
		sb.append(random.nextInt(10));
		sb.append(random.nextInt(10));
		sb.append(random.nextInt(10));
		return sb.toString();
	}
	
	/**
	 * 随机生成n位数字编码（纯数字）
	 * @param length 长度
	 * @return 对应长度的数字编码（纯数字）
	 */
    public static String randomNumberCode(int length) {
        //字符源，可以根据需要删减
        String randomCodeSource = "0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            //循环随机获得当次字符
            code.append(String.valueOf(randomCodeSource.charAt((int) Math.floor(Math.random() * randomCodeSource.length()))));
        }
        return code.toString();
    }
    
	/**
	 * 随机生成n位大写字母编码（数字+大写字母）
	 * @param length 长度
	 * @return 对应长度的大写字母编码（数字+大写字母）
	 */
    public static String randomCapitalCode(int length) {
        //字符源，可以根据需要删减
        String randomCodeSource = "23456789ABCDEFGHGKLMNPQRSTUVWXYZ";//去掉1和i ，0和o
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            //循环随机获得当次字符
            code.append(String.valueOf(randomCodeSource.charAt((int) Math.floor(Math.random() * randomCodeSource.length()))));
        }
        return code.toString();
    }
    
    /**
	 * 随机生成n位字符编码（数字+字母）
	 * @param length 长度
	 * @return 对应长度的字符编码（数字+字母）
	 */
    public static String randomCode(int length) {
        //字符源，可以根据需要删减
        String randomCodeSource = "23456789abcdefghgklmnpqrstuvwxyzABCDEFGHGKLMNPQRSTUVWXYZ";//去掉1和i ，0和o
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            //循环随机获得当次字符
            code.append(String.valueOf(randomCodeSource.charAt((int) Math.floor(Math.random() * randomCodeSource.length()))));
        }
        return code.toString();
    }
	
}
