package com.example.effectivemobiletask.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CreationRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    @Size(min = 11, max = 11, message = "Номер телефона должен состоять из 11 символов")
    private String phone;

    @NotEmpty
    @Email(message = "Email адрес некорректен")
    private String email;

    @NotNull
    private BigDecimal balance;

    @NotEmpty
    private String fullname;

    @NotNull
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date date_birth;
}
