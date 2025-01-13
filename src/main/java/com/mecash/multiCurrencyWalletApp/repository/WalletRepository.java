package com.mecash.multiCurrencyWalletApp.repository;

import com.mecash.multiCurrencyWalletApp.models.AppUser;
import com.mecash.multiCurrencyWalletApp.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByAccountNumber(String accountNumber);
    Optional<Wallet> findByUser(AppUser user);

}
