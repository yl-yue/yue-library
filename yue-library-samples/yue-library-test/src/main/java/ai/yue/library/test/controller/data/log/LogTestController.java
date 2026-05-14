package ai.yue.library.test.controller.data.log;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.log.annotation.Log;
import ai.yue.library.data.log.constant.LogTypeEnum;
import ai.yue.library.data.log.service.LoginLogService;
import ai.yue.library.data.mybatis.constant.DbCrudEnum;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志模块测试接口
 *
 * @author ylyue
 * @since 2025/5/13
 */
@RestController
@RequestMapping("/data/log")
public class LogTestController {

    @Resource
    private LoginLogService loginLogService;

    @Log(title = "查询用户", description = "测试GET请求自动推断为R类型")
    @GetMapping("/get")
    public Result<?> get(String username) {
        return R.success(username);
    }

    @Log(title = "新增用户", description = "测试POST请求自动推断为C类型")
    @PostMapping("/post")
    public Result<?> post(String username, String password) {
        return R.success(username);
    }

    @Log(title = "更新用户", description = "测试PUT请求自动推断为U类型")
    @PutMapping("/put")
    public Result<?> put(String username) {
        return R.success(username);
    }

    @Log(title = "更新用户(补丁)", description = "测试PATCH请求自动推断为U类型")
    @PatchMapping("/patch")
    public Result<?> patch(String username) {
        return R.success(username);
    }

    @Log(title = "删除用户", description = "测试DELETE请求自动推断为D类型")
    @DeleteMapping("/delete")
    public Result<?> delete(String username) {
        return R.success(username);
    }

    @Log(title = "导入数据", operType = DbCrudEnum.C, bizType = "IMPORT", description = "测试显式指定操作类型和业务分类")
    @PostMapping("/import")
    public Result<?> importData(String data) {
        return R.success(data);
    }

    @Log(title = "失败操作", description = "测试操作状态推断为失败")
    @PostMapping("/fail")
    public Result<?> fail() {
        return R.errorPrompt("操作失败");
    }

    @Log(title = "异常操作", description = "测试异常时操作状态推断为失败")
    @PostMapping("/exception")
    public Result<?> exception() {
        throw new RuntimeException("模拟异常");
    }

    @PostMapping("/loginLog")
    public Result<?> recordLoginLog(String username, boolean success, String msg) {
        loginLogService.recordLogin(username, "127.0.0.1", success, msg, "{\"username\":\"" + username + "\"}");
        return R.success();
    }

    @GetMapping("/noLog")
    public Result<?> noLog(String username) {
        return R.success(username);
    }

}
