package com.example.effectivemobiletask.services;

import com.example.effectivemobiletask.models.BankAccount;
import com.example.effectivemobiletask.models.CreationRequest;
import com.example.effectivemobiletask.models.MoneyTransferForm;
import com.example.effectivemobiletask.repositories.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public BankAccount getBankAccount(String username) { return bankRepository.getBankAccountByUsername(username).orElse(null); }

    public BankAccount getBankAccountByPhone(String phone) { return bankRepository.getBankAccountByPhone(phone).orElse(null); }

    public BankAccount getBankAccountByEmail(String email) { return bankRepository.getBankAccountByEmail(email).orElse(null);}

    public List<BankAccount> getBankAccountsByFullName(String full_name) { return bankRepository.findByFullNameLike(full_name).orElse(null); }

    public List<BankAccount> getBankAccountsByDateOfBirth(Date date) { return bankRepository.findAccountsNewerThan(date).orElse(null); }

    public List<BankAccount> findAll() { return bankRepository.findAll(); }

    @Transactional
    public void saveAll(List<BankAccount> list) { bankRepository.saveAll(list); }

    @Transactional
    public void save(BankAccount bankAccount) { bankRepository.save(bankAccount); }

    public BankAccount createAccountFromCreationRequest(CreationRequest request) {
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.setPhone(request.getPhone());
        newBankAccount.setEmail(request.getEmail());
        newBankAccount.setBalance(request.getBalance());
        newBankAccount.setFullname(request.getFullname());
        newBankAccount.setDate_birth(request.getDate_birth());
        newBankAccount.setStarting_balance(request.getBalance());
        newBankAccount.setPassword(passwordEncoder.encode(request.getPassword()));
        newBankAccount.setUsername(request.getUsername());

        return newBankAccount;
    }

    @Transactional
    public boolean deletePhoneNumber(String username) {
        BankAccount bankAccount = getBankAccount(username);

        if (bankAccount != null) {

            if (bankAccount.getEmail().isEmpty())
                return false;

            bankAccount.setPhone("");

            return true;
        } else
            return false;
    }

    @Transactional
    public boolean deleteEmail(String username) {
        BankAccount bankAccount = getBankAccount(username);

        if (bankAccount != null) {

            if (bankAccount.getPhone().isEmpty())
                return false;

            bankAccount.setEmail("");

            return true;
        } else
            return false;
    }

    @Transactional
    public boolean transferMoney(MoneyTransferForm form, String senderLogin) {

        BankAccount ReceiverAccount = getBankAccount(form.getReceiverLogin());
        BankAccount SenderAccount   = getBankAccount(senderLogin);

        if (SenderAccount.getBalance().doubleValue() > form.getQuality().doubleValue()) {
            ReceiverAccount.setBalance(ReceiverAccount.getBalance().add(form.getQuality()));
            SenderAccount.setBalance(SenderAccount.getBalance().subtract(form.getQuality()));

            return true;
        }

        return false;
    }

}
