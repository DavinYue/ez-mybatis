package org.rdlinux.dm;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.linuxprobe.luava.json.JacksonUtils;
import org.rdlinux.ezmybatis.core.EzQuery;
import org.rdlinux.ezmybatis.core.mapper.EzMapper;
import org.rdlinux.ezmybatis.core.sqlstruct.condition.Operator;
import org.rdlinux.ezmybatis.core.sqlstruct.table.DbTable;
import org.rdlinux.ezmybatis.core.sqlstruct.table.EntityTable;
import org.rdlinux.ezmybatis.java.entity.User;
import org.rdlinux.ezmybatis.java.mapper.UserMapper;
import org.rdlinux.ezmybatis.utils.StringHashMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Log4j2
public class DmSelectTest extends DmBaseTest {
    @Test
    public void selectById() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = sqlSession.getMapper(UserMapper.class).selectById("1");
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectByIds() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<String> ids = new LinkedList<>();
        ids.add("980e1f193035494198f90d24e01d6706");
        ids.add("1s");
        List<User> users = sqlSession.getMapper(UserMapper.class).selectByIds(ids);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void selectBySql() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        List<User> users = sqlSession.getMapper(UserMapper.class).selectBySql("select * from \"user\"", new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void selectMapBySql() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        EzMapper ezMapper = sqlSession.getMapper(EzMapper.class);
        List<Map<String, Object>> maps = ezMapper.selectMapBySql("SELECT banner as 版本信息 FROM v$version",
                new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(maps));
    }

    @Test
    public void queryTest() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        EntityTable userTable = EntityTable.of(User.class);
        EzQuery<User> query = EzQuery.builder(User.class).from(userTable)
                .select().addField("name").done()
                .groupBy().addField("name").done()
                .page(1, 2)
                .build();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.query(query);
        log.info(JacksonUtils.toJsonString(users));
        int i = userMapper.queryCount(query);
        log.info("总数" + i);
    }

    @Test
    public void groupTest() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addField("name").done()
                .where().addColumnCondition("name", Operator.gt, 1).done()
                //.groupBy().add("name").done()
                //.having().conditions().add("name", Operator.more, 1).done().done()
                //.orderBy().add("name").done()
                .page(2, 5)
                .build();
        List<User> users = sqlSession.getMapper(UserMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(users));
        int i = sqlSession.getMapper(UserMapper.class).queryCount(query);
        System.out.println("总数" + i);
    }

    @Test
    public void pageTest() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        DbTable table = DbTable.of("EZ_USER");
        String orderColumn = "AGE";
        EzQuery<StringHashMap> query = EzQuery.builder(StringHashMap.class).from(table)
                .page(1, 5)
                .build();
        EzMapper mapper = sqlSession.getMapper(EzMapper.class);
        List<StringHashMap> data = mapper.query(query);
        System.out.println(JacksonUtils.toJsonString(data));
        int i = mapper.queryCount(query);
        System.out.println("总数" + i);

        query = EzQuery.builder(StringHashMap.class).from(table)
                .page(2, 5)
                .build();
        data = mapper.query(query);
        System.out.println(JacksonUtils.toJsonString(data));
        sqlSession.clearCache();
        i = mapper.queryCount(query);
        System.out.println("总数" + i);

        query = EzQuery.builder(StringHashMap.class).from(table)
                .orderBy()
                .addColumn(orderColumn)
                .done()
                .page(1, 5)
                .build();
        data = mapper.query(query);
        System.out.println(JacksonUtils.toJsonString(data));
        sqlSession.clearCache();
        i = mapper.queryCount(query);
        System.out.println("总数" + i);

        query = EzQuery.builder(StringHashMap.class).from(table)
                .orderBy()
                .addColumn(orderColumn)
                .done()
                .page(2, 5)
                .build();
        data = mapper.query(query);
        System.out.println(JacksonUtils.toJsonString(data));
        sqlSession.clearCache();
        i = mapper.queryCount(query);
        System.out.println("总数" + i);
    }

    @Test
    public void normalQuery() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done()
                .build();
        List<User> users = sqlSession.getMapper(EzMapper.class).query(query);
        System.out.println(JacksonUtils.toJsonString(users));
    }

    @Test
    public void normalQueryOne() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        EzQuery<User> query = EzQuery.builder(User.class).from(EntityTable.of(User.class))
                .select().addAll().done().page(1, 1)
                .build();
        User user = sqlSession.getMapper(EzMapper.class).queryOne(query);
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void normalQueryCount() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        EzQuery<Integer> query = EzQuery.builder(Integer.class).from(EntityTable.of(User.class))
                .select().addFieldCount("id", "idc").done().page(1, 1)
                .build();
        int count = sqlSession.getMapper(EzMapper.class).queryOne(query);
        System.out.println(JacksonUtils.toJsonString(count));
    }

    @Test
    public void selectOneBySql() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        User user = sqlSession.getMapper(UserMapper.class).selectOneBySql("select * from \"user\" " +
                "where id = '2c50ee58773f468c82013f73c08e7bc8'", new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(user));
    }

    @Test
    public void selectOneMapBySql() {
        SqlSession sqlSession = DmBaseTest.sqlSessionFactory.openSession();
        Map<String, Object> user = sqlSession.getMapper(EzMapper.class).selectOneMapBySql(
                "select * from \"user\" " +
                        "where id = '1s'", new HashMap<>());
        System.out.println(JacksonUtils.toJsonString(user));
    }
}
