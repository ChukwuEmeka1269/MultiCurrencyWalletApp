package com.mecash.multiCurrencyWalletApp.authentication;

import com.mecash.multiCurrencyWalletApp.models.Credentials;
import com.mecash.multiCurrencyWalletApp.models.requests.RegistrationRequest;
import com.mecash.multiCurrencyWalletApp.models.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody @Valid RegistrationRequest request){
        return new ResponseEntity<>(authService.signUp(request), HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<Void> authPost(@RequestBody @Valid Credentials credentials) {
        String token = authService.authenticate(credentials);
        return ResponseEntity.ok().header("Authorization", token).build();
    }
}
