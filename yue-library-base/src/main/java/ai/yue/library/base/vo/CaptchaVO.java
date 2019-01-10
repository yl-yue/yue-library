package ai.yue.library.base.vo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	 * @param session
	 * @param response
	 */
	public void toLocalSessionAndResponse(HttpSession session, HttpServletResponse response) {
        session.setAttribute("captcha", captcha);
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
