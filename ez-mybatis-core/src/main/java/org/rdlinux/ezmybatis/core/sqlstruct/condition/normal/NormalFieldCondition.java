package org.rdlinux.ezmybatis.core.sqlstruct.condition.normal;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlstruct.SqlPart;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.LogicalOperator;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

/**
 * 普通条件
 */
@Getter
public class NormalFieldCondition extends NormalCondition implements SqlPart {
    private EntityTable table;
    private String field;

    public NormalFieldCondition(LogicalOperator logicalOperator, EntityTable table, String field, Operator operator,
                                Object value) {
        super(logicalOperator, operator, value);
        this.table = table;
        this.field = field;
    }

    @Override
    protected String getSqlField(Configuration configuration) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo etInfo = EzEntityClassInfoFactory.forClass(configuration, this.getTable().getEtType());
        String column = etInfo.getFieldInfo(this.getField()).getColumnName();
        return this.getTable().getAlias() + "." + keywordQM + column + keywordQM;
    }
}
