package ai.yue.library.base.ipo;

/**
 * 校验分组-默认定义
 * <p>校验分组需灵活使用，默认定义只可解决常规场景或作为示例参考</p>
 * <p>使用 {@link javax.validation.GroupSequence} 注解可指定分组校验顺序，同时还拥有短路能力</p>
 * <p>使用 {@link org.hibernate.validator.group.GroupSequenceProvider} 注解可动态指定分组校验顺序，解决多字段组合逻辑校验的痛点问题，但需自行实现 {@link org.hibernate.validator.spi.group.DefaultGroupSequenceProvider} 接口</p>
 *
 * @author ylyue
 * @since 2021/4/19
 */
public class ValidationGroups {

    // CRUD（create, read, update, delete）

    public interface Create {

    }

    public interface Read {

    }

    public interface Update {

    }

    public interface Delete {

    }

}
