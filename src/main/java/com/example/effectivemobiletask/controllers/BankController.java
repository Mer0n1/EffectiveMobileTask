package com.example.effectivemobiletask.controllers;

import com.example.effectivemobiletask.models.BankAccount;
import com.example.effectivemobiletask.models.EmailForm;
import com.example.effectivemobiletask.models.MoneyTransferForm;
import com.example.effectivemobiletask.models.PhoneNumberForm;
import com.example.effectivemobiletask.services.BankService;
import com.example.effectivemobiletask.utils.EmailValidator;
import com.example.effectivemobiletask.utils.PhoneNumberValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/bank")
public class BankController {

    @Autowired
    private BankService bankService;
    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private PhoneNumberValidator phoneNumberValidator;


    @PatchMapping("/deletePhoneNumber")
    public ResponseEntity<?> deletePhoneNumber(Principal principal) {

        if (bankService.deletePhoneNumber(principal.getName()))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().body("Ошибка: Нельзя удалить пока email не заполнен");
    }

    @PatchMapping("/deleteEmail")
    public ResponseEntity<?> deleteEmail(Principal principal) {

        if (bankService.deleteEmail(principal.getName()))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().body("Ошибка: Нельзя удалить пока номер телефона не заполнен");
    }

    @PatchMapping("/transferMoney")
    public ResponseEntity<?> transferMoney(@RequestBody @Valid MoneyTransferForm form, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors())
                errorMap.put(error.getField(), error.getDefaultMessage());
            return ResponseEntity.badRequest().body(errorMap);
        }

        if (bankService.transferMoney(form, principal.getName()))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().body("Недостаточно средств");
    }

    @PatchMapping("/changeEmail")
    public ResponseEntity<?> changeEmail(@Valid @RequestBody EmailForm form, BindingResult bindingResult, Principal principal) {

        BankAccount bankAccount = bankService.getBankAccount(principal.getName());
        bankAccount.setEmail(form.getEmail());

        emailValidator.validate(bankAccount, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors())
                errorMap.put(error.getField(), error.getDefaultMessage());
            return ResponseEntity.badRequest().body(errorMap);
        }

        bankService.save(bankAccount);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/changePhoneNumber")
    public ResponseEntity<?> changePhoneNumber(@Valid @RequestBody PhoneNumberForm form, BindingResult bindingResult, Principal principal) {

        BankAccount bankAccount = bankService.getBankAccount(principal.getName());
        bankAccount.setPhone(form.getPhone_number());

        phoneNumberValidator.validate(bankAccount, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors())
                errorMap.put(error.getField(), error.getDefaultMessage());
            return ResponseEntity.badRequest().body(errorMap);
        }

        bankService.save(bankAccount);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/searchByDateOfBirth")
    public List<BankAccount> searchByDate(@RequestParam("date") @DateTimeFormat(pattern = "dd.MM.yyyy") Date date) {
        return bankService.getBankAccountsByDateOfBirth(date);
    }

    @GetMapping("/searchByFullname")
    public List<BankAccount> searchByFullName(@RequestParam("full_name") String full_name) {
        return bankService.getBankAccountsByFullName(full_name);
    }

    @GetMapping("/searchByPhoneNumber")
    public List<BankAccount> searchByPhoneNumber(@RequestParam("phone_number") String phone_number) {
        return new ArrayList<>(Collections.singletonList(bankService.getBankAccountByPhone(phone_number)));
    }

    @GetMapping("/searchByEmail")
    public List<BankAccount> searchByEmail(String email) {
        return new ArrayList<>(Collections.singletonList(bankService.getBankAccountByEmail(email)));
    }
}
