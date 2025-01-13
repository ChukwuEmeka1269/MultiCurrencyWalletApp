package com.mecash.multiCurrencyWalletApp.controllers;

import com.mecash.multiCurrencyWalletApp.constants.APP_CONSTANT;
import com.mecash.multiCurrencyWalletApp.models.enums.TransactionType;
import com.mecash.multiCurrencyWalletApp.models.requests.TransactionRequest;
import com.mecash.multiCurrencyWalletApp.models.responses.ApiResponse;
import com.mecash.multiCurrencyWalletApp.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/initiate")
    public ResponseEntity<ApiResponse<?>> initiateTransaction(@RequestBody @Valid TransactionRequest request, @RequestParam String transactionType) {

        if (TransactionType.DEPOSIT.toString().equalsIgnoreCase(transactionType))
            return new ResponseEntity<>(transactionService.processDepositTxn(request), HttpStatus.OK);
        if (TransactionType.WITHDRAWAL.toString().equalsIgnoreCase(transactionType))
            return new ResponseEntity<>(transactionService.processWithdrawalTxn(request), HttpStatus.OK);
        if (TransactionType.TRANSFER.toString().equalsIgnoreCase(transactionType))
            return new ResponseEntity<>(transactionService.processTransferTxn(request), HttpStatus.OK);
        ApiResponse<?> response = ApiResponse.builder()
                .message(APP_CONSTANT.INVALID_TRANSACTION_TYPE)
                .data(request)
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/wallet-tnx/{walletId}/fetch")
    public ResponseEntity<ApiResponse<?>> fetchWalletTxn(@PathVariable("walletId") Long id) {
        return new ResponseEntity<>(transactionService.fetchAllWalletTxn(id), HttpStatus.OK);
    }

    @GetMapping("/wallet-txn/{walletId}/balance")
    public ResponseEntity<ApiResponse<?>> viewBalance(@PathVariable("walletId") Long id) {
        return new ResponseEntity<>(transactionService.viewAccountBalance(id), HttpStatus.OK);
    }


}
