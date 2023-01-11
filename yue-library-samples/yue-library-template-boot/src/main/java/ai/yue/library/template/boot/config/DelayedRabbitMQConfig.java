package ai.yue.library.template.boot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class DelayedRabbitMQConfig {
    @Bean
    CustomExchange delayedExchange() {
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("delayed.exchange", "x-delayed-message", true, false, args);
    }

    @Bean
    Queue delayedQueue() {
        return new Queue("delayed.queue", true);
    }

    @Bean
    Binding delayedBinding() {
        return BindingBuilder.bind(delayedQueue()).to(delayedExchange()).with("delayed").noargs();
    }
}