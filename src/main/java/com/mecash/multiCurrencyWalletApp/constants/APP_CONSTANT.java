package com.mecash.multiCurrencyWalletApp.constants;


public interface APP_CONSTANT {
    String USER_SIGNUP_SUCCESS_MSG = "User Creation Successful";
    String USER_NOT_FOUND_MSG = "User with id : %s was not found.";

    String USER_NOT_AUTHENTICATED = "User is not authenticated.";
    String USER_EMAIL_NOT_FOUND = "User with email : %s not found.";
    String WALLET_NOT_FOUND = "User wallet not found";
    String UNKNOWN_TRANSACTION_TYPE = "Unknown transaction type";
    String TRANSACTION_NOT_FOUND = "Transaction with transaction reference : %s not found";
    String ACCOUNT_NUMBER_MISMATCH = "Account Number mismatch";
    String DEPOSIT_SUCCESS = "DEPOSIT transaction successful";
    String TRANSACTION_FETCH_SUCCESS = "Transactions successfully retrieved";
    String PROCESSING_FAILED = "An error occurred while processing the transaction. Message : %s";
    String INSUFFICIENT_FUNDS = "Insufficient funds";
    String WITHDRAWAL_SUCCESS = "WITHDRAWAL transaction successful";
    String INVALID_TRANSACTION_TYPE = "Invalid Transaction type";
    String TRANSFER_SUCCESS = "Transfer successful";
    String OPERATION_SUCCESS = "Successful";
    String TRANSFER_NOT_ALLOWED = "Transfer not allowed. You can not transfer to own wallet";
}
