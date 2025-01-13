package com.mecash.multiCurrencyWalletApp.services.impl;

import com.mecash.multiCurrencyWalletApp.constants.APP_CONSTANT;
import com.mecash.multiCurrencyWalletApp.exceptions.InsufficientFundsException;
import com.mecash.multiCurrencyWalletApp.exceptions.NotFoundException;
import com.mecash.multiCurrencyWalletApp.exceptions.TransactionProcessingException;
import com.mecash.multiCurrencyWalletApp.exceptions.WalletNotFoundException;
import com.mecash.multiCurrencyWalletApp.models.AppUser;
import com.mecash.multiCurrencyWalletApp.models.Transaction;
import com.mecash.multiCurrencyWalletApp.models.Wallet;
import com.mecash.multiCurrencyWalletApp.models.dto.TransactionHistory;
import com.mecash.multiCurrencyWalletApp.models.enums.Currency;
import com.mecash.multiCurrencyWalletApp.models.enums.Status;
import com.mecash.multiCurrencyWalletApp.models.enums.TransactionChannel;
import com.mecash.multiCurrencyWalletApp.models.enums.TransactionType;
import com.mecash.multiCurrencyWalletApp.models.requests.TransactionRequest;
import com.mecash.multiCurrencyWalletApp.models.responses.ApiResponse;
import com.mecash.multiCurrencyWalletApp.models.responses.TransactionResponse;
import com.mecash.multiCurrencyWalletApp.models.responses.ViewBalanceResponse;
import com.mecash.multiCurrencyWalletApp.repository.TransactionRepository;
import com.mecash.multiCurrencyWalletApp.repository.WalletRepository;
import com.mecash.multiCurrencyWalletApp.services.TransactionService;
import com.mecash.multiCurrencyWalletApp.utils.AppUtil;
import com.mecash.multiCurrencyWalletApp.utils.CurrencyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final WalletRepository walletRepository;
    private final Lock balanceLock = new ReentrantLock();

    private final TransactionRepository transactionRepository;

    @Transactional
    @Override
    public ApiResponse<?> processDepositTxn(TransactionRequest request) {


        AppUser loggedInUser = AppUtil.getCurrentlyLoggedInUser();
        String receiverAccount = request.getReceiverAccount();

        Wallet wallet = walletRepository.findByUser(loggedInUser).orElseThrow(() -> new WalletNotFoundException(APP_CONSTANT.WALLET_NOT_FOUND));

        if (!wallet.getAccountNumber().equals(receiverAccount)) {
            throw new IllegalArgumentException(APP_CONSTANT.ACCOUNT_NUMBER_MISMATCH);
        }

        createTransaction(request, wallet);

        BigDecimal transactionAmount = request.getTransactionAmount();
        String sendingCurrencyStr = request.getTxnCurrency();
        Currency txnCurrency = Currency.valueOf(sendingCurrencyStr);
        Currency walletCurrency = wallet.getCurrency();

        if (!walletCurrency.equals(txnCurrency)) {
            transactionAmount = CurrencyUtil.convert(txnCurrency, walletCurrency, transactionAmount);
        }

        // Locking the balance update for thread safety
        balanceLock.lock();
        try {
            BigDecimal updatedBalance = wallet.getBalance().add(transactionAmount);
            wallet.setBalance(updatedBalance);
            walletRepository.save(wallet);
        } finally {
            balanceLock.unlock();
        }

        Transaction transaction = updateTransactionStatus(request.getTransactionReferenceId(), Status.COMPLETED);


        TransactionResponse transactionResponse = TransactionResponse.builder().user(AppUser.toDto(loggedInUser)).wallet(Wallet.toDto(wallet)).status(transaction.getTnxStatus().toString()).build();

        return ApiResponse.builder().data(transactionResponse).status(HttpStatus.OK).message(APP_CONSTANT.DEPOSIT_SUCCESS).build();
    }

    @Transactional
    @Override
    public ApiResponse<?> processWithdrawalTxn(TransactionRequest request) {
        AppUser loggedInUser = AppUtil.getCurrentlyLoggedInUser();

        // Retrieve the wallet associated with the logged-in user
        Wallet wallet = walletRepository.findByUser(loggedInUser)
                .orElseThrow(() -> new WalletNotFoundException(APP_CONSTANT.WALLET_NOT_FOUND));


        createTransaction(request, wallet);


        BigDecimal transactionAmount = request.getTransactionAmount();
        String txnCurrencyStr = request.getTxnCurrency();
        Currency txnCurrency = Currency.valueOf(txnCurrencyStr);
        Currency walletCurrency = wallet.getCurrency();

        // Convert currency if necessary
        if (!walletCurrency.equals(txnCurrency)) {
            transactionAmount = CurrencyUtil.convert(txnCurrency, walletCurrency, transactionAmount);
        }

        // Lock the balance update to ensure thread safety
        balanceLock.lock();
        try {
            // Check if the wallet has sufficient balance
            if (wallet.getBalance().compareTo(transactionAmount) < 0) {
                throw new InsufficientFundsException(APP_CONSTANT.INSUFFICIENT_FUNDS);
            }

            // Deduct the transaction amount from the wallet's balance
            BigDecimal updatedBalance = wallet.getBalance().subtract(transactionAmount);
            wallet.setBalance(updatedBalance);

            walletRepository.save(wallet);
        } finally {
            balanceLock.unlock();
        }

        // Update the transaction status to COMPLETED
        Transaction transaction = updateTransactionStatus(request.getTransactionReferenceId(), Status.COMPLETED);

        TransactionResponse transactionResponse = TransactionResponse
                .builder()
                .user(AppUser.toDto(loggedInUser))
                .wallet(Wallet.toDto(wallet))
                .status(transaction.getTnxStatus().toString())
                .build();

        return ApiResponse
                .builder()
                .data(transactionResponse)
                .status(HttpStatus.OK)
                .message(APP_CONSTANT.WITHDRAWAL_SUCCESS)
                .build();
    }


    @Transactional
    @Override
    public ApiResponse<?> processTransferTxn(TransactionRequest request) {
        AppUser loggedInUser = AppUtil.getCurrentlyLoggedInUser();

        // Retrieve the wallet associated with the logged-in user
        Wallet senderWallet = walletRepository.findByUser(loggedInUser)
                .orElseThrow(() -> new WalletNotFoundException(APP_CONSTANT.WALLET_NOT_FOUND));


        BigDecimal transactionAmount = request.getTransactionAmount();
        String txnCurrencyStr = request.getTxnCurrency();
        String senderAccount = request.getSenderAccount();
        String receiverAccount = request.getReceiverAccount();
        Currency txnCurrency = Currency.valueOf(txnCurrencyStr);
        Currency senderWalletCurrency = senderWallet.getCurrency();


        Wallet receiverWallet = walletRepository.findByAccountNumber(receiverAccount)
                .orElseThrow(() -> new WalletNotFoundException(APP_CONSTANT.WALLET_NOT_FOUND));

        if (senderWallet.equals(receiverWallet))
            throw new TransactionProcessingException(APP_CONSTANT.TRANSFER_NOT_ALLOWED);

        String sendingTxnRef = generateTransactionId();
        request.setTransactionReferenceId(sendingTxnRef);
        createTransaction(request, senderWallet);

        String receivingTxnRef = generateTransactionId();
        request.setTransactionReferenceId(receivingTxnRef);
        createTransaction(request, receiverWallet);

        Currency receiverWalletCurrency = receiverWallet.getCurrency();

        if (!senderWalletCurrency.equals(txnCurrency)) {
            transactionAmount = CurrencyUtil.convert(txnCurrency, senderWalletCurrency, transactionAmount);
        }

        balanceLock.lock();
        try {

            List<Wallet> wallets = new ArrayList<>();
            // Check if the wallet has sufficient balance
            if (senderWallet.getBalance().compareTo(transactionAmount) < 0) {
                throw new InsufficientFundsException(APP_CONSTANT.INSUFFICIENT_FUNDS);
            }

            // Deduct the transaction amount from the wallet's balance
            BigDecimal updatedBalance = senderWallet.getBalance().subtract(transactionAmount);
            senderWallet.setBalance(updatedBalance);

            if (!receiverWalletCurrency.equals(txnCurrency)) {
                transactionAmount = CurrencyUtil.convert(txnCurrency, receiverWalletCurrency, transactionAmount);
            }

            BigDecimal receiverUpdatedBalance = receiverWallet.getBalance().add(transactionAmount);
            receiverWallet.setBalance(receiverUpdatedBalance);

            wallets.add(receiverWallet);
            wallets.add(senderWallet);


            walletRepository.saveAll(wallets);
        } finally {
            balanceLock.unlock();
        }


        Transaction transaction = updateTransactionStatus(sendingTxnRef, Status.COMPLETED);
        updateTransactionStatus(receivingTxnRef, Status.COMPLETED);

        TransactionResponse transactionResponse = TransactionResponse
                .builder()
                .user(AppUser.toDto(loggedInUser))
                .wallet(Wallet.toDto(senderWallet))
                .status(transaction.getTnxStatus().toString())
                .build();

        return ApiResponse
                .builder()
                .data(transactionResponse)
                .status(HttpStatus.OK)
                .message(APP_CONSTANT.TRANSFER_SUCCESS)
                .build();
    }

    @Override
    public ApiResponse<?> fetchAllWalletTxn(Long walletId) {

        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new WalletNotFoundException(APP_CONSTANT.WALLET_NOT_FOUND));

        List<Transaction> walletTxn = transactionRepository.findByWallet(wallet);

        List<TransactionHistory> transactionResponses = walletTxn.stream().map(txn -> TransactionHistory.builder().transactionChannel(txn.getTransactionChannel().toString()).transactionType(txn.getTransactionType().toString()).tnxStatus(txn.getTnxStatus().toString()).transactionAmount(txn.getTransactionAmount()).sendingCurrency(txn.getTxnCurrency().toString()).sendingInstitution(txn.getSendingInstitution()).transactionReferenceId(txn.getTransactionReferenceId()).senderAccount(txn.getSenderAccount()).createdDate(txn.getCreatedDate()).lastModifiedDate(txn.getLastModifiedDate()).build()).toList();

        return ApiResponse.builder().data(transactionResponses).status(HttpStatus.OK).message(APP_CONSTANT.TRANSACTION_FETCH_SUCCESS).build();
    }

    @Override
    public ApiResponse<?> viewAccountBalance(Long walletId) {

        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new WalletNotFoundException(APP_CONSTANT.WALLET_NOT_FOUND));

        ViewBalanceResponse response = ViewBalanceResponse.builder().accountBalance(wallet.getBalance()).build();

        return ApiResponse
                .builder()
                .data(response)
                .status(HttpStatus.OK)
                .message(APP_CONSTANT.OPERATION_SUCCESS)
                .build();
    }


    protected Transaction createTransaction(TransactionRequest request, Wallet wallet) {

        String transactionType = request.getTransactionType().toUpperCase();
        String transactionChannel = request.getTransactionChannel().toUpperCase();
        Transaction transaction = Transaction.builder().transactionReferenceId(request.getTransactionReferenceId()).transactionAmount(request.getTransactionAmount()).receiverAccount(request.getReceiverAccount()).senderAccount(request.getSenderAccount()).txnCurrency(Currency.valueOf(request.getTxnCurrency())).sendingInstitution(request.getSendingInstitution()).transactionType(TransactionType.valueOf(transactionType)).transactionChannel(TransactionChannel.valueOf(transactionChannel)).tnxStatus(Status.PENDING).wallet(wallet).build();

        return transactionRepository.save(transaction);

    }


    protected Transaction updateTransactionStatus(String transactionReferenceId, Status status) {

        Transaction transaction = transactionRepository.findByTransactionReferenceId(transactionReferenceId).orElseThrow(() -> new NotFoundException(String.format(APP_CONSTANT.TRANSACTION_NOT_FOUND, transactionReferenceId)));

        transaction.setTnxStatus(status);

        return transactionRepository.save(transaction);
    }

    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
