package org.linuxprobe.crud.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.linuxprobe.crud.persistence.Sqlr;
import org.linuxprobe.crud.query.BaseQuery;

public interface UniversalMapper {
	/** 插入 */
	int insert(@Param("entity") Object entity, @Param("sqlr") Sqlr sqlr);

	/** 批量插入 */
	int batchInsert(@Param("entitys") List<Object> entitys, @Param("sqlr") Sqlr sqlr);

	/** 删除 */
	int deleteByPrimaryKey(@Param("entity") Object entity, @Param("sqlr") Sqlr sqlr);

	/** 批量删除 */
	long batchDeleteByPrimaryKey(@Param("entitys") List<Object> entitys, @Param("sqlr") Sqlr sqlr);

	/** 通用查询 */  
	public List<Map<String, Object>> universalSelect(BaseQuery param);

	/** 查询数量 */
	public long selectCount(BaseQuery param);

	/** 增量更新 */
	int localUpdate(@Param("entity") Object record, @Param("sqlr") Sqlr sqlr);

	/** 全量更新 */
	int globalUpdate(@Param("entity") Object record, @Param("sqlr") Sqlr sqlr);
}