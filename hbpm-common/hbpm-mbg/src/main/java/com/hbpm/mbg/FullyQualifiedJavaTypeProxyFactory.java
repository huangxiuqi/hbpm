package com.hbpm.mbg;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

/**
 * @author huangxiuqi
 */
public class FullyQualifiedJavaTypeProxyFactory extends FullyQualifiedJavaType {

    private static final FullyQualifiedJavaType PAGE_INFO_INSTANCE = new FullyQualifiedJavaType("cn.xxx.core.base.model.PageInfo");
    private static final FullyQualifiedJavaType BASE_EXAMPLE_INSTANCE = new FullyQualifiedJavaType("cn.xxx.core.base.model.BaseExample");
    private static final FullyQualifiedJavaType BASE_MAPPER_INSTANCE = new FullyQualifiedJavaType("com.hbpm.base.curd.BaseCurdDao");
    private static final FullyQualifiedJavaType BASE_ENTITY_INSTANCE = new FullyQualifiedJavaType("com.hbpm.base.curd.BaseEntity");
    private static final FullyQualifiedJavaType BASE_TREE_NODE_ENTITY_INSTANCE = new FullyQualifiedJavaType("com.hbpm.base.curd.BaseTreeNodeEntity");
    private static final FullyQualifiedJavaType BASE_SERVICE_INSTANCE = new FullyQualifiedJavaType("cn.xxx.core.base.service.BaseService");
    private static final FullyQualifiedJavaType BASE_SERVICE_IMPL_INSTANCE = new FullyQualifiedJavaType("cn.xxx.core.base.service.impl.BaseServiceImpl");

    public FullyQualifiedJavaTypeProxyFactory(String fullTypeSpecification) {
        super(fullTypeSpecification);
    }

    public static FullyQualifiedJavaType getPageInfoInstanceInstance() {
        return PAGE_INFO_INSTANCE;
    }

    public static FullyQualifiedJavaType getBaseExampleInstance() {
        return BASE_EXAMPLE_INSTANCE;
    }

    public static FullyQualifiedJavaType getBaseMapperInstance() {
        return BASE_MAPPER_INSTANCE;
    }

    public static FullyQualifiedJavaType getBaseEntityInstance() {
        return BASE_ENTITY_INSTANCE;
    }

    public static FullyQualifiedJavaType getBaseTreeNodeEntityInstance() {
        return BASE_TREE_NODE_ENTITY_INSTANCE;
    }

    public static FullyQualifiedJavaType getBaseServiceInstance() {
        return BASE_SERVICE_INSTANCE;
    }

    public static FullyQualifiedJavaType getBaseServiceImplInstance() {
        return BASE_SERVICE_IMPL_INSTANCE;
    }
}
