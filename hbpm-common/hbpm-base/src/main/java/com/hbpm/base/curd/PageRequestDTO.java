package com.hbpm.base.curd;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Min;

/**
 * @author huangxiuqi
 */
@Schema(name = "PageRequestDTO", description = "分页请求实体")
public class PageRequestDTO {

    @Min(value = 1, message = "页码最小为1")
    @Schema(name = "page", description = "页码，从1开始", example = "1")
    private Integer page;

    @Schema(name = "size", description = "每页条数", example = "10")
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageRequestDTO{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }
}
