package org.rdlinux.ezmybatis.core.sqlstruct.converter.mysql;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.constant.DbType;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.classinfo.EzEntityClassInfoFactory;
import org.rdlinux.ezmybatis.core.classinfo.entityinfo.EntityClassInfo;
import org.rdlinux.ezmybatis.core.sqlgenerate.MybatisParamHolder;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.AbstractConverter;
import org.rdlinux.ezmybatis.core.sqlstruct.converter.Converter;
import org.rdlinux.ezmybatis.core.sqlstruct.formula.FieldFormulaElement;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;

public class MySqlFieldFormulaElementConverter extends AbstractConverter<FieldFormulaElement>
        implements Converter<FieldFormulaElement> {
    private static volatile MySqlFieldFormulaElementConverter instance;

    protected MySqlFieldFormulaElementConverter() {
    }

    public static MySqlFieldFormulaElementConverter getInstance() {
        if (instance == null) {
            synchronized (MySqlFieldFormulaElementConverter.class) {
                if (instance == null) {
                    instance = new MySqlFieldFormulaElementConverter();
                }
            }
        }
        return instance;
    }

    @Override
    protected StringBuilder doBuildSql(Type type, StringBuilder sqlBuilder, Configuration configuration,
                                       FieldFormulaElement ojb,
                                       MybatisParamHolder mybatisParamHolder) {
        String keywordQM = EzMybatisContent.getKeywordQM(configuration);
        EntityClassInfo entityClassInfo = EzEntityClassInfoFactory.forClass(configuration,
                ((EntityTable) ojb.getTable()).getEtType());
        String columnName = entityClassInfo.getFieldInfo(ojb.getFiled()).getColumnName();
        sqlBuilder.append(ojb.getTable().getAlias()).append(".").append(keywordQM).append(columnName).append(keywordQM);
        return sqlBuilder;
    }

    @Override
    public DbType getSupportDbType() {
        return DbType.MYSQL;
    }
}