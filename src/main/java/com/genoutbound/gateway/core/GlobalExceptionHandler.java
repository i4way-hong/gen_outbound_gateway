package com.genoutbound.gateway.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import com.genoutbound.gateway.genesys.common.GenesysUnavailableException;
import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(GenesysUnavailableException.class)
    public ResponseEntity<ApiResponse<Void>> handleGenesysUnavailable(GenesysUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<Void>> handleJwtException(JwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("만료되었거나 유효하지 않은 토큰입니다."));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ApiResponse<Void>> handleValidation(Exception ex) {
        String message = "요청값이 올바르지 않습니다.";
        if (ex instanceof MethodArgumentNotValidException manve
                && manve.getBindingResult().getFieldError() != null) {
            message = manve.getBindingResult().getFieldError().getDefaultMessage();
        }
        if (ex instanceof BindException be && be.getBindingResult().getFieldError() != null) {
            message = be.getBindingResult().getFieldError().getDefaultMessage();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message));
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseEntity<Void> handleAsyncTimeout(AsyncRequestTimeoutException ex) {
        log.debug("Async 요청 타임아웃 발생: {}", ex.getMessage());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public ResponseEntity<Void> handleAsyncNotUsable(AsyncRequestNotUsableException ex) {
        log.debug("Async 요청 종료: {}", ex.getMessage());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseEntity<Void> handleMessageNotWritable(HttpMessageNotWritableException ex) {
        log.debug("응답 쓰기 실패: {}", ex.getMessage());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFound(NoResourceFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("알 수 없는 오류가 발생했습니다."));
    }
}
