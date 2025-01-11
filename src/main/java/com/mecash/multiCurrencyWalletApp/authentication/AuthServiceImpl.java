package com.mecash.multiCurrencyWalletApp.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mecash.multiCurrencyWalletApp.constants.APP_CONSTANT;
import com.mecash.multiCurrencyWalletApp.exceptions.UserNotFoundException;
import com.mecash.multiCurrencyWalletApp.jwt.JwtTokenProvider;
import com.mecash.multiCurrencyWalletApp.models.AppUser;
import com.mecash.multiCurrencyWalletApp.models.Credentials;
import com.mecash.multiCurrencyWalletApp.models.Wallet;
import com.mecash.multiCurrencyWalletApp.models.dto.AppUserDto;
import com.mecash.multiCurrencyWalletApp.models.enums.Currency;
import com.mecash.multiCurrencyWalletApp.models.enums.Role;
import com.mecash.multiCurrencyWalletApp.models.requests.RegistrationRequest;
import com.mecash.multiCurrencyWalletApp.models.responses.ApiResponse;
import com.mecash.multiCurrencyWalletApp.models.responses.UserRegistrationResponse;
import com.mecash.multiCurrencyWalletApp.repository.UserRepository;
import com.mecash.multiCurrencyWalletApp.repository.WalletRepository;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

import static com.mecash.multiCurrencyWalletApp.models.enums.Currency.*;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    private final WalletRepository walletRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${wallet.starting.balance}")
    private BigDecimal balance;

    @Override
    public ApiResponse<?> signUp(RegistrationRequest request) {

        AppUser appUser = AppUser.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.USER)
                .build();
        AppUser savedUser = userRepository.save(appUser);

        AppUserDto appUserDto = AppUser.toDto(savedUser);

        Currency currency = NGN;

        if(ObjectUtils.isNotEmpty(request.getCurrency())){
            currency = Currency.valueOf(request.getCurrency());
        }

        Wallet wallet = Wallet
                .builder()
                .user(savedUser)
                .accountNumber(savedUser.getPhoneNumber())
                .balance(balance)
                .currency(currency)
                .build();

        Wallet savedWallet = walletRepository.save(wallet);

        UserRegistrationResponse response = UserRegistrationResponse
                .builder()
                .user(appUserDto)
                .balance(savedWallet.getBalance())
                .accountNumber(savedUser.getPhoneNumber())
                .currency(savedWallet.getCurrency().name())
                .build();

        return ApiResponse.builder()
                .data(response)
                .status(HttpStatus.CREATED)
                .message(APP_CONSTANT.USER_SIGNUP_SUCCESS_MSG)
                .build();
    }

    @SneakyThrows
    @Override
    public String authenticate(Credentials credentials) {

        AppUser user = userRepository.findUserByEmail(credentials.getEmail()).orElseThrow(() -> new UserNotFoundException(String.format("User with email : %s not found.", credentials.getEmail())));

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));
        } catch (Exception ex) {
            throw new BadCredentialsException(ex.getMessage());
        }
        if (authentication.isAuthenticated()) {
            String userJsonString = objectMapper.writeValueAsString(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtTokenProvider.generateToken(userJsonString);
        } else {
            throw new AuthException("User is not authenticated.");
        }

    }
}
