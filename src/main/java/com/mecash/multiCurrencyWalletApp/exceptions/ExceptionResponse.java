package com.mecash.multiCurrencyWalletApp.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class ExceptionResponse<T> {
    private int status;
    private String message;
    private T data;
    private Date DateTimestamp;

}