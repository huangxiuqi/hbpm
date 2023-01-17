package com.hbpm.base.web.error;

import com.hbpm.base.web.response.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huangxiuqi
 */
@ControllerAdvice(basePackages = {"com.hbpm"})
public class ServletErrorHandler {

    /**
     * 主动抛出异常
     *
     * @param httpServletRequest
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleException(
            HttpServletRequest httpServletRequest,
            ApiException e
    ) {
        return new ResponseEntity<>(ResponseModel.of(e), HttpStatus.BAD_REQUEST);
    }
}
