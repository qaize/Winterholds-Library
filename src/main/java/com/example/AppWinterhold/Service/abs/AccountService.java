package com.example.AppWinterhold.Service.abs;

import com.example.AppWinterhold.Dto.Account.AccountIndexDto;
import com.example.AppWinterhold.Dto.Account.AccountInsertDto;
import com.example.AppWinterhold.Dto.Account.AccountUpdateDto;
import com.example.AppWinterhold.Entity.Account;

import java.util.List;

public interface AccountService {
    void insert(AccountInsertDto dto);

    List<AccountIndexDto> getlist();

    AccountIndexDto getAccountByUsername(String username);

    void delete(String username);

    boolean passwordChecker(String password, String conPassword);

    public Account getAccount(String username);

    public void setCountWrong(Account acc);

    void update(AccountUpdateDto dto);

//    void changePassword( dto);
}
