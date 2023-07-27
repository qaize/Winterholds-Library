package com.example.AppWinterhold.Service.abs;

import com.example.AppWinterhold.Dto.Loan.LoanIndexDto;
import com.example.AppWinterhold.Dto.Loan.LoanInsertDto;
import com.example.AppWinterhold.Dto.Loan.LoanUpdateDto;
import com.example.AppWinterhold.Entity.Loan;

import java.time.LocalDate;
import java.util.List;

public interface LoanService {
    List<LoanIndexDto> getListLoanBySearch(Integer page, String title, String name);

    Long getCountPage(String title, String name);

    Long getCountDenda(LocalDate loanDate, LocalDate dueLoan );

    void insert(LoanInsertDto dto);

    Loan getLoanById(Long id);

    List<LoanIndexDto> getAll();

    void delete(Long id);

    void update(LoanUpdateDto dto);

//    List<LoanInsertDto> getAllByInsert();

    Long checkLoanBooks(String code);

    List<LoanIndexDto> getOnDenda(Integer page);

    void goPayOff(Long id);

    void insertByEntity(Loan data);

    Long getCountPageDenda();
}
