package org.rdlinux.ezmybatis.core.utils;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.Configuration;
import org.rdlinux.ezmybatis.core.constant.DbType;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DbTypeUtils {
    /**
     * 配置与数据库类型映射
     */
    private static final ConcurrentMap<Configuration, DbType> DB_TYPE_MAP = new ConcurrentHashMap<>();

    public static DbType getDbType(Configuration configuration) {
        DbType dbType = DB_TYPE_MAP.get(configuration);
        if (dbType == null) {
            synchronized ( configuration ) {
                dbType = DB_TYPE_MAP.get(configuration);
                if (dbType == null) {
                    DataSource dataSource = configuration.getEnvironment().getDataSource();
                    String driver;
                    if (PooledDataSource.class.isAssignableFrom(dataSource.getClass())) {
                        driver = ((PooledDataSource) dataSource).getDriver();
                    } else {
                        driver = "";
                    }
                    if (driver.contains("mysql")) {
                        dbType = DbType.MYSQL;
                    } else if (driver.contains("oracle")) {
                        dbType = DbType.ORACLE;
                    } else {
                        throw new RuntimeException("Unsupported db type");
                    }
                    DB_TYPE_MAP.put(configuration, dbType);
                }
            }
        }
        return dbType;
    }
}