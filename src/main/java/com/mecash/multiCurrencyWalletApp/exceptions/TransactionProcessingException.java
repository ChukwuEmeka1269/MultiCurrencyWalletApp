package com.mecash.multiCurrencyWalletApp.exceptions;

public class TransactionProcessingException extends RuntimeException {
    public TransactionProcessingException(String message) {
        super(message);
    }
}
