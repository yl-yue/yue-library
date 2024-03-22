package ai.yue.library.data.redis.instance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.redisson.api.RBlockingQueue;

import java.util.List;

/**
 * 有界阻塞队列
 * <p>建议在非必要的情况下，都使用有界阻塞队列操作，避免队列未消费，导致redis内存暴增</p>
 * <p>redis属于轻量级分布式队列，如果需要更加健壮可靠的队列操作，请根据业务需求使用：RabbitMQ、RocketMQ、Kafka等专业队列中间件实现</p>
 */
@Getter
@AllArgsConstructor
public class BoundedBlockingQueue<V> {

    /**
     * https://github.com/redisson/redisson/issues/3020
     * RBoundedBlockingQueue有界阻塞队列有bug，capacity值只减不增，导致队列最终可用容量为0，无法继续插入数据
     */
    RBlockingQueue<V> boundedBlockingQueue;

    /**
     * 添加list有界阻塞队列值
     * <p>队列大小限制为100000，超过丢弃</p>
     *
     * @param list list值
     */
    public boolean addData(List<V> list) {
        return addData(list, 100000);
    }

    /**
     * 添加list有界阻塞队列值
     *
     * @param list      list值
     * @param blockSize 队列最大大小，队列满后，继续添加的数据将被直接丢弃
     */
    public boolean addData(List<V> list, int blockSize) {
        int size = boundedBlockingQueue.size();
        if (size >= blockSize) {
            return false;
        }
        return boundedBlockingQueue.addAll(list);
    }

    /**
     * 获取并删除list有界阻塞队列值
     * <p>每次最大消费1000条数据</p>
     */
    public List<V> consumerData() {
        return consumerData(1000);
    }

    /**
     * 获取并删除list有界阻塞队列值
     *
     * @param consumeSize 一次消费多少条数据
     */
    public List<V> consumerData(int consumeSize) {
        return boundedBlockingQueue.poll(consumeSize);
    }

}
