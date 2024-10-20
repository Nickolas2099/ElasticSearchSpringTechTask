package com.example.searchTechTask.domain.response.exception;

import com.example.searchTechTask.domain.constant.Code;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@Builder
public class CommonException extends RuntimeException{

    private final Code code;
    private final String userMessage;
    private final String techMessage;
    private final HttpStatus httpStatus;
}
