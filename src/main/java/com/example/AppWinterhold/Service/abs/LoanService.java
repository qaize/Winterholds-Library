package com.example.AppWinterhold.Service.abs;

import com.example.AppWinterhold.Dto.Loan.*;
import com.example.AppWinterhold.Dto.Models.DataDTO;
import com.example.AppWinterhold.Entity.Loan;
import com.example.AppWinterhold.Entity.LogsIncome;
import com.example.AppWinterhold.Entity.RequestLoan;

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
    Long getCountDenda(Loan loan);
    Long getCountPage(String title, String name);
    List<LoanIndexDto> getOnDenda(Integer page);
    List<LogsIncome> getLoanPaymentHistory(Integer page);
    List<LoanIndexDto> getListLoanHistoryBySearch(Integer page);
    DataDTO<List<LoanIndexDto>> getListLoanBySearch(Integer page, String title, String name);
    boolean returnBook(Long id);

    boolean newLoanRequest(RequestLoanDTO requestNew);
    DataDTO<List<RequestLoanIndexDTO>> getRequestLoanByCurrentLogin(String currentLogin, Integer page);
}
