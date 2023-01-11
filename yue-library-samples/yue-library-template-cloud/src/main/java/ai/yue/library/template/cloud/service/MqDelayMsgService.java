package ai.yue.library.template.cloud.service;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * MQ延迟消息
 *
 * @author yl-yue
 * @since 2023/1/10
 */
@Service
public class MqDelayMsgService {

    @Autowired
    private StreamBridge streamBridge;

    public void producer(String message, Integer delay) {
        Map<String, Object> headers = new HashMap<>();
        headers.put(MessageProperties.X_DELAY, delay);
        GenericMessage<String> stringMessage = new GenericMessage<>(message, headers);
        streamBridge.send("producer-out-0", stringMessage);
        System.out.println("******************");
        System.out.println(" Sending value: " + message);
        System.out.println("******************");
    }

    @Bean
    public Consumer<Message<String>> consumer() {
        return msg -> {
            System.out.println();
            System.out.println("Consumer,Consumer,Consumer");
            System.out.println(Thread.currentThread().getName() + " Consumer Receive New Messages: " + msg.getPayload());
            System.out.println("Consumer,Consumer,Consumer");
            System.out.println();
        };
    }

}
