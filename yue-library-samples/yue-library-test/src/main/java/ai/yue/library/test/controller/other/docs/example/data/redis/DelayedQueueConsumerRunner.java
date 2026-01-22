package ai.yue.library.test.controller.other.docs.example.data.redis;

import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.data.redis.instance.DelayedQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DelayedQueueConsumerRunner implements ApplicationRunner {

    final Redis redis;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 同步等待消费
		DelayedQueue<LocalDateTime> delayedQueue1 = redis.getDelayedQueue("delayedQueue1");
		delayedQueue1.consumerDelayedMsg(delayedMsg -> {
			System.out.println("接收到延迟消息：" + delayedMsg + ", 当前时间：" + LocalDateTime.now());
		});

		// 异步订阅消费
		DelayedQueue<LocalDateTime> delayedQueue2 = redis.getDelayedQueue("delayedQueue2");
		delayedQueue2.consumerDelayedMsgAsync(delayedMsg -> {
			System.out.println("接收到延迟消息：" + delayedMsg + ", 当前时间：" + LocalDateTime.now());
		});
	}

}
