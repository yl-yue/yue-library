package ai.yue.library.test.controller.data.mybatis;

import ai.yue.library.base.view.Result;
import ai.yue.library.test.service.LogicDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 逻辑删除测试
 *
 * @author	ylyue
 * @since	2020年2月21日
 */
@RestController
@RequestMapping("/data/mybatis/logicDelete")
public class LogicDeleteController {

	@Autowired
    LogicDeleteService logicDeleteService;

    @PostMapping("/testDefault")
    public Result<?> testDefault() {
        return logicDeleteService.testDefault();
    }

    @GetMapping("/getLogicDelete")
    public Result<?> getLogicDelete() {
        return logicDeleteService.getLogicDelete();
    }

    @GetMapping("/listLogicDelete")
    public Result<?> listLogicDelete() {
        return logicDeleteService.listLogicDelete();
    }

    @GetMapping("/listLogicDeleteData")
    public Result<?> listLogicDeleteData() {
        return logicDeleteService.listLogicDeleteData();
    }

    @GetMapping("/queryLogicDelete1")
    public Result<?> queryLogicDelete1() {
        return logicDeleteService.queryLogicDelete1();
    }

    @GetMapping("/queryLogicDelete3")
    public Result<?> queryLogicDelete3() {
        return logicDeleteService.queryLogicDelete3();
    }

    @GetMapping("/queryLogicDeleteAll")
    public Result<?> queryLogicDeleteAll() {
        return logicDeleteService.queryLogicDeleteAll();
    }

    @PutMapping("/updateLogicDelete")
    public Result<?> updateLogicDelete() {
        return logicDeleteService.updateLogicDelete();
    }

    @DeleteMapping("/delLogicDelete")
    public Result<?> delLogicDelete() {
        return logicDeleteService.delLogicDelete();
    }

}
