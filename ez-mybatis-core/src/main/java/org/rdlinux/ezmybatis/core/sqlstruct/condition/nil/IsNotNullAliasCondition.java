package org.rdlinux.ezmybatis.core.sqlstruct.condition.nil;

import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

public class IsNotNullAliasCondition extends IsNullAliasCondition {
    public IsNotNullAliasCondition(LogicalOperator logicalOperator, String alias) {
        super(logicalOperator, alias);
        this.operator = Operator.isNotNull;
    }
}
