package com.mecash.multiCurrencyWalletApp.models;

import com.mecash.multiCurrencyWalletApp.models.enums.Currency;
import com.mecash.multiCurrencyWalletApp.models.enums.Status;
import com.mecash.multiCurrencyWalletApp.models.enums.TransactionChannel;
import com.mecash.multiCurrencyWalletApp.models.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Transaction")
public class Transaction extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal transactionAmount;

    @Column(unique = true)
    private String transactionReferenceId;

    @Column(nullable = false)
    private String senderAccount;

    private String sendingInstitution;

    @Column(nullable = false)
    private String receiverAccount;

    @Column(nullable = false)
    private Currency txnCurrency;

    @Enumerated(EnumType.STRING)
    private TransactionChannel transactionChannel;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private Status tnxStatus;

    @ManyToOne
    private Wallet wallet;
}
