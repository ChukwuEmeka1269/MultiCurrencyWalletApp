package com.mecash.multiCurrencyWalletApp.services;


import com.mecash.multiCurrencyWalletApp.models.AppUser;
import com.mecash.multiCurrencyWalletApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.mecash.multiCurrencyWalletApp.constants.APP_CONSTANT.USER_NOT_FOUND_MSG;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public AppUser loadUserByUsername(String phoneOrEmail) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumberOrEmail(phoneOrEmail, phoneOrEmail).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, phoneOrEmail)));
    }
}
