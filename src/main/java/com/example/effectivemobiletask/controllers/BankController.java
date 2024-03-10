package com.example.effectivemobiletask.controllers;

import com.example.effectivemobiletask.models.BankAccount;
import com.example.effectivemobiletask.models.EmailForm;
import com.example.effectivemobiletask.models.MoneyTransferForm;
import com.example.effectivemobiletask.models.PhoneNumberForm;
import com.example.effectivemobiletask.models.dto.BankAccountDTO;
import com.example.effectivemobiletask.services.BankService;
import com.example.effectivemobiletask.utils.EmailValidator;
import com.example.effectivemobiletask.utils.PhoneNumberValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bank")
public class BankController {

    @Autowired
    private BankService bankService;
    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private PhoneNumberValidator phoneNumberValidator;
    @Autowired
    private ModelMapper modelMapper;

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
    public List<BankAccountDTO> searchByDate(@RequestParam("date") @DateTimeFormat(pattern = "dd.MM.yyyy") Date date) {
        return bankService.getBankAccountsByDateOfBirth(date)
                .stream().map(this::convertToBankAccountDTO).collect(Collectors.toList());
    }

    @GetMapping("/searchByFullname")
    public List<BankAccountDTO> searchByFullName(@RequestParam("full_name") String full_name) {
        return bankService.getBankAccountsByFullName(full_name)
                .stream().map(this::convertToBankAccountDTO).collect(Collectors.toList());
    }

    @GetMapping("/searchByPhoneNumber")
    public List<BankAccountDTO> searchByPhoneNumber(@RequestParam("phone_number") String phone_number) {
        return new ArrayList<>(Collections.singletonList(bankService.getBankAccountByPhone(phone_number)))
                .stream().map(this::convertToBankAccountDTO).collect(Collectors.toList());
    }

    @GetMapping("/searchByEmail")
    public List<BankAccountDTO> searchByEmail(String email) {
        return new ArrayList<>(Collections.singletonList(bankService.getBankAccountByEmail(email)))
                .stream().map(this::convertToBankAccountDTO).collect(Collectors.toList());
    }

    private BankAccount convertToBankAccount(BankAccountDTO dto) {
        if (dto == null)
            return null;

        return modelMapper.map(dto, BankAccount.class);
    }
    private BankAccountDTO convertToBankAccountDTO(BankAccount account) {
        if (account == null)
            return null;

        return modelMapper.map(account, BankAccountDTO.class);
    }
}
