package com.hbpm.base.curd;

/**
 * 树形结构闭包表实体
 *
 * <div>表结构定义</div>
 * <pre>
 * CREATE TABLE table_name  (
 *   `id` bigint(20) NOT NULL COMMENT '主键',
 *   `anc_id` bigint(20) NOT NULL COMMENT '祖先id',
 *   `dsc_id` bigint(20) NOT NULL COMMENT '后代id',
 *   `dis` int(11) NOT NULL COMMENT '与根节点距离',
 *   PRIMARY KEY (`id`) USING BTREE,
 *   INDEX `idx_anc_dis`(`anc_id`, `dis`) USING BTREE,
 *   INDEX `idx_dsc_dis`(`dsc_id`, `dis`) USING BTREE
 * ) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资源树形表' ROW_FORMAT = Dynamic;
 * </pre>
 *
 * @see TreeRelationDao
 * @author huangxiuqi
 */
public class TreeRelationEntity<ID> implements BaseEntity<ID> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private ID id;

    /**
     * 祖先id
     * 表字段名anc_id
     */
    private ID ancId;

    /**
     * 后代id
     * 表字段名dsc_id
     */
    private ID dscId;

    /**
     * 与根节点的距离
     * 表字段名dis
     */
    private Integer dis;

    @Override
    public ID getId() {
        return id;
    }

    @Override
    public void setId(ID id) {
        this.id = id;
    }

    public ID getAncId() {
        return ancId;
    }

    public void setAncId(ID ancId) {
        this.ancId = ancId;
    }

    public ID getDscId() {
        return dscId;
    }

    public void setDscId(ID dscId) {
        this.dscId = dscId;
    }

    public Integer getDis() {
        return dis;
    }

    public void setDis(Integer dis) {
        this.dis = dis;
    }

    @Override
    public String toString() {
        return "TreeRelationEntity{" +
                "id=" + id +
                ", ancId=" + ancId +
                ", dscId=" + dscId +
                ", dis=" + dis +
                '}';
    }
}
