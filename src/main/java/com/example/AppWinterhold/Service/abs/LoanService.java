package com.example.AppWinterhold.Service.abs;

import com.example.AppWinterhold.Dto.Loan.LoanIndexDto;
import com.example.AppWinterhold.Dto.Loan.LoanInsertDto;
import com.example.AppWinterhold.Dto.Loan.LoanUpdateDto;

import java.util.List;

public interface LoanService {
    List<LoanIndexDto> getListLoanBySearch(Integer page, String title, String name);

    Long getCountPage(String title, String name);

    void insert(LoanInsertDto dto);

    LoanInsertDto getLoanById(Long id);

    List<LoanIndexDto> getAll();

    void delete(Long id);

    void update(LoanUpdateDto dto,Integer set);

    List<LoanInsertDto> getAllByInsert();

    Long checkLoanBooks(String code);
}
