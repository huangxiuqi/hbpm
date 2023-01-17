package com.hbpm.base.curd;

import com.hbpm.base.idworker.IdWorker;
import com.hbpm.base.utils.BaseEntityUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author huangxiuqi
 */
public interface BaseCurdService<ID, ENTITY extends BaseEntity<ID>, DAO extends BaseCurdDao<ID, ENTITY>> {

    /**
     * 获取DAO
     * @return DAO
     */
    DAO getDao();

    /**
     * 获取IdWorker
     * @return IdWorker
     */
    IdWorker<ID> getIdWorker();

    /**
     * 根据实体id查询
     * @param id 实体id
     * @return 实体
     */
    default Optional<ENTITY> findById(ID id) {
        return getDao().selectByPrimaryKey(id);
    }

    /**
     * 查询所有实体
     * @return 实体列表
     */
    default List<ENTITY> findAll() {
        return getDao().selectAll();
    }

    /**
     * 根据实体id删除
     * @param id 实体id
     * @return 操作行数
     */
    default int deleteById(ID id) {
        return getDao().deleteByPrimaryKey(id);
    }

    /**
     * 根据实体id批量删除
     * @param ids id列表
     * @return 操作行数
     */
    @Transactional(rollbackFor = Exception.class)
    default int batchDeleteByIds(Collection<ID> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return getDao().batchDeleteByPrimaryKey(ids);
    }

    /**
     * 插入实体
     * @param entity 实体
     * @return 操作行数
     */
    default int insert(ENTITY entity) {
        return getDao().insert(entity);
    }

    /**
     * 批量插入实体
     * @param entityList 实体列表
     * @return 操作行数
     */
    @Transactional(rollbackFor = Exception.class)
    default int batchInsert(Collection<ENTITY> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return 0;
        }
        return getDao().batchInsert(entityList);
    }

    /**
     * 更新实体
     * @param entity 实体
     * @return 操作行数
     */
    default int update(ENTITY entity) {
        return getDao().updateByPrimaryKey(entity);
    }

    /**
     * 插入或更新实体
     * @param entity 实体
     * @return 操作行数
     */
    default int save(ENTITY entity) {
        Optional<ENTITY> old = Optional.empty();
        if (BaseEntityUtils.isIdEmpty(entity.getId())) {
            old = findById(entity.getId());
        } else {
            entity.setId(getIdWorker().nextId());
        }

        if (!old.isPresent()) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }
}
