package ai.yue.library.base.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.servlet.http.HttpSession;

import ai.yue.library.base.ipo.CaptchaIPO;
import ai.yue.library.base.util.servlet.ServletUtils;
import ai.yue.library.base.vo.CaptchaVO;

/**
 * 验证码工具类，用于创建验证码图片与验证验证码
 * <p>若需要分布式验证，推荐使用 yue-library-data-redis 模块 User 类所提供的 getCaptchaImage() 方法
 * 
 * @author	ylyue
 * @since	2018年4月3日
 */
public class CaptchaUtils {
	
	/**
	 * Captcha Key
	 */
	public static final String CAPTCHA_KEY = "captcha";
	
	/**
	 * Captcha Redis 前缀
	 */
	public static final String CAPTCHA_REDIS_PREFIX = "captcha_%s";
	
    private static final char[] CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    
    static Color getRandomColor() {
        Random ran = new Random();
        Color color = new Color(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
        return color;
    }
    
    /**
     * 创建验证码
     * @param captchaIPO 验证码IPO
     * @return 验证码VO
     */
    public static CaptchaVO createCaptchaImage(CaptchaIPO captchaIPO) {
    	// 1. 解析参数
    	int width = captchaIPO.getWidth();
    	int height = captchaIPO.getHeight();
    	int charQuantity = captchaIPO.getCharQuantity();
    	int fontSize = captchaIPO.getFontSize();
    	int interferingLineQuantity =  captchaIPO.getInterferingLineQuantity();
    	
    	// 2. 创建空白图片
        StringBuffer captcha = new StringBuffer();
        BufferedImage captchaImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        // 3. 获取图片画笔
        Graphics graphic = captchaImage.getGraphics();
        
        // 4.设置画笔颜色
        graphic.setColor(Color.LIGHT_GRAY);
        
        // 5.绘制矩形背景
        graphic.fillRect(0, 0, width, height);
        
        // 6.画随机字符
        Random ran = new Random();
        for (int i = 0; i < charQuantity; i++) {
            // 取随机字符索引
            int n = ran.nextInt(CHARS.length);
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 设置字体大小
            graphic.setFont(new Font(
                    null, Font.BOLD + Font.ITALIC, fontSize));
            // 画字符
            graphic.drawString(CHARS[n] + "", i * width / charQuantity, height * 2 / 3);
            // 记录字符
            captcha.append(CHARS[n]);
        }
        
        // 7.画干扰线
        for (int i = 0; i < interferingLineQuantity; i++) {
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 随机画线
            graphic.drawLine(ran.nextInt(width), ran.nextInt(height), ran.nextInt(width), ran.nextInt(height));
        }
        graphic.dispose();
        
        // 8.返回验证码和图片
        return CaptchaVO.builder().captcha(captcha.toString()).captchaImage(captchaImage).build();
    }
    
    /**
     * 验证-验证码
     * 
     * @param captcha 验证码
     * @return 是否正确
     */
    public static boolean isValidateCaptcha(String captcha) {
    	HttpSession httpSession = ServletUtils.getSession();
		String randCaptcha = (String) httpSession.getAttribute(CAPTCHA_KEY);
		if (StringUtils.isEmpty(randCaptcha) || !randCaptcha.equalsIgnoreCase(captcha)) {
			return false;
		}
		
		httpSession.removeAttribute(CAPTCHA_KEY);
		return true;
    }
    
}
