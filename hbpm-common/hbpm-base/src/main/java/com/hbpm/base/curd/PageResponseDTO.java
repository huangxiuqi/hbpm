package com.hbpm.base.curd;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author huangxiuqi
 */
@Schema(name = "PageResponseDTO", description = "分页响应实体")
public class PageResponseDTO<T> {

    @Schema(name = "total", description = "数据总数", example = "100")
    private Long total;

    @Schema(name = "list", description = "数据列表", example = "[]")
    private List<T> list;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageResponseDTO{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
