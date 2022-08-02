package org.rdlinux.ezmybatis.core.sqlstruct;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzParam;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Condition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.ConditionBuilder;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.GroupCondition;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.join.JoinType;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;

import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class Join implements SqlStruct {

    /**
     * 主表
     */
    private Table table;
    /**
     * 关联类型
     */
    private JoinType joinType;
    /**
     * 被join表
     */
    private Table joinTable;
    /**
     * 链表条件
     */
    private List<Condition> onConditions;
    /**
     * 关联表
     */
    private List<Join> joins;
    /**
     * 是否确认连表
     */
    private boolean sure;

    @Override
    public StringBuilder toSqlPart(StringBuilder sqlBuilder, Configuration configuration, EzParam<?> ezParam,
                                   MybatisParamHolder mybatisParamHolder) {
        return this.joinToSql(sqlBuilder, configuration, mybatisParamHolder);
    }

    protected StringBuilder joinToSql(StringBuilder sqlBuilder, Configuration configuration,
                                      MybatisParamHolder mybatisParamHolder) {
        if (!this.isSure()) {
            return sqlBuilder;
        }
        StringBuilder sonSql;
        if (this.joinType == JoinType.CrossJoin) {
            sonSql = new StringBuilder();
        } else {
            sonSql = Where.conditionsToSqlPart(new StringBuilder(), configuration, mybatisParamHolder,
                    this.getOnConditions());
            if (sonSql.length() == 0) {
                return sqlBuilder;
            }
        }
        sqlBuilder.append(this.joinType.toSqlStruct()).append(this.joinTable.toSqlStruct(configuration,
                mybatisParamHolder));
        if (this.joinType != JoinType.CrossJoin) {
            sqlBuilder.append(" ON ");
        }
        sqlBuilder.append(sonSql);
        if (this.getJoins() != null && !this.getJoins().isEmpty()) {
            for (Join join : this.joins) {
                sqlBuilder.append(join.joinToSql(new StringBuilder(), configuration, mybatisParamHolder));
            }
        }
        return sqlBuilder;
    }

    public static class JoinBuilder<Builder> extends ConditionBuilder<Builder,
            JoinBuilder<Builder>> {
        private Join join;

        public JoinBuilder(Builder builder, Join join) {
            super(builder, join.getOnConditions(), join.getTable(), join.getJoinTable());
            this.sonBuilder = this;
            this.join = join;
        }

        public JoinBuilder<JoinBuilder<Builder>> groupCondition(boolean sure, LogicalOperator logicalOperator) {
            GroupCondition condition = new GroupCondition(sure, new LinkedList<>(), logicalOperator);
            this.conditions.add(condition);
            Join newJoin = new Join();
            newJoin.setTable(this.join.getTable());
            newJoin.setJoinTable(this.join.getJoinTable());
            newJoin.setOnConditions(condition.getConditions());
            return new JoinBuilder<>(this, newJoin);
        }

        public JoinBuilder<JoinBuilder<Builder>> groupCondition(LogicalOperator logicalOperator) {
            return this.groupCondition(true, logicalOperator);
        }

        public JoinBuilder<JoinBuilder<Builder>> groupCondition() {
            return this.groupCondition(LogicalOperator.AND);
        }

        public JoinBuilder<JoinBuilder<Builder>> groupCondition(boolean sure) {
            return this.groupCondition(sure, LogicalOperator.AND);
        }

        public JoinBuilder<JoinBuilder<Builder>> join(boolean sure, JoinType joinType, Table joinTable) {
            if (this.join.getJoins() == null) {
                this.join.joins = new LinkedList<>();
            }
            Join newJoin = new Join();
            newJoin.setJoinType(joinType);
            newJoin.setTable(this.join.getJoinTable());
            newJoin.setJoinTable(joinTable);
            newJoin.setOnConditions(new LinkedList<>());
            newJoin.setSure(sure);
            this.join.joins.add(newJoin);
            return new Join.JoinBuilder<>(this, newJoin);
        }

        public JoinBuilder<JoinBuilder<Builder>> join(JoinType joinType, Table joinTable) {
            return this.join(true, joinType, joinTable);
        }

        public JoinBuilder<JoinBuilder<Builder>> join(Table joinTable) {
            return this.join(JoinType.InnerJoin, joinTable);
        }

        public JoinBuilder<JoinBuilder<Builder>> join(boolean sure, Table joinTable) {
            return this.join(sure, JoinType.InnerJoin, joinTable);
        }

        /**
         * 将被join表设置为条件构造表
         */
        public JoinBuilder<Builder> joinTableCondition() {
            this.table = this.join.getJoinTable();
            this.otherTable = this.join.getTable();
            return this;
        }

        /**
         * 将被主表设置为条件构造表
         */
        public JoinBuilder<Builder> masterTableCondition() {
            this.table = this.join.getTable();
            this.otherTable = this.join.getJoinTable();
            return this;
        }
    }
}
