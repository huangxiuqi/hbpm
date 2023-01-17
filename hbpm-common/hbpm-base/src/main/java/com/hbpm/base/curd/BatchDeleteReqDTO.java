package com.hbpm.base.curd;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author huangxiuqi
 */
@Schema(name = "BatchDeleteReqDTO", description = "批量删除实体")
public class BatchDeleteReqDTO<ID> {

    @NotEmpty(message = "id列表不能为空")
    @Schema(name = "ids", description = "实体Id列表", defaultValue = "[]")
    private List<ID> ids;

    public List<ID> getIds() {
        return ids;
    }

    public void setIds(List<ID> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "BatchDeleteReqDTO{" +
                "ids=" + ids +
                '}';
    }
}
