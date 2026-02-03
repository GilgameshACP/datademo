package com.uman.datademo;

import com.uman.datademo.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository is an interface that provides access to data in a database
 */
public interface AccountRepo extends JpaRepository<Account, Integer> {
}