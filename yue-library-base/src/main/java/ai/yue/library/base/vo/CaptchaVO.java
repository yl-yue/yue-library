package ai.yue.library.base.vo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.ServletRequestAttributes;

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
	 * session- this.captcha<br>
	 * response- this.captchaImage
	 * <p>
	 * 设置验证码到session中<br>
	 * 设置验证码图片到response中，并设置ContentType为image/png<br>
	 */
	public void toLocalSessionAndResponse() {
		ServletRequestAttributes servletRequestAttributes = HttpUtils.getRequestAttributes();
		HttpServletRequest request = servletRequestAttributes.getRequest();
		HttpServletResponse response = servletRequestAttributes.getResponse();
		HttpSession httpSession = request.getSession();
		
		httpSession.setAttribute(CaptchaUtils.CAPTCHA_KEY, captcha);
        response.setContentType("image/png");
        
        OutputStream out;
		try {
			out = response.getOutputStream();
			// 响应结束时servlet会自动将out关闭
			ImageIO.write(captchaImage, "png", out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
