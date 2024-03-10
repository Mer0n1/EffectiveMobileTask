package com.example.effectivemobiletask.models.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BankAccountDTO {
    private String fullname;
    private Date date_birth;
    private String email;
    private String phone;
    private BigDecimal balance;
    private BigDecimal starting_balance;
}
