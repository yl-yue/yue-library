package ai.yue.library.test.controller.data.mybatis;

import ai.yue.library.base.view.Result;
import ai.yue.library.test.service.LogicDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @DeleteMapping("/testDefault")
    public Result<?> testDefault() {
        return logicDeleteService.testDefault();
    }

    @DeleteMapping("/getLogicDelete")
    public Result<?> getLogicDelete() {
        return logicDeleteService.getLogicDelete();
    }

    @DeleteMapping("/listLogicDelete")
    public Result<?> listLogicDelete() {
        return logicDeleteService.listLogicDelete();
    }

    @DeleteMapping("/updateLogicDelete")
    public Result<?> updateLogicDelete() {
        return logicDeleteService.updateLogicDelete();
    }

    @DeleteMapping("/delLogicDelete")
    public Result<?> delLogicDelete() {
        return logicDeleteService.delLogicDelete();
    }

}
