package ai.yue.library.base.util;

import cn.hutool.core.util.RandomUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务工具类
 * <pre>
 *     常见业务算法封装
 * </pre>
 */
public class BusinessUtils {

    /**
     * 红包分配（二倍均值法）
     * <p>本次金额 = (0.01, (剩余金额 / 剩余数量) * 2)</p>
     *
     * @param remainAmount 剩余金额
     * @param remainNum    剩余数量
     */
    public static BigDecimal redEnvelopeDistribution(BigDecimal remainAmount, Integer remainNum) {
        if (remainNum == 1) {
            return remainAmount;
        }

        BigDecimal averageValue = remainAmount.divide(BigDecimal.valueOf(remainNum), 2, RoundingMode.HALF_UP);
        BigDecimal maxValue = averageValue.multiply(BigDecimal.valueOf(2));
        BigDecimal maxAmount = RandomUtil.randomBigDecimal(BigDecimal.valueOf(0.01), maxValue);
        BigDecimal winningAmount = maxAmount.setScale(2, RoundingMode.DOWN);
        return winningAmount;
    }

    /**
     * 预红包分配（二倍均值法）
     * <p>每次金额 = (0.01, (剩余金额 / 剩余数量) * 2)</p>
     *
     * @param totalAmount 总金额
     * @param totalNum    总数量
     */
    public static List<BigDecimal> redEnvelopeDistributionPre(BigDecimal totalAmount, Integer totalNum) {
        List<BigDecimal> redEnvelopeList = new ArrayList<>();
        BigDecimal remainAmount = totalAmount;
        for (int remainNum = totalNum; remainNum > 0; remainNum--) {
            BigDecimal winningAmount = redEnvelopeDistribution(remainAmount, remainNum);
            remainAmount = remainAmount.subtract(winningAmount);
            redEnvelopeList.add(winningAmount);
        }

        return redEnvelopeList;
    }

}
