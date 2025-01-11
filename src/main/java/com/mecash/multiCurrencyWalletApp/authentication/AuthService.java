package com.mecash.multiCurrencyWalletApp.authentication;

import com.mecash.multiCurrencyWalletApp.models.Credentials;
import com.mecash.multiCurrencyWalletApp.models.requests.RegistrationRequest;
import com.mecash.multiCurrencyWalletApp.models.responses.ApiResponse;

public interface AuthService {

    ApiResponse<?> signUp(RegistrationRequest request);
    String authenticate(Credentials credentials);
}
