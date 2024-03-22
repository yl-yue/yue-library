package ai.yue.library.data.redis.custom;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 分布式锁Key生成器
 */
public class DefaultLockKeyBuilder implements LockKeyBuilder {

    private static final ExpressionParser PARSER = new SpelExpressionParser();
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    private BeanResolver beanResolver;

    public DefaultLockKeyBuilder(BeanFactory beanFactory) {
        this.beanResolver = new BeanFactoryResolver(beanFactory);
    }

    @Override
    public String buildKey(MethodInvocation invocation, String[] definitionKeys) {
        if (definitionKeys.length > 1 || !"".equals(definitionKeys[0])) {
            return getSpelDefinitionKey(invocation, definitionKeys);
        }

        return "";
    }

    protected String getSpelDefinitionKey(MethodInvocation invocation, String[] definitionKeys) {
        Object rootObject = invocation.getThis();
        Method method = invocation.getMethod();
        Object[] arguments = invocation.getArguments();
        StandardEvaluationContext context = new MethodBasedEvaluationContext(rootObject, method, arguments, NAME_DISCOVERER);
        context.setBeanResolver(beanResolver);
        List<String> definitionKeyList = new ArrayList<>(definitionKeys.length);
        for (String definitionKey : definitionKeys) {
            if (definitionKey != null && !definitionKey.isEmpty()) {
                String key = PARSER.parseExpression(definitionKey).getValue(context, String.class);
                definitionKeyList.add(key);
            }
        }

        return StringUtils.collectionToDelimitedString(definitionKeyList, ".", "", "");
    }

}
