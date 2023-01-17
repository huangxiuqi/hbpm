package com.hbpm.base.curd;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 树形闭包表Dao
 *
 * @see com.hbpm.base.curd.TreeRelationEntity
 * @author HuangXiuQi
 */
@Mapper
public interface TreeRelationDao<ID> {

    /**
     * 关系表批量插入
     * @param tableName 表名
     * @param entityList 实体列表
     */
    @Insert({
            "<script>",
            "insert into ${tableName} (`id`, `anc_id`, `dsc_id`, `dis`) values ",
            "<foreach collection='entityList' item='item' index='index' separator=','>",
            "(#{item.id}, #{item.ancId}, #{item.dscId}, #{item.dis})",
            "</foreach>",
            "</script>"
    })
    void batchInsertRelation(@Param("tableName") String tableName, @Param("entityList") List<TreeRelationEntity<ID>> entityList);

    /**
     * 关系表单条插入
     * @param tableName 表名
     * @param entity 实体
     */
    @Insert("insert into ${tableName} (`id`, `anc_id`, `dsc_id`, `dis`) value (#{entity.id}, #{entity.ancId}, #{entity.dscId}, #{entity.dis})")
    void insertRelation(@Param("tableName") String tableName, @Param("entity") TreeRelationEntity<ID> entity);

    /**
     * 查询所有祖先节点
     * @param tableName 表名
     * @param id
     * @return
     */
    @Select("select * from ${tableName} where `dsc_id` = #{id}")
    @Results(id = "treeRelationMap", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "anc_id", property = "ancId"),
            @Result(column = "dsc_id", property = "dscId"),
            @Result(column = "dis", property = "dis", javaType = Integer.class)
    })
    List<TreeRelationEntity<ID>> selectAncestors(@Param("tableName") String tableName, @Param("id") ID id);

    /**
     * 查询所有后代节点（包含自身）
     * @param tableName 表名
     * @param id id
     * @return
     */
    @Select("select * from ${tableName} where `anc_id` = #{id}")
    @ResultMap("treeRelationMap")
    List<TreeRelationEntity<ID>> selectDescendantsWithSelf(@Param("tableName") String tableName, @Param("id") ID id);

    /**
     * 查询所有后代节点（不包含自身）
     * @param tableName 表名
     * @param id id
     * @return 后代节点列表
     */
    @Select("select * from ${tableName} where `anc_id` = #{id} and `dsc_id` != #{id}")
    @ResultMap("treeRelationMap")
    List<TreeRelationEntity<ID>> selectDescendantsWithoutSelf(@Param("tableName") String tableName, @Param("id") ID id);

    /**
     * 根据祖先删除所有节点
     * @param tableName 表名
     * @param ancId 祖先id
     */
    @Delete("delete from ${tableName} where `anc_id` = #{ancId}")
    void deleteRelationByAncestorId(@Param("tableName") String tableName, @Param("ancId") ID ancId);

    /**
     * 根据后代列表删除所有节点
     * @param tableName 表名
     * @param dscIds id列表
     */
    @Delete({
            "<script>",
            "delete from ${tableName} where `dsc_id` in (",
            "<foreach collection='dscIds' item='item' index='index' separator=','>",
            "#{item}",
            "</foreach>)",
            "</script>"
    })
    void deleteRelationByDescendantIdIn(@Param("tableName") String tableName, @Param("dscIds") List<ID> dscIds);

    /**
     * 分离指定节点
     * @param tableName 表名
     * @param id 节点id
     */
    @Delete({
            "delete from ${tableName} where ",
            "`dsc_id` IN (select d.`dsc_id` from (select * from ${tableName}) as d where d.`anc_id` = #{id}) and ",
            "anc_id IN (select a.`anc_id` from (select * from ${tableName}) as a where a.`dsc_id` = #{id} and a.`anc_id` <> a.`dsc_id`)"
    })
    void splitTreeNode(@Param("tableName") String tableName, @Param("id") ID id);
}
