## QPS限流 <!-- {docsify-ignore} -->
1. 用于单服务限流，实现服务熔断降级，服务自己保障自身运行的稳定可靠性
  - 降级需要前端配合做统一降级处理，如：收到 `429` code，前端开始阻断请求10秒、20秒、40秒，实现阶段累加阻塞
2. 在网关处实现分布式接口统一限流，不要让超出的流量，打在服务上，导致服务挂掉
3. 在nginx前置网关处实现ip统一限流与安全限制，阻断无效流量，攻击流量

### 快速使用
单接口限流：
```java
@QpsLimit(qps = 5)
@GetMapping("/get")
public Result<?> get() {
    ThreadUtil.sleep(500);
    return R.success();
}
```

单服务全局限流：
```
yue:
  web: 
    enable-global-api-qps-limit: true     # 开启单服务全局限流，默认：开启
    global-api-qps: 3000                  # 单服务全局QPS限制，默认：3000
```

- 关闭单服务全局限流：`yue.web.enable-global-api-qps-limit=false`
- 关闭单接口限流：`yue.web.enable-api-qps-limit=fasle`
