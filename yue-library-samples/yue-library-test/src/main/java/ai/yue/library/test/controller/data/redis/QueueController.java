package ai.yue.library.test.controller.data.redis;

import ai.yue.library.base.convert.Convert;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.redis.client.Redis;
import ai.yue.library.data.redis.instance.DelayedQueue;
import ai.yue.library.test.dto.ConvertDTO;
import ai.yue.library.test.service.ParamService;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 队列测试
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/queue")
public class QueueController {

    final Redis redis;
    final ParamService paramService;

    /**
     * 持续消费延迟队列
     */
//    @PostConstruct
    public void consumerDelayedMsg() {
        DelayedQueue<LocalDateTime> delayedQueue = redis.getDelayedQueue("test");
        delayedQueue.consumerDelayedMsg(delayedMsg -> {
            System.out.println("接收到延迟消息：" + delayedMsg + ", 当前时间：" + LocalDateTime.now());
        });

        DelayedQueue<LocalDateTime> delayedQueue2 = redis.getDelayedQueue("test2");
        delayedQueue2.consumerDelayedMsg(delayedMsg -> {
            System.out.println("接收到延迟消息：" + delayedMsg + ", 当前时间：" + LocalDateTime.now());
        });
    }

    /**
     * 发送一次，接收一次（无消息会一直阻塞，直到接收一次为止）
     */
    @PostMapping("/delayedQueue")
    public Result<?> delayedQueue() {
        // test
        DelayedQueue<LocalDateTime> delayedQueueTest = redis.getDelayedQueue("test");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("发送延迟消息：" + now);
        delayedQueueTest.sendDelayedMsg(now, 5, TimeUnit.SECONDS);
        LocalDateTime delayedMsg = delayedQueueTest.consumerDelayedMsg();
        System.out.println("接收到延迟消息：" + delayedMsg + ", 当前时间：" + LocalDateTime.now());

        // test2
        DelayedQueue<LocalDateTime> delayedQueueTest2 = redis.getDelayedQueue("test2");
        LocalDateTime now2 = LocalDateTime.now();
        System.out.println("发送延迟消息：" + now2);
        delayedQueueTest2.sendDelayedMsg(now2, 5, TimeUnit.SECONDS);
        LocalDateTime delayedMsg2 = delayedQueueTest2.consumerDelayedMsg();
        System.out.println("接收到延迟消息：" + delayedMsg2 + ", 当前时间：" + LocalDateTime.now());
        return R.success();
    }

    /**
     * 发送一次
     */
    @PostMapping("/sendDelayedMsg")
    public Result<?> sendDelayedMsg(long delay) {
        // test
        LocalDateTime now = LocalDateTime.now();
        DelayedQueue<LocalDateTime> delayedQueue = redis.getDelayedQueue("test");
        delayedQueue.sendDelayedMsg(now, delay, TimeUnit.SECONDS);
        System.out.println("发送延迟消息：" + now);

        // test2
        LocalDateTime now2 = LocalDateTime.now();
        DelayedQueue<LocalDateTime> delayedQueue2 = redis.getDelayedQueue("test2");
        delayedQueue2.sendDelayedMsg(now2, delay, TimeUnit.SECONDS);
        System.out.println("发送延迟消息：" + now2);
        return R.success();
    }

    /**
     * 消费延迟消息（无消息会一直阻塞，直到接收对应数量为止）
     */
    @PostMapping("/consumerDelayedMsg")
    public Result<?> consumerDelayedMsg(int consumeNum) {
        for (int i = 0; i < consumeNum; i++) {
            // test
            DelayedQueue<LocalDateTime> delayedQueueTest = redis.getDelayedQueue("test");
            LocalDateTime delayedMsg = delayedQueueTest.consumerDelayedMsg();
            System.out.println("接收到延迟消息1：" + delayedMsg + ", 当前时间：" + LocalDateTime.now());

            // test2
            DelayedQueue<LocalDateTime> delayedQueueTest2 = redis.getDelayedQueue("test2");
            LocalDateTime delayedMsg2 = delayedQueueTest2.consumerDelayedMsg();
            System.out.println("接收到延迟消息2：" + delayedMsg2 + ", 当前时间：" + LocalDateTime.now());
        }
        return R.success();
    }

    @PostMapping("/addBoundedBlockingQueueData")
    public Result<?> addBoundedBlockingQueueData(int addSize) {
        double doubleValue = 1.32D;
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Double> doubleValues = new ArrayList<>();
        List<LocalDateTime> localDateTimes = new ArrayList<>();
        JSONObject paramJson = paramService.getParamJson();
        ConvertDTO javaBean = Convert.toJavaBean(paramService.getParamJson(), ConvertDTO.class);

        for (int i = 0; i < addSize; i++) {
            doubleValues.add(doubleValue + i);
            localDateTimes.add(localDateTime);
        }
        redis.<Double>getBoundedBlockingQueue("redisSerializer:doubleValue").addData(doubleValues, 10000);
        redis.<LocalDateTime>getBoundedBlockingQueue("redisSerializer:localDateTime").addData(localDateTimes, 10000);
        redis.<JSONObject>getBoundedBlockingQueue("redisSerializer:paramJson").addData(Arrays.asList(paramJson, paramJson, paramJson));
        redis.<ConvertDTO>getBoundedBlockingQueue("redisSerializer:javaBean").addData(Arrays.asList(javaBean, javaBean, javaBean));
        return R.success(doubleValues);
    }

    @GetMapping("/consumeBoundedBlocking")
    public Result<?> consumeBoundedBlocking(int consumeSize) {
        List<Double> doubleValueQueueValue = redis.<Double>getBoundedBlockingQueue("redisSerializer:doubleValue").consumerData(consumeSize);
        List<LocalDateTime> localDateTimeQueueValue = redis.<LocalDateTime>getBoundedBlockingQueue("redisSerializer:localDateTime").consumerData(consumeSize);
        List<JSONObject> paramJsonQueueValue = redis.<JSONObject>getBoundedBlockingQueue("redisSerializer:paramJson").consumerData(consumeSize);
        List<ConvertDTO> javaBeanQueueValue = redis.<ConvertDTO>getBoundedBlockingQueue("redisSerializer:javaBean").consumerData(consumeSize);
        System.out.println("======get queue======");
        System.out.println("doubleValueQueueValue: " + doubleValueQueueValue);
        System.out.println("localDateTimeQueueValue: " + localDateTimeQueueValue);
        System.out.println("paramJsonQueueValue: " + paramJsonQueueValue);
        System.out.println("javaBeanQueueValue: " + javaBeanQueueValue);
        System.out.println();
        System.out.println();
        return R.success(doubleValueQueueValue);
    }

}
