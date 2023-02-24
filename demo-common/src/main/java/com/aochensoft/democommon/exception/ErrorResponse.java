package com.aochensoft.democommon.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final int status;
    private final String message;
    private String stackTrace;
    private List<ValidationError> errors;

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) {
            errors = new ArrayList<>();
        }
        errors.add(new ValidationError(field, message));
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    private record ValidationError(String field, String message) {
    }
}
