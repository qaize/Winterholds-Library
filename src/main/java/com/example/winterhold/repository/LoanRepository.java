package com.example.winterhold.repository;

import com.example.winterhold.dto.loan.LoanIndexDto;
import com.example.winterhold.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("""
            select l
            from Loan l
            order by l.id DESC
            """)
    List<Loan> findLatestData();


    @Query("""
            SELECT 
                new com.example.winterhold.dto.loan.LoanIndexDto
                (   l.id, CONCAT(c.firstName,' ',c.lastName),b.title,
                    l.loanDate,l.dueDate,l.returnDate,l.note,l.denda    )
            FROM 
                Loan AS l
                            LEFT JOIN l.book AS b
                            LEFT JOIN l.customer AS c            
            WHERE 
                b.title LIKE %:title% 
                AND CONCAT(c.firstName,' ',c.lastName) LIKE %:name% 
                AND l.returnDate IS NULL    
            """)
    Page<LoanIndexDto> getListLoanBySearch(String title, String name, Pageable paging);


    @Query("""
            SELECT 
                COUNT(l.id)
            FROM 
                Loan AS l
                            LEFT JOIN l.book AS b
                            LEFT JOIN l.customer AS c
             WHERE 
                b.title LIKE %:title% 
                AND CONCAT(c.firstName,' ',c.lastName) LIKE %:name% 
                AND l.returnDate IS NULL
            """)
    Long getCountPage(String title, String name);

    @Query("""
            SELECT 
                new com.example.winterhold.dto.loan.LoanIndexDto
                (   l.id, CONCAT(c.firstName,' ',c.lastName),b.title,
                    l.loanDate,l.dueDate,l.returnDate,l.note,l.denda    )
            FROM 
                Loan AS l
                        LEFT JOIN l.book AS b
                        LEFT JOIN l.customer AS c
              """)
    List<LoanIndexDto> getAll();

    @Query("""
            SELECT 
                l.extend
            FROM 
                Loan AS l
            WHERE 
                l.id = :id
            """)
    Integer getExtendById(Long id);



    @Query("""
            SELECT 
                new com.example.winterhold.dto.loan.LoanIndexDto
                (   l.id, CONCAT(c.firstName,' ',c.lastName),b.title,
                    l.loanDate,l.dueDate,l.returnDate,l.note,l.denda    )
            FROM 
                Loan AS l
                        LEFT JOIN l.book AS b
                        LEFT JOIN l.customer AS c
            WHERE 
                l.denda > 0
            """)
    List<LoanIndexDto> getOnDenda(Pageable page);

    @Query("""
            SELECT 
                COUNT(id)
            FROM 
                Loan AS l
                        LEFT JOIN l.book AS b
                        LEFT JOIN l.customer AS c
            WHERE 
                l.denda > 0
            """)
    Long getCountPageDenda();


    @Query("""
            SELECT 
                COUNT(l.id)
            FROM 
                Loan AS l
            WHERE 
                l.customerNumber = :CustomerNumber 
                AND l.denda != 0 
                AND l.denda IS NOT NULL
            """)
    Long findCostumerOnLoan(@Param("CustomerNumber") String s);

    @Query("""
            SELECT 
                new com.example.winterhold.dto.loan.LoanIndexDto
                (   l.id, CONCAT(c.firstName,' ',c.lastName),b.title,
                    l.loanDate,l.dueDate,l.returnDate,l.note,l.denda    )
            FROM 
                Loan AS l
                        LEFT JOIN l.book AS b
                        LEFT JOIN l.customer AS c            
            WHERE 
                l.returnDate IS NOT NULL
            """)
    List<LoanIndexDto> getListLoanHistoryBySearch(Pageable paging);

    @Query("""
            SELECT 
                COUNT(id)
            FROM 
                Loan AS l
            WHERE 
                l.returnDate IS NOT NULL
            """)
    Long getCountHistoryPage();

    @Query("""
            SELECT 
                count(id)
            FROM
                Loan as l
            where 
                l.customerNumber = :customer AND l.bookCode = :book AND  l.returnDate IS NULL
            """)
    Long validateReplicateBookLoan(@Param("customer") String customer, @Param("book")String book);

    @Query("""
            select 
                l
            from 
                Loan as l
            where 
                l.customerNumber = :member and l.returnDate is null
            """)
    List<Loan> validateCurrentUserOnLoan(String member);

    @Query("""
            SELECT 
                new com.example.winterhold.dto.loan.LoanIndexDto
                (   l.id, CONCAT(c.firstName,' ',c.lastName),b.title,
                    l.loanDate,l.dueDate,l.returnDate,l.note,l.denda    )
            FROM 
                Loan AS l
                        LEFT JOIN l.book AS b
                        LEFT JOIN l.customer AS c
            WHERE 
                l.returnDate is null AND l.customerNumber = :username
              """)
    Page<LoanIndexDto> getCustomerOnLoan(String username,Pageable pages);
}
