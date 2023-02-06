package org.rdlinux.ezmybatis.core.sqlstruct.condition.between;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;

/**
 * between 条件
 */
public abstract class BetweenCondition implements Condition, SqlPart {
    @Getter
    protected Operator operator = Operator.between;
    @Getter
    @Setter
    protected LogicalOperator logicalOperator;
    @Getter
    @Setter
    protected Object minValue;
    @Getter
    @Setter
    protected Object maxValue;

    public BetweenCondition(LogicalOperator logicalOperator, Object minValue, Object maxValue) {
        this.logicalOperator = logicalOperator;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    protected abstract String getSqlField(Configuration configuration);

    @Override
    public String toSqlPart(Configuration configuration, MybatisParamHolder mybatisParamHolder) {
        if (this.minValue == null || this.maxValue == null) {
            return "";
        }
        return " " + this.getSqlField(configuration) +
                " " + this.getOperator().getOperator() + " " +
                Condition.valueToSqlStruct(configuration, mybatisParamHolder, this.minValue) + " AND " +
                Condition.valueToSqlStruct(configuration, mybatisParamHolder, this.maxValue) + " ";
    }
}
