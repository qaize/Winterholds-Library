package com.example.winterhold.Service.abs;

import com.example.winterhold.Dto.CurrentLoginDetailDTO;
import com.example.winterhold.Dto.Loan.*;
import com.example.winterhold.Dto.Models.DataDTO;
import com.example.winterhold.Entity.Loan;
import com.example.winterhold.Entity.LogsIncome;

import java.util.List;

public interface LoanService {

    void delete(Long id);
    void payment(Long id);
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

    DataDTO<Boolean> newLoanRequest(RequestLoanDTO requestNew);
    DataDTO<List<RequestLoanIndexDTO>> getRequestLoanByCurrentLogin(CurrentLoginDetailDTO currentLogin, Integer page);

    DataDTO<Boolean> insertByRequestId(Long id);

    DataDTO<List<LoanIndexDto>> getListLoanByMembershipNumber(String username);

    DataDTO<Boolean> deleteLoanRequest(Long id);

    Integer getNotification();
}
