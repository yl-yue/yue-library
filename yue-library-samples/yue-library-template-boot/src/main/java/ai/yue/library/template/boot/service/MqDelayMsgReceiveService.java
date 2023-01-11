package ai.yue.library.template.boot.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MqDelayMsgReceiveService {

    @RabbitListener(queues = "delayed.queue")
    public void receive2(String message) {
        System.out.println("收到的延时消息：" + message);
    }

}