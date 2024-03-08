package com.example.effectivemobiletask.controllers;

import com.example.effectivemobiletask.models.AuthRequest;
import com.example.effectivemobiletask.models.BankAccount;
import com.example.effectivemobiletask.models.CreationRequest;
import com.example.effectivemobiletask.services.BankService;
import com.example.effectivemobiletask.services.JwtService;
import com.example.effectivemobiletask.utils.EmailValidator;
import com.example.effectivemobiletask.utils.PhoneNumberValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BankService bankService;
    @Autowired
    private PhoneNumberValidator phoneNumberValidator;
    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/createAccount")
    public ResponseEntity<?> serviceAPI(@RequestBody @Valid CreationRequest request,
                           BindingResult bindingResult) {

        BankAccount bankAccount = bankService.createAccountFromCreationRequest(request);

        phoneNumberValidator.validate(bankAccount, bindingResult);
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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid AuthRequest authRequest,
                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors())
                errorMap.put(error.getField(), error.getDefaultMessage());
            return ResponseEntity.badRequest().body(errorMap.toString());
        }

        Authentication authenticate = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        if (authenticate.isAuthenticated()) {
            BankAccount account = bankService.getBankAccount(authRequest.getUsername());

            return ResponseEntity.ok().body(jwtService.generateToken(account));

        } else {
            return ResponseEntity.badRequest().body("Ошибка аутентификации");
        }
    }
}
