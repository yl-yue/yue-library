package ai.yue.library.data.mybatis.provider;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.parser.ASTNodeAccessImpl;

@Data
@EqualsAndHashCode(callSuper = false)
public class AuditValue extends ASTNodeAccessImpl implements Expression {

    AuditDataProvider auditDataProvider;

    public AuditValue(AuditDataProvider auditDataProvider) {
        this.auditDataProvider = auditDataProvider;
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(new StringValue(auditDataProvider.getTenantCo()));
    }

    @Override
    public <T, S> T accept(ExpressionVisitor<T> expressionVisitor, S s) {
        String tenantCo = auditDataProvider.getTenantCo();
        return expressionVisitor.visit(new StringValue(tenantCo), tenantCo);
    }

    @Override
    public String toString() {
        return new StringValue(auditDataProvider.getTenantCo()).toString();
    }

}
