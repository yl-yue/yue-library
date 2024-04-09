package ai.yue.library.test.controller.data.mybatis;

import ai.yue.library.base.view.Result;
import ai.yue.library.test.entity.TableIdAlias;
import ai.yue.library.test.service.TableIdAliasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data/mybatis/tableIdAlias")
public class TableIdAliasController {

	@Autowired
    TableIdAliasService tableIdAliasService;

    @PostMapping("/testIdAlias")
    public Result<?> testIdAlias(TableIdAlias tableIdAlias) {
        return tableIdAliasService.testIdAlias(tableIdAlias);
    }

}
