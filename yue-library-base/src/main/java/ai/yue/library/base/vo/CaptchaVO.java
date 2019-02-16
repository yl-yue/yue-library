package ai.yue.library.base.vo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ai.yue.library.base.util.CaptchaUtils;
import ai.yue.library.base.util.HttpUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  孙金川
 * @version 创建时间：2018年7月23日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaVO {

	/** 验证码 */
	String captcha;
	/** 验证码图片 */
	BufferedImage captchaImage;
	
	/**
	 * {@linkplain #captcha} set session
	 * <p>{@linkplain #captchaImage} write response
	 * <p>将验证码设置到session
	 * <p>将验证码图片写入response，并设置ContentType为image/png
	 */
	public void writeResponseAndSetSession() {
		HttpSession httpSession = HttpUtils.getSession();
		HttpServletResponse response = HttpUtils.getResponse();
		
		httpSession.setAttribute(CaptchaUtils.CAPTCHA_KEY, captcha);
        response.setContentType("image/png");
        
        OutputStream output;
		try {
			output = response.getOutputStream();
			// 响应结束时servlet会自动将output关闭
			ImageIO.write(captchaImage, "png", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
