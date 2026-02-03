package com.uman.datademo;

import com.uman.datademo.Account;
import com.uman.datademo.AccountRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//Simple Logging Facade for Java - Slf4j
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService implements UserDetailsService {
    private final AccountRepo accountRepo;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) {
        Optional<Account> accountOpt = accountRepo.findAll().stream()
                .filter(account -> account.getUserEmail().equals(username))
                .findFirst();

        if (accountOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Account account = accountOpt.get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(account.getUserEmail())
                .password(account.getUserPass())
                .disabled(!account.isEnabled())
                .roles("USER") // Assign default role
                .build();
    }
    

    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }

    public Optional<Account> getAccountById(Integer userId) {
        return accountRepo.findById(userId);
    }

    public Account createAccount(Account account) {
        log.info("Creating account: {}", account);
        return accountRepo.save(account);
    }

    public void deleteAccount(Integer userId) {
        log.info("Deleting account with userId: {}", userId);
        accountRepo.deleteById(userId);
    }
}
