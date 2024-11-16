package com.example.winterhold.service.imp;

import com.example.winterhold.repository.AccountRepository;
import com.example.winterhold.repository.CustomerRepository;
import com.example.winterhold.dto.account.AccountIndexDto;
import com.example.winterhold.dto.account.AccountInsertDto;
import com.example.winterhold.dto.account.AccountUpdateDto;
import com.example.winterhold.dto.customer.CustomerInsertDto;
import com.example.winterhold.entity.Account;
import com.example.winterhold.entity.Customer;
import com.example.winterhold.service.abs.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class AccountServiceImp implements AccountService {

    private final AccountRepository accountRepository;

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImp ( AccountRepository accountRepository,CustomerRepository customerRepository,PasswordEncoder passwordEncoder ){
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createNewAccount(AccountInsertDto accountInsertRequest) {

        if (accountInsertRequest.getRole().equals("customer")){
            CustomerInsertDto newDataCustomer = new CustomerInsertDto(accountInsertRequest.getUsername(), accountInsertRequest.getName(), LocalDate.now().plusYears(2));
            customerRepository.save(mapInsertCustomer(newDataCustomer));
        }

        String encryptedPassword = passwordEncoder.encode(accountInsertRequest.getPassword());
        Account en = new Account(accountInsertRequest.getUsername(), encryptedPassword, false, 0, accountInsertRequest.getName(),accountInsertRequest.getRole());

        accountRepository.save(en);
    }

    private Customer mapInsertCustomer(CustomerInsertDto dto) {

        LocalDateTime createDate = LocalDateTime.now();

        return new Customer(
                dto.getMembershipNumber(),
                dto.getFirstName(),
                Objects.nonNull(dto.getLastName()) ? dto.getLastName() : "",
                null,
                null,
                null,
                null,
                dto.getMembershipExpireDate(),
                createDate, 0, 0, 0,0);
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
        return password.equals(conPassword);
    }

    @Override
    public Optional<Account> getAccount(String username) {
        return accountRepository.findById(username);
    }

    @Override
    public void setCountWrong(Account acc) {
        accountRepository.save(acc);
    }

    @Override
    public void update(AccountUpdateDto dto) {

        String hash = passwordEncoder.encode(dto.getPassword());
        Account en = new Account(dto.getUsername(), hash, false, 0, "sementara","admin");
        accountRepository.save(en);
    }

    @Override
    public String getCurrentUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userlogin;

        Optional<Account> ac = accountRepository.findById(authentication.getName());

        userlogin = ac.isPresent() ? ac.get().getUserLogin() : authentication.getName();

        log.info("User login : {}",userlogin);
        return userlogin;
    }

}
