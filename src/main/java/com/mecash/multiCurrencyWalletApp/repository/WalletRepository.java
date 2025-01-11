package com.mecash.multiCurrencyWalletApp.repository;

import com.mecash.multiCurrencyWalletApp.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

}
