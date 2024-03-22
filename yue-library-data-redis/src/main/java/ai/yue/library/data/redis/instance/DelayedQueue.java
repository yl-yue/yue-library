package ai.yue.library.data.redis.instance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RFuture;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 延迟队列
 */
@Getter
@AllArgsConstructor
public class DelayedQueue<V> {

    /**
     * 发送消息队列
     */
    RDelayedQueue<V> producerQueue;
    /**
     * 消费消息队列
     */
    RBlockingQueue<V> consumerQueue;

    /**
     * 发送延迟消息
     *
     * @param msg      要发送的数据
     * @param delay    延迟多久
     * @param timeUnit 时间单位
     */
    public void sendDelayedMsg(V msg, long delay, TimeUnit timeUnit) {
        producerQueue.offer(msg, delay, timeUnit);
    }

    /**
     * 发送延迟消息（异步）
     *
     * @param msg      要发送的数据
     * @param delay    延迟多久
     * @param timeUnit 时间单位
     */
    public RFuture<Void> sendDelayedMsgAsync(V msg, long delay, TimeUnit timeUnit) {
        return producerQueue.offerAsync(msg, delay, timeUnit);
    }

    /**
     * 消费一条延迟消息
     * <p>如果没有消息将会一直阻塞到成功获取为止</p>
     */
    @SneakyThrows
    public V consumerDelayedMsg() {
        return consumerQueue.take();
    }

    /**
     * 消费一条延迟消息（异步）
     */
    public RFuture<V> consumerDelayedMsgAsync() {
        return consumerQueue.takeAsync();
    }

    /**
     * 持续消费延迟消息（监听延迟消息）
     *
     * @param consumer lambda实现消费逻辑
     * @return listenerId - 侦听器的id
     */
    public int consumerDelayedMsg(Consumer<V> consumer) {
        return consumerQueue.subscribeOnElements(consumer);
    }

}
