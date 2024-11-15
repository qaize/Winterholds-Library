package com.example.winterhold.repository;

import com.example.winterhold.dto.account.AccountIndexDto;
import com.example.winterhold.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    @Query("""
            SELECT
                new com.example.winterhold.dto.account.AccountIndexDto
             
                ( a.username, a.password)
            
            FROM
                Account AS a
            """)
    List<AccountIndexDto> getall();

    @Query("""
            SELECT
                new com.example.winterhold.dto.account.AccountIndexDto
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
