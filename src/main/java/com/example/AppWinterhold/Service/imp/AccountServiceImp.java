package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Dao.AccountRepository;
import com.example.AppWinterhold.Dto.Account.AccountIndexDto;
import com.example.AppWinterhold.Dto.Account.AccountInsertDto;
import com.example.AppWinterhold.Dto.Account.AccountUpdateDto;
import com.example.AppWinterhold.Entity.Account;
import com.example.AppWinterhold.Service.abs.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImp implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void insert(AccountInsertDto dto) {
        String hash = passwordEncoder.encode(dto.getPassword());
        Account en = new Account(dto.getUsername(), hash,false,0);
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
        if(password.equals(conPassword)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public Account getAccount(String username) {
        return accountRepository.getAccount(username);
    }

    @Override
    public void setCountWrong(Account acc) {
        Account en = acc;
        accountRepository.save(en);
    }

    @Override
    public void update(AccountUpdateDto dto) {
        String hash = passwordEncoder.encode(dto.getPassword());
        Account en = new Account(dto.getUsername(), hash,false,0);
        accountRepository.save(en);
    }


}
