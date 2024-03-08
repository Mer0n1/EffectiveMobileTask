package com.example.effectivemobiletask.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PhoneNumberForm {
    @NotEmpty
    @Size(min = 11, max = 11)
    private String phone_number;
}
