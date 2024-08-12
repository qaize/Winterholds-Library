package com.example.winterhold.dao;

import com.example.winterhold.dto.loan.RequestLoanIndexDTO;
import com.example.winterhold.entity.RequestLoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface LoanRequestRepository extends JpaRepository<RequestLoan, Long> {


    @Query("""
            SELECT 
            new com.example.winterhold.dto.loan.RequestLoanIndexDTO(
             l.id,CONCAT(c.firstName,' ',c.lastName),b.title,l.requestDate,l.status )
            FROM 
                RequestLoan AS l
            JOIN 
                l.book as b
            JOIN 
                l.customer as c
            WHERE l.membershipNumber = :currentLogin AND l.isActive = 1
            """)
    Page<RequestLoanIndexDTO> findRequestLoanById(String currentLogin, Pageable pages);

    @Query("""
            SELECT 
            new com.example.winterhold.dto.loan.RequestLoanIndexDTO(
             l.id,CONCAT(c.firstName,' ',c.lastName),b.title,l.requestDate,l.status )
            FROM 
                RequestLoan AS l
            JOIN 
                l.book as b
            JOIN 
                l.customer as c
            WHERE l.status = 0 AND l.isActive = 1
            """)
    Page<RequestLoanIndexDTO> findAllRequestLoan(Pageable pages);


    @Query("""
            select count(r)
            from RequestLoan as r
            where r.membershipNumber = :membershipNumber AND r.bookCode = :bookCode AND r.isActive = 1
            """)
    Long validateRepicateRequest(String membershipNumber, String bookCode);
}
