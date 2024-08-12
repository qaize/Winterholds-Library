package com.example.winterhold.service.abs;

import com.example.winterhold.dto.account.AccountIndexDto;
import com.example.winterhold.dto.account.AccountInsertDto;
import com.example.winterhold.dto.account.AccountUpdateDto;
import com.example.winterhold.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    String getCurrentUserLogin();
    void delete(String username);
    List<AccountIndexDto> getlist();
    void setCountWrong(Account acc);
    void update(AccountUpdateDto dto);
    void insert(AccountInsertDto dto);
    Optional<Account> getAccount(String username);
    AccountIndexDto getAccountByUsername(String username);
    boolean passwordChecker(String password, String conPassword);


}
