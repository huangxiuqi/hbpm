package com.hbpm.base.web.error;

/**
 * 响应异常
 * @author huangxiuqi
 */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String message;

    private final Object data;

    public ApiException(String message) {
        this(message, null);
    }

    public ApiException(String message, Object data) {
        super(message);
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
