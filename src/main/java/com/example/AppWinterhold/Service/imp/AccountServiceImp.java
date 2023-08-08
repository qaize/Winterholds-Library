package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Dao.AccountRepository;
import com.example.AppWinterhold.Dto.Account.AccountIndexDto;
import com.example.AppWinterhold.Dto.Account.AccountInsertDto;
import com.example.AppWinterhold.Dto.Account.AccountUpdateDto;
import com.example.AppWinterhold.Entity.Account;
import com.example.AppWinterhold.Service.abs.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImp implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImp.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void insert(AccountInsertDto dto) {

        String hash = passwordEncoder.encode(dto.getPassword());
        Account en = new Account(dto.getUsername(), hash, false, 0, dto.getName());

        accountRepository.save(en);
    }

    @Override
    public List<AccountIndexDto> getlist() {
        return accountRepository.getall();
    }

    @Override
    public AccountIndexDto getAccountByUsername(String username) {
        return accountRepository.getAccountByUsername(username);
    }

    @Override
    public void delete(String username) {
        accountRepository.deleteById(username);
    }

    @Override
    public boolean passwordChecker(String password, String conPassword) {
        return password.equals(conPassword)? true : false;
    }

    @Override
    public Account getAccount(String username) {
        return accountRepository.findById(username).get();
    }

    @Override
    public void setCountWrong(Account acc) {
        Account en = acc;
        accountRepository.save(en);
    }

    @Override
    public void update(AccountUpdateDto dto) {

        String hash = passwordEncoder.encode(dto.getPassword());
        Account en = new Account(dto.getUsername(), hash, false, 0, "sementara");
        accountRepository.save(en);
    }

    @Override
    public String getCurrentUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userlogin = "Default";

        Optional<Account> ac = accountRepository.findById(authentication.getName().toString());

        if (ac.get() != null) {
            userlogin = ac.get().getUserLogin() != null ? ac.get().getUserLogin() : authentication.getName();
        }

        LOGGER.info(userlogin);
        return userlogin;
    }


}
