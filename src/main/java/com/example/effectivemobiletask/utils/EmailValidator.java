package com.example.effectivemobiletask.utils;

import com.example.effectivemobiletask.models.BankAccount;
import com.example.effectivemobiletask.services.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EmailValidator implements Validator {

    @Autowired
    private BankService bankService;

    @Override
    public boolean supports(Class<?> clazz) {
        return BankAccount.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BankAccount account = (BankAccount) target;

        if (bankService.getBankAccountByEmail(account.getEmail()) != null)
            errors.rejectValue("email", "", "Такой email уже существует");
    }

}
