package com.example.effectivemobiletask.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailForm {
    @NotEmpty
    @Email
    private String email;
}
