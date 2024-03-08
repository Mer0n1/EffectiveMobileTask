package com.example.effectivemobiletask.utils;

import com.example.effectivemobiletask.models.BankAccount;
import com.example.effectivemobiletask.services.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class BalanceUpdater {

    @Autowired
    private BankService bankService;

    @Scheduled(fixedRate = 60000)
    public void updateBalances() {

        List<BankAccount> accounts = bankService.findAll();
        for (BankAccount account : accounts) {
            BigDecimal increasedBalance = account.getBalance().multiply(new BigDecimal("1.05"));

            if (account.getStarting_balance().multiply(new BigDecimal("2.07")).compareTo(increasedBalance) > 0)
                account.setBalance(increasedBalance);
        }

        bankService.saveAll(accounts);
    }
}
