package com.hbpm.base.curd;

import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author huangxiuqi
 */
public interface BaseCurdDao<ID, ENTITY extends BaseEntity<ID>> {

    /**
     * 根据实体id查询
     * @param id 实体id
     * @return 实体
     */
    Optional<ENTITY> selectByPrimaryKey(@Param("id") ID id);

    /**
     * 查询所有实体
     * @return 实体列表
     */
    List<ENTITY> selectAll();

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
    int batchInsert(@Param("entityList") Collection<ENTITY> entityList);

    /**
     * 更新实体
     * @param entity 实体
     * @return 操作行数
     */
    int updateByPrimaryKey(ENTITY entity);

    /**
     * 根据实体id删除实体
     * @param id 实体id
     * @return 操作行数
     */
    int deleteByPrimaryKey(@Param("id") ID id);

    /**
     * 根据实体id批量删除
     * @param ids id列表
     * @return 操作行数
     */
    int batchDeleteByPrimaryKey(@Param("ids") Collection<ID> ids);
}
