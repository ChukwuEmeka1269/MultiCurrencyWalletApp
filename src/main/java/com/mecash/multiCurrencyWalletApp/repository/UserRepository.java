package com.mecash.multiCurrencyWalletApp.repository;

import com.mecash.multiCurrencyWalletApp.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findUserByEmail(String email);

    Optional<AppUser> findByPhoneNumberOrEmail(String phone, String email);

    boolean existsByEmail(String email);
}
