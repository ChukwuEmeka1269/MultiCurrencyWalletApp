package com.mecash.multiCurrencyWalletApp.models.dto;

import com.mecash.multiCurrencyWalletApp.models.enums.Currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletDto {

    private BigDecimal balance;

    private Currency currency;

    private String accountNumber;
}
