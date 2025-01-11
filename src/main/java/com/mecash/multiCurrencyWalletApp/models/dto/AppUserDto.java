package com.mecash.multiCurrencyWalletApp.models.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AppUserDto {

    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String email;
}
