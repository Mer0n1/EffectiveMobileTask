package com.example.effectivemobiletask.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MoneyTransferForm {
    @NotEmpty
    private String receiverLogin;

    @NotNull
    private BigDecimal quality;
}
