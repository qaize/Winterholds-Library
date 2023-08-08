package com.example.AppWinterhold.Dao;

import com.example.AppWinterhold.Dto.Account.AccountIndexDto;
import com.example.AppWinterhold.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {

    @Query("""
            SELECT 
                new com.example.AppWinterhold.Dto.Account.AccountIndexDto
                
                ( a.username, a.password)
            
            FROM
                Account AS a
            """)
    List<AccountIndexDto> getall();

    @Query("""
            SELECT 
                new com.example.AppWinterhold.Dto.Account.AccountIndexDto
                (a.username, a.password)
            FROM
                Account AS a       
            WHERE 
                a.username = :username
            """)
    AccountIndexDto getAccountByUsername(String username);

    @Query("""
            SELECT 
                COUNT(a.username)
            FROM
                Account AS a
            WHERE
                a.username = :s
            """)
    Long getCountUsernameByUsername(String s);


    @Query("""
            SELECT 
                a
            FROM
                Account AS a
            WHERE
                a.username = :username
            """)
    Account getAccount(String username);
}
