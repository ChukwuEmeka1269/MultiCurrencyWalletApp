package com.mecash.multiCurrencyWalletApp.models.requests;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistrationRequest {
    @Size(max = 100)
    @NotEmpty(message = "No empty names allowed")
    @NotBlank(message = "Name cannot be blank..")
    private String firstname;

    @Size(max = 100)
    @NotEmpty(message = "No empty names allowed")
    @NotBlank(message = "Name cannot be blank..")
    private String lastname;

    @Pattern(regexp = "^\\d{11}$", message = "Phone number must contain exactly 11 digits")
    @NotEmpty(message = "No empty Phone numbers allowed")
    @NotBlank(message = "Phone number be blank..")
    private String phoneNumber;

    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Email cannot be null or empty")
    private String email;

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, message = "Please provide a strong password with minimum character length of 8")
    private String password;
}
