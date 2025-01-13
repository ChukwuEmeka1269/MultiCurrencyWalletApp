package com.mecash.multiCurrencyWalletApp.repository;

import com.mecash.multiCurrencyWalletApp.models.Transaction;
import com.mecash.multiCurrencyWalletApp.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByTransactionReferenceId(String transactionReferenceId);

    List<Transaction> findByWallet(Wallet wallet);
}
