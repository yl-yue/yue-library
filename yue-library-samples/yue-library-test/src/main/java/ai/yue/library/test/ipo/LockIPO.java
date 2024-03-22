package ai.yue.library.test.ipo;

import lombok.Data;

@Data
public class LockIPO {

    private Long id;

    private String name;

    private String address;

    public LockIPO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
