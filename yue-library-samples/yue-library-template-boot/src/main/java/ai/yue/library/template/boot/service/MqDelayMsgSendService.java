package ai.yue.library.template.boot.service;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqDelayMsgSendService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * @param message
     * @param delay   延时时间，单位毫秒
     */
    public void send(String message, Integer delay) {
        MessagePostProcessor processor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 设置消息延时处理的时间
                message.getMessageProperties().setDelay(delay);
                return message;
            }
        };
        rabbitTemplate.convertAndSend("delayed.exchange", "delayed", message, processor);
        System.out.println("发送的延时消息：" + message);
    }

}
