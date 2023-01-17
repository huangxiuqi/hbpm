package com.hbpm.base.curd;

import com.hbpm.base.idworker.IdWorker;
import com.hbpm.base.utils.BaseEntityUtils;
import com.hbpm.base.web.error.ApiException;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huangxiuqi
 * @see TreeRelationEntity
 * @see TreeRelationDao
 */
public abstract class BaseTreeCurdServiceImpl<ID, ENTITY extends BaseTreeNodeEntity<ID>, DAO extends BaseCurdDao<ID, ENTITY>>
        implements BaseTreeCurdService<ID, ENTITY> {

    /**
     * 获取DAO
     *
     * @return DAO
     */
    protected abstract DAO getDao();

    protected abstract TreeRelationDao<ID> getTreeRelationDao();

    protected abstract String getTreeRelationTableName();

    /**
     * 获取IdWorker
     *
     * @return IdWorker
     */
    protected abstract IdWorker<ID> getIdWorker();

    @Override
    public Optional<ENTITY> findById(ID id) {
        return getDao().selectByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(ID id) {
        // 查询此节点的所有后代节点，包括其自身
        List<TreeRelationEntity<ID>> descendants = getTreeRelationDao()
                .selectDescendantsWithSelf(getTreeRelationTableName(), id);

        List<ID> dscIds = descendants.stream().map(TreeRelationEntity::getDscId).collect(Collectors.toList());
        if (dscIds.isEmpty()) {
            return 0;
        }

        // 删除节点关系
        getTreeRelationDao().deleteRelationByAncestorId(getTreeRelationTableName(), id);
        getTreeRelationDao().deleteRelationByDescendantIdIn(getTreeRelationTableName(), dscIds);

        // 删除当前节点和后代节点
        return getDao().batchDeleteByPrimaryKey(dscIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteByIds(Collection<ID> ids) {
        // 因为涉及节点关系维护，所以采取循环删除方式进行批量删除
        int ret = 0;
        for (ID id : ids) {
            ret += deleteById(id);
        }
        return ret;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(ENTITY entity) {
        int ret = getDao().insert(entity);

        // 自身插入关系表
        TreeRelationEntity<ID> relation = new TreeRelationEntity<>();
        relation.setId(getIdWorker().nextId());
        relation.setAncId(entity.getId());
        relation.setDscId(entity.getId());
        relation.setDis(0);
        getTreeRelationDao().insertRelation(getTreeRelationTableName(), relation);

        if (!BaseEntityUtils.isIdEmpty(entity.getParentId())) {
            return ret;
        }

        // 校验parentId
        getDao()
                .selectByPrimaryKey(entity.getParentId())
                .orElseThrow(() -> new ApiException("上级节点不存在"));

        // 构建所有祖先到本节点的关系
        List<TreeRelationEntity<ID>> parentAncestors = getTreeRelationDao()
                .selectAncestors(getTreeRelationTableName(), entity.getParentId());
        List<TreeRelationEntity<ID>> relations = new ArrayList<>();
        for (TreeRelationEntity<ID> ancestor : parentAncestors) {
            TreeRelationEntity<ID> r = new TreeRelationEntity<>();
            r.setId(getIdWorker().nextId());
            r.setAncId(ancestor.getAncId());
            r.setDscId(entity.getId());
            r.setDis(ancestor.getDis() + 1);
            relations.add(r);
        }
        if (relations.size() > 0) {
            getTreeRelationDao().batchInsertRelation(getTreeRelationTableName(), relations);
        }
        return ret;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(Collection<ENTITY> entityList) {
        // 因为涉及节点关系维护，所以采取循环插入方式进行批量插入
        int ret = 0;
        for (ENTITY entity : entityList) {
            ret += insert(entity);
        }
        return ret;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(ENTITY entity) {
        // 获取此节点的所有后代节点
        List<TreeRelationEntity<ID>> descendants = getTreeRelationDao()
                .selectDescendantsWithSelf(getTreeRelationTableName(), entity.getId());

        // 上级不能选择自身和自己的后代
        if (descendants.stream().anyMatch(i -> Objects.equals(i.getDscId(), entity.getParentId()))) {
            throw new ApiException("上级不能选择自身或自己的下级");
        }

        int ret = getDao().updateByPrimaryKey(entity);
        getTreeRelationDao().splitTreeNode(getTreeRelationTableName(), entity.getId());
        if (entity.getParentId() == null) {
            return ret;
        }

        // 获取父节点的所有祖先节点
        List<TreeRelationEntity<ID>> ancestors = getTreeRelationDao()
                .selectAncestors(getTreeRelationTableName(), entity.getParentId());
        // 求笛卡尔积，保存关系
        List<TreeRelationEntity<ID>> relations = cartesian(ancestors, descendants);
        getTreeRelationDao().batchInsertRelation(getTreeRelationTableName(), relations);
        return ret;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(ENTITY entity) {
        Optional<ENTITY> old = Optional.empty();
        if (entity.getId() != null) {
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

    /**
     * 获取笛卡尔积
     *
     * @param ancestors   祖先列表
     * @param descendants 后代列表
     * @return 笛卡尔积列表
     */
    private List<TreeRelationEntity<ID>> cartesian(List<TreeRelationEntity<ID>> ancestors, List<TreeRelationEntity<ID>> descendants) {
        List<TreeRelationEntity<ID>> result = new ArrayList<>(ancestors.size() * descendants.size());
        for (TreeRelationEntity<ID> descendant : descendants) {
            for (TreeRelationEntity<ID> ancestor : ancestors) {
                TreeRelationEntity<ID> relationEntity = new TreeRelationEntity<ID>();
                relationEntity.setId(getIdWorker().nextId());
                relationEntity.setAncId(ancestor.getAncId());
                relationEntity.setDscId(descendant.getDscId());
                relationEntity.setDis(ancestor.getDis() + descendant.getDis() + 1);
                result.add(relationEntity);
            }
        }
        return result;
    }
}
