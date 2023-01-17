package com.hbpm.mbg;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author huangxiuqi
 */
public class CustomPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * Mapper接口添加基类
     * 返回值改为Optional
     *
     * @param interfaze
     *            the generated interface if any, may be null
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return
     */
    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        // 生成Mapper添加基础类
        JavaTypeResolver javaTypeResolver = new JavaTypeResolverDefaultImpl();
        FullyQualifiedJavaType calculateJavaType = javaTypeResolver
                .calculateJavaType(introspectedTable.getPrimaryKeyColumns().get(0));

        FullyQualifiedJavaType superInterfaceType = new FullyQualifiedJavaType(
                "BaseCurdDao<" +
                        calculateJavaType.getShortName() +
                        ", " +
                        introspectedTable.getBaseRecordType() +
                        ">"
        );
        FullyQualifiedJavaType baseMapperInstance = FullyQualifiedJavaTypeProxyFactory.getBaseMapperInstance();

        interfaze.addSuperInterface(superInterfaceType);
        interfaze.addImportedType(baseMapperInstance);

        List<Method> changeMethods = interfaze.getMethods().stream()
                .filter(method -> method.getName().endsWith("WithBLOBs")
                        || method.getReturnType().toString().endsWith("WithBLOBs")
                        || Arrays.toString(method.getParameters().toArray()).contains("WithBLOBs"))
                .collect(Collectors.toList());

        interfaze.getMethods().retainAll(changeMethods);

        if (changeMethods.isEmpty()) {
            interfaze.getImportedTypes().removeIf(javaType -> javaType.getFullyQualifiedName().equals("java.util.List")
                    || javaType.getFullyQualifiedName().equals("org.apache.ibatis.annotations.Param"));
        }

        return super.clientGenerated(interfaze, introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        AbstractXmlElementGenerator elementGenerator = new BatchDeleteXmlGenerator();
        elementGenerator.setContext(context);
        elementGenerator.setIntrospectedTable(introspectedTable);
        elementGenerator.addElements(document.getRootElement());

        elementGenerator = new BatchInsertXmlGenerator();
        elementGenerator.setContext(context);
        elementGenerator.setIntrospectedTable(introspectedTable);
        elementGenerator.addElements(document.getRootElement());
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    /**
     * 实体类添加基类
     *
     * @param topLevelClass
     *            the generated base record class
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        JavaTypeResolver javaTypeResolver = new JavaTypeResolverDefaultImpl();
        FullyQualifiedJavaType calculateJavaType = javaTypeResolver
                .calculateJavaType(introspectedTable.getPrimaryKeyColumns().get(0));

        FullyQualifiedJavaType superInterfaceType = new FullyQualifiedJavaType(
                "BaseEntity<" + calculateJavaType.getShortName() + ">");
        FullyQualifiedJavaType baseEntityInstance = FullyQualifiedJavaTypeProxyFactory.getBaseEntityInstance();
        if (Objects.equals(introspectedTable.getTableConfiguration().getProperties().get("isTree"), "true")) {
            superInterfaceType = new FullyQualifiedJavaType(
                    "BaseTreeNodeEntity<" + calculateJavaType.getShortName() + ">");
            baseEntityInstance = FullyQualifiedJavaTypeProxyFactory.getBaseTreeNodeEntityInstance();
        }

        topLevelClass.addSuperInterface(superInterfaceType);
        topLevelClass.addImportedType(baseEntityInstance);

        Field serialField = new Field("serialVersionUID", new FullyQualifiedJavaType("long"));
        serialField.setFinal(true);
        serialField.setStatic(true);
        serialField.setInitializationString("1L");
        serialField.setVisibility(JavaVisibility.PRIVATE);
        topLevelClass.getFields().add(0, serialField);

        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }
}
