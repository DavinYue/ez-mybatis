package ink.dvc.ezmybatis.core.interceptor.listener;

import java.util.List;

/**
 * 插入事件监听器
 */
public interface EzMybatisInsertListener {
    void onInsert(Object entity);

    void onBatchInsert(List<Object> entity);

    default int order() {
        return 1;
    }
}