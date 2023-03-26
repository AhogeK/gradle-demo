package com.aochensoft.democommon.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * 自定义公共异常
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-02-27 22:59:13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AochenGlobalException extends RuntimeException {

    private final Integer status;
    private final String message;

    public AochenGlobalException() {
        super();
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = "内部服务器错误";
    }

    public AochenGlobalException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = message;
    }

    public AochenGlobalException(Integer status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public AochenGlobalException(HttpStatus status, String message) {
        super(message);
        this.status = status.value();
        this.message = message;
    }
}
