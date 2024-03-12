package com.example.winterhold.Service.abs;

import com.example.winterhold.Dto.Account.AccountIndexDto;
import com.example.winterhold.Dto.Account.AccountInsertDto;
import com.example.winterhold.Dto.Account.AccountUpdateDto;
import com.example.winterhold.Entity.Account;

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
