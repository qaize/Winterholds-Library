package com.example.AppWinterhold.Dao;

import com.example.AppWinterhold.Dto.Loan.LoanIndexDto;
import com.example.AppWinterhold.Entity.Loan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {


    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Loan.LoanIndexDto
            (
            l.id, CONCAT(c.firstName,' ',c.lastName),b.title,
            l.loanDate,l.dueDate,l.returnDate,l.note,l.denda
            )
            FROM Loan AS l
                LEFT JOIN l.book AS b
                LEFT JOIN l.customer AS c            
                WHERE b.title LIKE %:title% AND CONCAT(c.firstName,' ',c.lastName) LIKE %:name% AND l.returnDate IS NULL
                
            """)
    List<LoanIndexDto> getListLoanBySearch(String title, String name, Pageable paging);


    @Query("""
            SELECT COUNT(l.id)
            FROM Loan AS l
                LEFT JOIN l.book AS b
                LEFT JOIN l.customer AS c
             WHERE b.title LIKE %:title% AND CONCAT(c.firstName,' ',c.lastName) LIKE %:name% AND l.returnDate IS NULL
            """)
    Long getCountPage(String title, String name);

//    @Query("""
//            SELECT new com.example.AppWinterhold.Dto.Loan.LoanInsertDto
//            (
//            l.id, l.customerNumber,l.bookCode,
//            l.loanDate,l.dueDate,l.returnDate,l.note,l.denda
//            )
//            FROM Loan AS l
//
//                WHERE l.id = :id
//            """)
//    LoanInsertDto getLoanById(Long id);

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Loan.LoanIndexDto
            (
            l.id, CONCAT(c.firstName,' ',c.lastName),b.title,
            l.loanDate,l.dueDate,l.returnDate,l.note,l.denda
            )
            FROM Loan AS l
                LEFT JOIN l.book AS b
                LEFT JOIN l.customer AS c
                
            """)
    List<LoanIndexDto> getAll();

//    @Query("""
//            SELECT new com.example.AppWinterhold.Dto.Loan.LoanInsertDto
//            (
//            l.id, CONCAT(c.firstName,' ',c.lastName),b.title,
//            l.loanDate,l.dueDate,l.returnDate,l.note,l.denda
//            )
//            FROM Loan AS l
//                LEFT JOIN l.book AS b
//                LEFT JOIN l.customer AS c
//            """)
//    List<LoanInsertDto> getAllByInsert();

    @Query("""
            SELECT l.extend
            FROM Loan AS l
                WHERE l.id = :id
            """)
    Integer getExtendById(Long id);

    @Query("""
            SELECT COUNT(l.id)
            FROM Loan AS l
                LEFT JOIN l.book AS b
                LEFT JOIN l.customer AS c
             WHERE b.code = :code AND b.isBorrowed = 1 AND l.returnDate IS NULL
            """)
    Long CheckBook(String code);

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Loan.LoanIndexDto
            (
            l.id, CONCAT(c.firstName,' ',c.lastName),b.title,
            l.loanDate,l.dueDate,l.returnDate,l.note,l.denda
            )
            FROM Loan AS l
                LEFT JOIN l.book AS b
                LEFT JOIN l.customer AS c
            WHERE l.denda > 0
            """)
    List<LoanIndexDto> getOnDenda(Pageable page);

    @Query("""
            SELECT COUNT(id)
            FROM Loan AS l
                LEFT JOIN l.book AS b
                LEFT JOIN l.customer AS c
            WHERE l.denda > 0
            """)
    Long getCountPageDenda();


    @Query("""
            SELECT COUNT(l.id)
            FROM Loan AS l
             WHERE l.customerNumber = :CustomerNumber AND l.denda != 0 AND l.denda IS NOT NULL
            """)
    Long findCostumerOnLoan(@Param("CustomerNumber") String s);

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Loan.LoanIndexDto
            (
            l.id, CONCAT(c.firstName,' ',c.lastName),b.title,
            l.loanDate,l.dueDate,l.returnDate,l.note,l.denda
            )
            FROM Loan AS l
                LEFT JOIN l.book AS b
                LEFT JOIN l.customer AS c            
                WHERE l.returnDate IS NOT NULL
            """)
    List<LoanIndexDto> getListLoanHistoryBySearch(Pageable paging);

    @Query("""
            SELECT COUNT(id)
            FROM Loan AS l
            WHERE l.returnDate IS NOT NULL
            """)
    Long getCountHistoryPage();
}
