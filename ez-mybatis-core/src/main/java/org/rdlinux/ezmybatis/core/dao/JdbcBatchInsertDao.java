package org.rdlinux.ezmybatis.core.dao;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.TypeHandler;
import org.rdlinux.ezmybatis.core.EzJdbcBatchSql;
import org.rdlinux.ezmybatis.core.EzJdbcSqlParam;
import org.rdlinux.ezmybatis.core.EzMybatisContent;
import org.rdlinux.ezmybatis.core.interceptor.listener.EzMybatisInsertListener;
import org.rdlinux.ezmybatis.core.sqlgenerate.SqlGenerateFactory;
import org.rdlinux.ezmybatis.core.sqlstruct.table.Table;
import org.rdlinux.ezmybatis.utils.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * 使用jdbc批量插入
 */
public class JdbcBatchInsertDao {
    private static Log log;

    static {
        log = LogFactory.getLog(JdbcBatchInsertDao.class);
    }

    private SqlSession sqlSession;

    public JdbcBatchInsertDao(SqlSession sqlSession) {
        Assert.notNull(sqlSession, "sqlSession can not be null");
        this.sqlSession = sqlSession;
    }

    /**
     * 批量插入
     */
    public int batchInsert(Collection<?> models) {
        return this.batchInsertByTable(null, models);
    }

    /**
     * 批量插入, 指定表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public int batchInsertByTable(Table table, Collection<?> models) {
        Connection connection = this.sqlSession.getConnection();
        Configuration configuration = this.sqlSession.getConfiguration();
        List<EzMybatisInsertListener> listeners = EzMybatisContent.getInsertListeners(configuration);
        if (listeners != null) {
            for (EzMybatisInsertListener listener : listeners) {
                listener.onBatchInsert(models);
            }
        }
        long start = System.currentTimeMillis();
        EzJdbcBatchSql jdbcBatchSql = SqlGenerateFactory.getSqlGenerate(EzMybatisContent.getDbType(configuration))
                .getJdbcBatchInsertSql(configuration, table, models);
        long end = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("SQL construction takes: " + (end - start) + "ms");
        }
        try {
            PreparedStatement statement = connection.prepareStatement(jdbcBatchSql.getSql());
            start = System.currentTimeMillis();
            for (List<EzJdbcSqlParam> batchParam : jdbcBatchSql.getBatchParams()) {
                for (int i = 0; i < batchParam.size(); i++) {
                    EzJdbcSqlParam param = batchParam.get(i);
                    TypeHandler typeHandler = param.getTypeHandler();
                    typeHandler.setParameter(statement, i + 1, param.getValue(), param.getJdbcType());
                }
                statement.addBatch();
            }
            if (log.isDebugEnabled()) {
                end = System.currentTimeMillis();
                log.debug("SQL parameter setting takes: " + (end - start) + "ms");
            }
            start = System.currentTimeMillis();
            int[] intRets = statement.executeBatch();
            if (log.isDebugEnabled()) {
                end = System.currentTimeMillis();
                log.debug("SQL execution takes: " + (end - start) + "ms");
            }
            int ret = 0;
            for (int intRet : intRets) {
                if (intRet == -2) {
                    ret = -2;
                    break;
                }
                ret = ret + intRet;
            }
            if (log.isDebugEnabled()) {
                String msg = "==>  Preparing: " + jdbcBatchSql.getSql();
                msg = msg + "\n" + "==> Parameters: *";
                msg = msg + "\n" + "<==    Updates: " + ret;
                log.debug(msg);
            }
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
