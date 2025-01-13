package com.mecash.multiCurrencyWalletApp.exceptions;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse<?>> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ExceptionResponse<?>> handleWalletNotFoundException(WalletNotFoundException ex, WebRequest request) {


        return getExceptionResponseResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse<?>> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionResponse<?>> handleNullPointerException(NullPointerException ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ExceptionResponse<?>> handleInsufficientFundException(InsufficientFundsException ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionProcessingException.class)
    public ResponseEntity<ExceptionResponse<?>> handleTxnProcessingException(TransactionProcessingException ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse<?>> handleNotFoundException(NotFoundException ex, WebRequest request) {

        return getExceptionResponseResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ErrorDetails apiError = new ErrorDetails(HttpStatus.BAD_REQUEST, errors.get(0), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    private ResponseEntity<ExceptionResponse<?>> getExceptionResponseResponseEntity(Exception ex, HttpStatus httpStatus) {

        var exceptionResponse = new ExceptionResponse<String>();
        String errorDescription = ex.getMessage();
        if (Objects.isNull(errorDescription)) errorDescription = ex.toString();
        exceptionResponse.setMessage(errorDescription);
        exceptionResponse.setStatus(httpStatus.value());
        exceptionResponse.setDateTimestamp(new Date());
        exceptionResponse.setData(null);
        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }
}
