package com.aochensoft.democommon.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 异常响应体
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-02-15 12:27:33
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final int status;
    private final String message;
    // customizing timestamp serialization format
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;
    private String stackTrace;
    private List<ValidationError> errors;

    public ErrorResponse() {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = "内部服务器错误";
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String message) {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) {
            errors = new ArrayList<>();
        }
        errors.add(new ValidationError(field, message));
    }

    /**
     * <a href="https://www.baeldung.com/java-record-keyword">Java 14 Record Keyword</a>
     * same as
     * {@code
     *
     * @Getter
     * @Setter
     * @RequiredArgsConstructor private static class ValidationError {
     * private final String field;
     * private final String message;
     * }
     * }
     */
    private record ValidationError(String field, String message) {
    }
}
