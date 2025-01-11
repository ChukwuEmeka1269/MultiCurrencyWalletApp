package com.mecash.multiCurrencyWalletApp.models.responses;

import com.mecash.multiCurrencyWalletApp.models.AppUser;
import com.mecash.multiCurrencyWalletApp.models.dto.AppUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationResponse {
    private AppUserDto user;
    private String accountNumber;
    private BigDecimal balance;
    private String currency;
}
