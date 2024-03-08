package com.example.effectivemobiletask.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "bankaccount")
@Data
@ToString
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty
    @Column(name = "full_name")
    private String fullname;

    @NotNull
    @Column(name = "date_birth")
    private Date date_birth;

    @NotEmpty
    @Column(name = "email")
    private String email;

    @NotEmpty
    @Column(name = "phone_number")
    private String phone;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "starting_balance")
    private BigDecimal starting_balance;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @NotEmpty
    @Column(name = "username")
    private String username;

}
