package com.hbpm.mbg;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.List;

/**
 * @author huangxiuqi
 */
public class BatchDeleteXmlGenerator extends AbstractXmlElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {
        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();

        // 添加批量删除
        XmlElement batchDelete = new XmlElement("delete");
        batchDelete.addAttribute(new Attribute("id", "batchDeleteByPrimaryKey"));

        StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        sb.append(" where ");
        sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(primaryKeyColumns.get(0)));
        sb.append(" in ");

        XmlElement foreachEl = new XmlElement("foreach");
        foreachEl.addAttribute(new Attribute("collection", "ids"));
        foreachEl.addAttribute(new Attribute("item", MyBatis3FormattingUtilities.getEscapedColumnName(primaryKeyColumns.get(0))));
        foreachEl.addAttribute(new Attribute("index", "index"));
        foreachEl.addAttribute(new Attribute("open", "("));
        foreachEl.addAttribute(new Attribute("close", ")"));
        foreachEl.addAttribute(new Attribute("separator", ","));
        foreachEl.addElement(new TextElement(MyBatis3FormattingUtilities.getParameterClause(primaryKeyColumns.get(0))));

        batchDelete.addElement(new TextElement(sb.toString()));
        batchDelete.addElement(foreachEl);
        parentElement.addElement(batchDelete);
    }
}
