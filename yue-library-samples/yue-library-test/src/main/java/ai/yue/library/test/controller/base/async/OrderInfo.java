package ai.yue.library.test.controller.base.async;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {
    private String orderId;
    private String orderName;
    private String orderDesc;
    private String orderStatus;
    private String orderCreateTime;
    private String orderUpdateTime;
    private String orderDeleteTime;
    private String orderCreateUser;
    private String orderUpdateUser;
    private String orderDeleteUser;
    private String orderCreateIp;
}
