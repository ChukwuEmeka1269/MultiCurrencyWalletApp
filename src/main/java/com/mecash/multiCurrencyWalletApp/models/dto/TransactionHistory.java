package com.mecash.multiCurrencyWalletApp.models.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistory {

    private BigDecimal transactionAmount;

    private String transactionReferenceId;

    private String senderAccount;

    private String sendingInstitution;

    private String receiverAccount;

    private String sendingCurrency;

    private String receivingCurrency;

    private String transactionChannel;

    private String transactionType;

    private String tnxStatus;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

}
