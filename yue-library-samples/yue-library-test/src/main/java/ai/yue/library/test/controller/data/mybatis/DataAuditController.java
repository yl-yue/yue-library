package ai.yue.library.test.controller.data.mybatis;

import ai.yue.library.base.view.R;
import ai.yue.library.base.view.Result;
import ai.yue.library.test.entity.DataAudit;
import ai.yue.library.test.service.DataAuditService;
import ai.yue.library.web.util.ServletUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data/mybatis/dataAudit")
public class DataAuditController {

	@Autowired
    DataAuditService dataAuditService;

    @PostMapping("/insert")
    public Result<?> insert(DataAudit dataAudit) {
        boolean save = dataAuditService.save(dataAudit);
        return R.success(save);
    }

    @DeleteMapping("/deleteById")
    public Result<?> deleteById(long id) {
        boolean b = dataAuditService.removeById(id);
        return R.success(b);
    }

    @PutMapping("/updateById")
    public Result<?> updateById(DataAudit dataAudit) {
        boolean b = dataAuditService.updateById(dataAudit);
        return R.success(b);
    }

    @GetMapping("/page")
    public Result<?> page(DataAudit dataAudit) {
        PageHelper.startPage(ServletUtils.getRequest());
        List<DataAudit> list = dataAuditService.list(Wrappers.query(dataAudit));
        return R.success(PageInfo.of(list));
    }

}
