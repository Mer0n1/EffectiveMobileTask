package com.example.effectivemobiletask.config;

import com.example.effectivemobiletask.models.BankAccount;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
@Data
public class PersonDetails implements UserDetails {
    private Long id;
    private String username;
    private String password;

    public PersonDetails(BankAccount userCredential) {
        this.username = userCredential.getUsername();
        this.password = userCredential.getPassword();
    }
    public PersonDetails() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

