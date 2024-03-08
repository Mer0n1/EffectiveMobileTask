package com.example.effectivemobiletask.services;

import com.example.effectivemobiletask.config.PersonDetails;
import com.example.effectivemobiletask.models.BankAccount;
import com.example.effectivemobiletask.repositories.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private BankRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<BankAccount> credential = repository.getBankAccountByUsername(username);
        return credential.map(PersonDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + username));
    }
}
