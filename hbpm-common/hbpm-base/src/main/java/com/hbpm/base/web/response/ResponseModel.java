package com.hbpm.base.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hbpm.base.web.error.ApiException;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 统一响应格式
 * @author HuangXiuQi
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ResponseModel", description = "统一响应格式")
public class ResponseModel {

    @Schema(name = "code", description = "状态码  200：成功  400：业务处理错误  401：未认证  403：无访问权限  500：服务端异常", defaultValue = "200")
    private Integer code = 200;

    @Schema(name = "message", description = "code返回400时服务端返回的错误信息", defaultValue = "success")
    private String message = "success";

    @Schema(name = "data", defaultValue = "实际数据")
    private Object data;

    protected ResponseModel() {}

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    /**
     * 生成成功响应
     * @return 响应实体
     */
    public static ResponseModel success() {
        ResponseModel model = new ResponseModel();
        model.code = 200;
        model.message = "success";
        return model;
    }

    /**
     * 生成成功响应
     * @param data 数据
     * @return 响应实体
     */
    public static ResponseModel success(Object data) {
        ResponseModel model = success();
        model.data = data;
        return model;
    }

    /**
     * 生成失败响应
     * @param code 错误码
     * @param message 错误信息
     * @return 响应实体
     */
    public static ResponseModel fail(int code, String message) {
        ResponseModel model = new ResponseModel();
        model.code = code;
        model.message = message;
        return model;
    }

    /**
     * 生成失败响应
     * @param code 错误码
     * @param message 错误信息
     * @param data 数据
     * @return 响应实体
     */
    public static ResponseModel fail(int code, String message, Object data) {
        ResponseModel model = fail(code, message);
        model.data = data;
        return model;
    }

    /**
     * 生成响应
     * @param code 错误码
     * @param message 错误信息
     * @param data 数据
     * @return 响应实体
     */
    public static ResponseModel of(int code, String message, Object data) {
        ResponseModel model = new ResponseModel();
        model.code = code;
        model.message = message;
        model.data = data;
        return model;
    }

    /**
     * 生成响应
     * @param exception 异常
     * @return 响应实体
     */
    public static ResponseModel of(ApiException exception) {
        ResponseModel model = new ResponseModel();
        model.code = 400;
        model.message = exception.getLocalizedMessage();
        model.data = exception.getData();
        return model;
    }
}
