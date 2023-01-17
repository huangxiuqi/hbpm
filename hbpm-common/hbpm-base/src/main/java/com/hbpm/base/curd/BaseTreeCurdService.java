package com.hbpm.base.curd;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

/**
 * @see com.hbpm.base.curd.TreeRelationEntity
 * @see TreeRelationDao
 * @author huangxiuqi
 */
public interface BaseTreeCurdService<ID, ENTITY extends BaseTreeNodeEntity<ID>> {

    /**
     * 根据实体id查询
     * @param id 实体id
     * @return 实体
     */
    Optional<ENTITY> findById(ID id);

    /**
     * 根据实体id删除
     * @param id 实体id
     * @return 操作行数
     */
    int deleteById(ID id);

    /**
     * 根据实体id批量删除
     * @param ids id列表
     * @return 操作行数
     */
    @Transactional(rollbackFor = Exception.class)
    int batchDeleteByIds(Collection<ID> ids);

    /**
     * 插入实体
     * @param entity 实体
     * @return 操作行数
     */
    int insert(ENTITY entity);

    /**
     * 批量插入实体
     * @param entityList 实体列表
     * @return 操作行数
     */
    @Transactional(rollbackFor = Exception.class)
    int batchInsert(Collection<ENTITY> entityList);

    /**
     * 更新实体
     * @param entity 实体
     * @return 操作行数
     */
    int update(ENTITY entity);

    /**
     * 插入或更新实体
     * @param entity 实体
     * @return 操作行数
     */
    int save(ENTITY entity);
}
