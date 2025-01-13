package com.mecash.multiCurrencyWalletApp.utils;

import com.mecash.multiCurrencyWalletApp.models.enums.Currency;

import java.math.BigDecimal;

public class CurrencyUtil {

    //A Dummy function to simulate currency conversion
    public static BigDecimal convert(Currency fromCurrency, Currency toCurrency, BigDecimal transactionAmount){
        return BigDecimal.valueOf(218.0);
    }
}
