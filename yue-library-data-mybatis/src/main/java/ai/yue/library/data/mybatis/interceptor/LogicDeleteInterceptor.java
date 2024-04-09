package ai.yue.library.data.mybatis.interceptor;

import ai.yue.library.data.mybatis.config.MybatisProperties;
import ai.yue.library.data.mybatis.constant.DbConstant;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * 逻辑删除拦截器
 *
 * @author yl-yue
 * @since 2023/2/20
 */
@Configuration
@ConditionalOnProperty(prefix = "yue.data.mybatis", name = "enable-logic-delete", havingValue = "true", matchIfMissing = true)
public class LogicDeleteInterceptor extends FieldInterceptor {

    public LogicDeleteInterceptor(MybatisProperties mybatisProperties) {
        this.interceptorName = "逻辑删除拦截器";
        this.ignoreTableList = mybatisProperties.getIgnoreTableLogicDeletes();
        this.classField = DbConstant.CLASS_DELETE_TIME;
        this.dbField = DbConstant.DB_DELETE_TIME;
        this.tenantIdExpression = new LongValue(0L);
    }

}
