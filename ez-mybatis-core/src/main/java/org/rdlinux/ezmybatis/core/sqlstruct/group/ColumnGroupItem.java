package org.rdlinux.ezmybatis.core.sqlstruct.group;

import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.sqlgenerate.DbKeywordQMFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.DbTypeUtils;

@Getter
public class ColumnGroupItem extends AbstractGroupItem {
    protected String column;

    public ColumnGroupItem(Table table, String column) {
        super(table);
        this.column = column;
    }

    @Override
    public String toSqlStruct(Configuration configuration) {
        String keywordQM = DbKeywordQMFactory.getKeywordQM(DbTypeUtils.getDbType(configuration));
        return " " + this.table.getAlias() + "." + keywordQM + this.column + keywordQM + " ";
    }
}