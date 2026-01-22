package ai.yue.library.test.base.util;

import ai.yue.library.base.util.ListUtils;
import ai.yue.library.test.entity.DataAudit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ListUtilsTest {

    @SneakyThrows
    @Test
    public void test() {
        List<DataAudit> list = new ArrayList<>();
        list.add(new DataAudit(1L, 1L, 1L, "1", "1", "1", '1', LocalDate.now()));
        list.add(new DataAudit(2L, 2L, 2L, "2", "2", "2", '2', LocalDate.now()));
        list.add(new DataAudit(3L, 3L, 3L, "3", "3", "3", '3', LocalDate.now()));
        List<String> listT = ListUtils.toListT(list, DataAudit::getCellphone);
        System.out.println(listT);
        assert listT.get(0) != null;
    }

}
