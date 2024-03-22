package ai.yue.library.test.ipo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component("lockBeanIPO")
public class LockBeanIPO {

    private Long id= Long.valueOf(1);

    private String name="yue";

    private String address="yue";

}
