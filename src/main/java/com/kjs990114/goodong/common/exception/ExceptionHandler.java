package com.kjs990114.goodong.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;
import java.util.List;


@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError) {
                        return ((FieldError) error).getField() + ": " + error.getDefaultMessage();
                    } else {
                        return error.getDefaultMessage() != null ? error.getDefaultMessage() : "알 수 없는 에러";
                    }
                }).toList();

        ErrorDTO errorDTO = new ErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                "입력 필드가 유효하지 않습니다",
                "1"
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ErrorException.class)
    public ResponseEntity<ErrorDTO> handleCustomErrorException(ErrorException e) {
        Error error = e.getErrorCode();
        ErrorDTO errorDTO = new ErrorDTO(
                error.getStatus(),
                error.getMsg(),
                error.getCode()
        );

        return ResponseEntity.status(error.getStatus()).body(errorDTO);
    }

    @Setter
    @Getter
    public static class ErrorDTO {
        private int status;
        private String code;
        private String msg;

        public ErrorDTO(int status, String msg, String code) {
            this.status = status;
            this.msg = msg;
            this.code = code;
        }
    }
}
