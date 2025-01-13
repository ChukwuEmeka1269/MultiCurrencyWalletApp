package com.mecash.multiCurrencyWalletApp.services;

import com.mecash.multiCurrencyWalletApp.models.requests.TransactionRequest;
import com.mecash.multiCurrencyWalletApp.models.responses.ApiResponse;

public interface TransactionService {

    ApiResponse<?> processDepositTxn(TransactionRequest request);

    ApiResponse<?> processWithdrawalTxn(TransactionRequest request);

    ApiResponse<?> processTransferTxn(TransactionRequest request);

    ApiResponse<?> fetchAllWalletTxn(Long walletId);

    ApiResponse<?> viewAccountBalance(Long walletId);
}
