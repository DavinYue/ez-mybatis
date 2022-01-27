package org.rdlinux.ezmybatis.core.sqlgenerate;

import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.EzDelete;

import java.util.List;

public interface DeleteSqlGenerate {
    String getDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Class<?> ntClass, Object id);

    String getBatchDeleteByIdSql(Configuration configuration, MybatisParamHolder paramHolder, Class<?> ntClass,
                                 List<?> ids);

    String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, EzDelete delete);

    String getDeleteSql(Configuration configuration, MybatisParamHolder paramHolder, List<EzDelete> deletes);
}