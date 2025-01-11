package com.mecash.multiCurrencyWalletApp.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Credentials {

    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Email cannot be null or empty")
    private String email;

    @NotBlank(message = "Password cannot be blank.")
    private String password;
}
