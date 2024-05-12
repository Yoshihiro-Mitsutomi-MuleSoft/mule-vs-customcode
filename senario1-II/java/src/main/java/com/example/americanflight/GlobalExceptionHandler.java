package com.example.americanflight;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;

/**
 * GlobalExceptionHandler
 * 制約違反の際の例外処理クラス
 * デフォルトではEnum制約の際、HTTP Status 500で400で戻るように記述
 */
@ControllerAdvice
@jakarta.annotation.Nullable
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)

    // Exception handler for additional constraints for Enum
    public ResponseEntity<Object> handleConstraintViolatioEntity(ConstraintViolationException ex, WebRequest aRequest) {

        Object[] args = { ex.getConstraintViolations() };
        String defaultDetail = ex.getMessage();

        ProblemDetail body = createProblemDetail(ex, HttpStatus.BAD_REQUEST, defaultDetail, null, args, aRequest);

        return handleExceptionInternal(ex, body, null, HttpStatus.BAD_REQUEST, aRequest);
    }
}
