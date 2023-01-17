package com.hbpm.contact.dto.resource;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author huangxiuqi
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResourceDTO {

    private Long id;

    private Long parentId;

    private String name;

    private Byte type;

    private String url;

    private List<ResourceDTO> children;

    private String icon;

    private Integer orderNumber;

    private Boolean show;

    private Boolean newWindow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ResourceDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ResourceDTO> children) {
        this.children = children;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Boolean getNewWindow() {
        return newWindow;
    }

    public void setNewWindow(Boolean newWindow) {
        this.newWindow = newWindow;
    }
}
