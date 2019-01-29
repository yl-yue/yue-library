package ai.yue.library.base.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.servlet.http.HttpSession;

import ai.yue.library.base.ipo.CaptchaIPO;
import ai.yue.library.base.vo.CaptchaVO;

/**
 * @author  孙金川
 * @version 创建时间：2018年4月3日
 */
public class CaptchaUtils {
	
	/**
	 * captcha Key
	 */
	public static final String CAPTCHA_KEY = "captcha";
	
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
     * @param captchaIPO
     * @return
     */
    public static CaptchaVO createCaptchaImage(CaptchaIPO captchaIPO) {
    	int width = captchaIPO.getWidth();
    	int height = captchaIPO.getHeight();
    	int charQuantity = captchaIPO.getCharQuantity();
    	int fontSize = captchaIPO.getFontSize();
    	int interferingLineQuantity =  captchaIPO.getInterferingLineQuantity();
    	
        StringBuffer captcha = new StringBuffer();
        // 1.创建空白图片
        BufferedImage captchaImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 2.获取图片画笔
        Graphics graphic = captchaImage.getGraphics();
        // 3.设置画笔颜色
        graphic.setColor(Color.LIGHT_GRAY);
        // 4.绘制矩形背景
        graphic.fillRect(0, 0, width, height);
        // 5.画随机字符
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
        // 6.画干扰线
        for (int i = 0; i < interferingLineQuantity; i++) {
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 随机画线
            graphic.drawLine(ran.nextInt(width), ran.nextInt(height), ran.nextInt(width), ran.nextInt(height));
        }
        graphic.dispose();
        // 7.返回验证码和图片
        return CaptchaVO.builder().captcha(captcha.toString()).captchaImage(captchaImage).build();
    }
    
    /**
     * 验证-验证码
     * @param captcha
     * @return 
     */
    public static boolean isValidateCaptcha(String captcha) {
    	HttpSession httpSession = HttpUtils.getSession();
		String randCaptcha = (String) httpSession.getAttribute(CAPTCHA_KEY);
		if (StringUtils.isEmpty(randCaptcha) || !randCaptcha.equalsIgnoreCase(captcha)) {
			return false;
		}
		
		httpSession.removeAttribute(CAPTCHA_KEY);
		return true;
    }
    
}
