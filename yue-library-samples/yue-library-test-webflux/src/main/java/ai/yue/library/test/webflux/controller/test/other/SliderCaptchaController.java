package ai.yue.library.test.webflux.controller.test.other;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author	ylyue
 * @since	2019年8月9日
 */
@RestController
@RequestMapping("/sliderCaptcha")
public class SliderCaptchaController {

	@PostMapping("/isVerify")
	public boolean isVerify(List<Integer> datas) {
		int sum = 0;
		for (Integer data : datas) {
			sum += data;
		}
		double avg = sum * 1.0 / datas.size();
		
		double sum2 = 0.0;
		for (Integer data : datas) {
			sum2 += Math.pow(data - avg, 2);
		}
		
		double stddev = sum2 / datas.size();
		return stddev != 0;
	}
	
}
