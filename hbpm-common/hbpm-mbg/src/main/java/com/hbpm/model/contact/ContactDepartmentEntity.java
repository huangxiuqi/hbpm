package com.hbpm.model.contact;

import com.hbpm.base.curd.BaseEntity;

public class ContactDepartmentEntity implements BaseEntity<Long> {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long tenantId;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}