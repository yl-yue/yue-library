package ai.yue.library.test.service.redis.lock.custom;

import ai.yue.library.data.redis.custom.DefaultLockKeyBuilder;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 自定义lock key builder
 */
//@Component
public class CustomLockKeyBuilder extends DefaultLockKeyBuilder implements Ordered {

    public CustomLockKeyBuilder(BeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
