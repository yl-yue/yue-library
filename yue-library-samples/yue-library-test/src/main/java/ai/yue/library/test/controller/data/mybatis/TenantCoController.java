package ai.yue.library.test.controller.data.mybatis;

import ai.yue.library.base.util.SpringUtils;
import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.data.mybatis.config.MybatisProperties;
import ai.yue.library.data.mybatis.interceptor.LogicDeleteInterceptor;
import ai.yue.library.data.mybatis.interceptor.TenantCoInterceptor;
import ai.yue.library.test.entity.TableExampleStandard;
import ai.yue.library.test.service.TableExampleStandardService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * 逻辑删除测试
 *
 * @author	ylyue
 * @since	2020年2月21日
 */
@RestController
@RequestMapping("/data/mybatis/tenantCo")
public class TenantCoController {

    @Resource
    TableExampleStandardService tableExampleStandardService;
    @Resource
    MybatisProperties mybatisProperties;

    @PostMapping("/testDefault")
    public Result<?> testDefault(TableExampleStandard tableExampleStandard) {
        tableExampleStandardService.insert(tableExampleStandard);
        tableExampleStandard.setFieldThree("testDefault");
        tableExampleStandardService.updateById(tableExampleStandard);
        return R.success(tableExampleStandardService.getById(tableExampleStandard.getId()));
    }

    @PostMapping("/testIgnore")
    public Result<?> testIgnore(TableExampleStandard tableExampleStandard) {
        Set<String> ignoreDsTenantCos = mybatisProperties.getIgnoreDsTenantCos();
        Set<String> ignoreDsLogicDeletes = mybatisProperties.getIgnoreDsLogicDeletes();
        ignoreDsTenantCos.add("mysql5");
        ignoreDsLogicDeletes.add("mysql5");

        tableExampleStandardService.insert(tableExampleStandard);
        tableExampleStandard.setFieldThree("testDefault");
        tableExampleStandardService.updateById(tableExampleStandard);

        TenantCoInterceptor bean = SpringUtils.getBean(TenantCoInterceptor.class);
        LogicDeleteInterceptor bean2 = SpringUtils.getBean(LogicDeleteInterceptor.class);
        System.out.println(bean);
        System.out.println(bean2);

        return R.success(tableExampleStandardService.getById(tableExampleStandard.getId()));
    }

}
