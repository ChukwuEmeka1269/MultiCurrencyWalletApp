package com.mecash.multiCurrencyWalletApp.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {

    private BigDecimal transactionAmount;

    private String transactionReferenceId;

    private String senderAccount;

    private String sendingInstitution;

    private String receiverAccount;

    private String txnCurrency;

    private String transactionType;

    private String transactionChannel;
}
