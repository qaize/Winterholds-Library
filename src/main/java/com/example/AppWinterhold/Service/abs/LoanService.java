package com.example.AppWinterhold.Service.abs;

import com.example.AppWinterhold.Dto.Loan.LoanIndexDto;
import com.example.AppWinterhold.Dto.Loan.LoanInsertDto;
import com.example.AppWinterhold.Dto.Loan.LoanUpdateDto;
import com.example.AppWinterhold.Entity.Loan;
import com.example.AppWinterhold.Entity.LogsIncome;

import java.time.LocalDate;
import java.util.List;

public interface LoanService {

    void delete(Long id);
    void goPayOff(Long id);
    Long getCountPageDenda();
    Long getCountHistoryPage();
    Loan getLoanById(Long id);
    void extendLoan(Loan data);
    List<LoanIndexDto> getAll();
    Long getCountPaymentHistory();
    void update(LoanUpdateDto dto);
    void insert(LoanInsertDto dto);
    Long checkLoanBooks(String code);
    Long getCountDenda(LocalDate dueDate);
    Long getCountPage(String title, String name);
    List<LoanIndexDto> getOnDenda(Integer page);
    List<LogsIncome> getLoanPaymentHistory(Integer page);
    List<LoanIndexDto> getListLoanHistoryBySearch(Integer page);
    List<LoanIndexDto> getListLoanBySearch(Integer page, String title, String name);
}
