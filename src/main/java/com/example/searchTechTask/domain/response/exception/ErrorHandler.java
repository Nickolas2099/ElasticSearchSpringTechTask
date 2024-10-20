package com.example.searchTechTask.domain.response.exception;

import com.example.searchTechTask.domain.constant.Code;
import com.example.searchTechTask.domain.response.error.Error;
import com.example.searchTechTask.domain.response.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> handleCommonException(CommonException ex) {
        log.error("common error: {}", ex.toString());
        return new ResponseEntity<>(ErrorResponse.builder()
                .error(Error.builder().code(ex.getCode()).techMessage(ex.getUserMessage())
                        .build()).build(), ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedErrorException(Exception ex) {
        ex.printStackTrace();
        log.error("internal server error: {}", ex.toString());
        return new ResponseEntity<>(ErrorResponse.builder().error(Error.builder().code(Code.INTERNAL_SERVER_ERROR)
                .techMessage("Внутренняя ошибка сервиса").build()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
