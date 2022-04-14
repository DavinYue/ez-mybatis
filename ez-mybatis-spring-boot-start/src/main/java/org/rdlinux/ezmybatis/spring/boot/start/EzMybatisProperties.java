package org.rdlinux.ezmybatis.spring.boot.start;

import org.rdlinux.ezmybatis.constant.DbType;
import org.springframework.boot.context.properties.ConfigurationProperties;

//@ConfigurationProperties(prefix = MybatisProperties.MYBATIS_PREFIX)
@ConfigurationProperties(prefix = EzMybatisProperties.EZ_MYBATIS_PREFIX)
public class EzMybatisProperties {
    public static final String EZ_MYBATIS_PREFIX = "ez-mybatis";
    /**
     * 数据库类型
     */
    private DbType dbType;
    /**
     * 转义关键词
     */
    private boolean escapeKeyword = true;

    public DbType getDbType() {
        return this.dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    public boolean isEscapeKeyword() {
        return this.escapeKeyword;
    }

    public void setEscapeKeyword(boolean escapeKeyword) {
        this.escapeKeyword = escapeKeyword;
    }
}
