package ai.yue.library.test.base.util;

import ai.yue.library.base.util.BusinessUtils;
import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * 常见业务算法封装测试
 */
public class BusinessUtilsTest {

    @Test
    public void redEnvelopeDistributionTest() {
        BigDecimal totalAmount = BigDecimal.valueOf(RandomUtil.randomInt(89, 3000));
        Integer totalNum = RandomUtil.randomInt(1, 30);
        BigDecimal remainAmount = totalAmount;
        BigDecimal distributionAmount = BigDecimal.ZERO;
        Integer distributionNum = 0;
        for (int remainNum = totalNum; remainNum > 0; remainNum--) {
            BigDecimal currentMoney = BusinessUtils.redEnvelopeDistribution(remainAmount, remainNum);
            remainAmount = remainAmount.subtract(currentMoney);
            distributionAmount = distributionAmount.add(currentMoney);
            distributionNum++;
            System.out.println(remainNum + ": " + currentMoney);
        }
        System.out.println("totalAmount: " + totalAmount);
        System.out.println("totalNum: " + totalNum);
        System.out.println("remainAmount: " + remainAmount);
        System.out.println("distributionAmount: " + distributionAmount);
        System.out.println("distributionNum: " + distributionNum);
        assert remainAmount.doubleValue() == 0.0;
        assert totalAmount.doubleValue() == distributionAmount.doubleValue();
        assert distributionNum.equals(totalNum);
    }

    @Test
    public void redEnvelopeDistributionPreTest() {
        BigDecimal totalAmount = BigDecimal.valueOf(RandomUtil.randomInt(89, 3000));
        Integer totalNum = RandomUtil.randomInt(1, 30);
        List<BigDecimal> distributionAmountList = BusinessUtils.redEnvelopeDistributionPre(totalAmount, totalNum);
        Integer distributionNum = distributionAmountList.size();

        BigDecimal remainAmount = totalAmount;
        BigDecimal distributionAmount = BigDecimal.ZERO;
        for (int i = 0; i < distributionAmountList.size(); i++) {
            BigDecimal currentMoney = distributionAmountList.get(i);
            remainAmount = remainAmount.subtract(currentMoney);
            distributionAmount = distributionAmount.add(currentMoney);
            System.out.println(i + ": " + currentMoney);
        }

        System.out.println("totalAmount: " + totalAmount);
        System.out.println("totalNum: " + totalNum);
        System.out.println("remainAmount: " + remainAmount);
        System.out.println("distributionAmount: " + distributionAmount);
        System.out.println("distributionNum: " + distributionNum);
        assert remainAmount.doubleValue() == 0.0;
        assert totalAmount.doubleValue() == distributionAmount.doubleValue();
        assert distributionNum.equals(totalNum);
    }

}
