package com.example.effectivemobiletask.repositories;

import com.example.effectivemobiletask.models.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BankRepository extends JpaRepository<BankAccount,Integer> {
    Optional<BankAccount> getBankAccountByUsername(String username);
    Optional<BankAccount> getBankAccountByPhone(String login);
    Optional<BankAccount> getBankAccountByEmail(String email);

    @Query("SELECT p FROM BankAccount p WHERE p.fullname LIKE :fullName%")
    Optional<List<BankAccount>> findByFullNameLike(@Param("fullName") String fullName);

    @Query("SELECT b FROM BankAccount b WHERE b.date_birth > :date")
    Optional<List<BankAccount>> findAccountsNewerThan(@Param("date") Date date);

}
