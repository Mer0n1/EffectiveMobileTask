package com.example.effectivemobiletask.controllers;

import com.example.effectivemobiletask.models.MoneyTransferForm;
import com.example.effectivemobiletask.services.BankService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;

import java.math.BigDecimal;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BankControllerTest {

    @InjectMocks
    private BankController controller;

    @Mock
    private BankService bankService;

    @Mock
    private Principal principal;


    @Test
    void transferMoney() {

        MoneyTransferForm form = new MoneyTransferForm();
        form.setQuality(new BigDecimal(5));
        form.setReceiverLogin("Root");

        when(principal.getName()).thenReturn("Root3");
        when(bankService.transferMoney(form, "Root3")).thenReturn(true);

        ResponseEntity<?> responseEntity = controller.transferMoney(form, new BeanPropertyBindingResult(form, "form"), principal);

        verify(bankService).transferMoney(form, "Root3");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}