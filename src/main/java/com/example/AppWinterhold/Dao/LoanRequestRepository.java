package com.example.AppWinterhold.Dao;

import com.example.AppWinterhold.Dto.Loan.RequestLoanIndexDTO;
import com.example.AppWinterhold.Entity.RequestLoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigInteger;

@Transactional
@Repository
public interface LoanRequestRepository extends JpaRepository<RequestLoan, Long> {


    @Query("""
            SELECT 
            new com.example.AppWinterhold.Dto.Loan.RequestLoanIndexDTO(
             l.id,CONCAT(c.firstName,' ',c.lastName),b.title,l.requestDate,l.status )
            FROM 
                RequestLoan AS l
            JOIN 
                l.book as b
            JOIN 
                l.customer as c
            WHERE l.membershipNumber = :currentLogin
            """)
    Page<RequestLoanIndexDTO> findRequestLoanById(String currentLogin, Pageable pages);

    @Query("""
            SELECT 
            new com.example.AppWinterhold.Dto.Loan.RequestLoanIndexDTO(
             l.id,CONCAT(c.firstName,' ',c.lastName),b.title,l.requestDate,l.status )
            FROM 
                RequestLoan AS l
            JOIN 
                l.book as b
            JOIN 
                l.customer as c
            WHERE l.status = 0
            """)
    Page<RequestLoanIndexDTO> findAllRequestLoan(Pageable pages);
}
