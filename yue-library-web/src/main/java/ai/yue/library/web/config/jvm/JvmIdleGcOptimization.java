package ai.yue.library.web.config.jvm;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.v7.core.math.NumberUtil;
import cn.hutool.v7.core.thread.ThreadUtil;
import cn.hutool.v7.core.util.RuntimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;

/**
 * JVM 空闲 GC 调优
 */
@Slf4j
@Configuration
@EnableScheduling
@ConditionalOnBean(MetricsEndpoint.class)
@ConditionalOnProperty(prefix = "yue.web", name = "enable-jvm-idle-optimization", havingValue = "true", matchIfMissing = true)
public class JvmIdleGcOptimization {

    @Autowired(required = false)
    MetricsEndpoint metricsEndpoint;

    /**
     * 空闲期间异步完整GC
     * 1. java12之前，由于G1 GC特性，即使在服务空闲期间，也不会进行完整的内存回收，导致垃圾数据仍然会占用大量内存不释放，这非常不利于云原生场景的弹性资源策略
     * 2. java12之后，说是会在空闲期间进行内存回收，但经过严谨测试与观察，发现几乎没什么效果，因此java17之后，此特性仍然保留，请放心使用
     * 3. G1 GC在高并发期间会开辟更多的内存空间，而在空闲时期既不清理这些过期数据，也不将申请的内存空间释放，返还给操作系统
     * 4. List对象会占用大量内存不及时释放，就算jvm处于空闲期间，内存也一直被占用，需要在单独的线程中调用`System.gc()`内存才会及时释放
     * 5. 在业务请求期间，就算调用`list.clear()`、`list = null`、`System.gc()`，内存也不会释放，这会导致jvm需要的内存越来越多，最终内存溢出
     */
	@Scheduled(cron = "50 0/3 * * * *")
    public void gc() {
        // 打印GC执行前，内存使用情况
        long maxMemory = RuntimeUtil.getMaxMemory() / 1024 / 1024;
        long usableMemory = RuntimeUtil.getUsableMemory() / 1024 / 1024;
        long totalMemory = RuntimeUtil.getTotalMemory() / 1024 / 1024;
        long freeMemory = RuntimeUtil.getFreeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;
        JSONObject print = new JSONObject();
        print.put("jvm最大上限内存", maxMemory + "MB");
        print.put("jvm最大可用内存", usableMemory + "MB");
        print.put("jvm当前申请内存", totalMemory + "MB");
        print.put("jvm当前使用内存", usedMemory + "MB");
        print.put("jvm当前空闲内存", freeMemory + "MB");
        log.info("jvm内存使用情况：{}", print);

        // 内存使用已超过3/2
        if (usableMemory < maxMemory * 0.35) {
            if (metricsEndpoint == null) {
                return;
            }
            MetricsEndpoint.MetricDescriptor metric = metricsEndpoint.metric("process.cpu.usage", null);
            int cpuUsage = NumberUtil.roundHalfEven(BigDecimal.valueOf(metric.getMeasurements().get(0).getValue() * 100), 0).intValue();
            int processorCount = RuntimeUtil.getProcessorCount();

            // 程序空闲时，进行GC释放内存
            if ((processorCount <= 4 && cpuUsage <= 8) || (processorCount > 4 && cpuUsage <= 5)) {
                System.gc();

                // 打印GC执行后，内存使用情况
                ThreadUtil.sleep(500);
                long maxMemoryGc = RuntimeUtil.getMaxMemory() / 1024 / 1024;
                long usableMemoryGc = RuntimeUtil.getUsableMemory() / 1024 / 1024;
                long totalMemoryGc = RuntimeUtil.getTotalMemory() / 1024 / 1024;
                long freeMemoryGc = RuntimeUtil.getFreeMemory() / 1024 / 1024;
                long usedMemoryGc = totalMemoryGc - freeMemoryGc;
                JSONObject printGc = new JSONObject();
                printGc.put("jvm最大上限内存", maxMemoryGc + "MB");
                printGc.put("jvm最大可用内存", usableMemoryGc + "MB");
                printGc.put("jvm当前申请内存", totalMemoryGc + "MB");
                printGc.put("jvm当前使用内存", usedMemoryGc + "MB");
                printGc.put("jvm当前空闲内存", freeMemoryGc + "MB");
                log.info("jvm内存使用情况-显式GC调用执行后：{}", printGc);
            }
        }
    }

}
