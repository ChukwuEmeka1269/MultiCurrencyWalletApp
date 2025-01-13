package com.mecash.multiCurrencyWalletApp.models.responses;

import com.mecash.multiCurrencyWalletApp.models.dto.AppUserDto;
import com.mecash.multiCurrencyWalletApp.models.dto.WalletDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {

    private AppUserDto user;
    private WalletDto wallet;
    private String status;
}
