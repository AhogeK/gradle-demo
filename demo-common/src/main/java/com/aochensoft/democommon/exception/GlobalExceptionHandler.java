package com.aochensoft.democommon.exception;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

/**
 * 公共异常控制
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-02-15 12:03:50
 */
@ControllerAdvice
@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String TRACE = "trace";

    @Value("${reflector.trace:false}")
    private boolean printStackTrace;

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "参数校验失败，请确认错误的字段。");
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    /**
     * 未受查异常拦截
     *
     * @param exception 未知异常
     * @param request   web 请求
     * @return 失败响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {
        log.error("服务器未知异常", exception);
        return buildErrorResponse(exception, "服务器未知异常", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * 异常响应构造函数
     *
     * @param exception  异常
     * @param httpStatus http 状态码
     * @param request    web 请求
     * @return 公共异常响应体
     */
    private ResponseEntity<Object> buildErrorResponse(Exception exception,
                                                      HttpStatusCode httpStatus,
                                                      WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    /**
     * 异常响应构造函数
     *
     * @param exception  异常
     * @param message    异常信息
     * @param httpStatus http 状态码
     * @param request    web 请求
     * @return 公共异常响应体
     */
    private ResponseEntity<Object> buildErrorResponse(Exception exception,
                                                      String message,
                                                      HttpStatusCode httpStatus,
                                                      WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        if (printStackTrace && isTraceOn(request)) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * 如果请求带trace，失败返回将携带异常栈
     *
     * @param request web 请求对象
     * @return 是否携带异常栈
     */
    private boolean isTraceOn(WebRequest request) {
        String[] value = request.getParameterValues(TRACE);
        return Objects.nonNull(value)
                && value.length > 0
                && value[0].contentEquals("true");
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, @Nullable Object body,
                                                             @NonNull HttpHeaders headers,
                                                             @NonNull HttpStatusCode statusCode,
                                                             @NonNull WebRequest request) {
        return buildErrorResponse(ex, statusCode, request);
    }
}
