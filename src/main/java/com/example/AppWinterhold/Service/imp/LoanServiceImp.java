package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Dao.LoanRepository;
import com.example.AppWinterhold.Dto.Loan.LoanIndexDto;
import com.example.AppWinterhold.Dto.Loan.LoanInsertDto;
import com.example.AppWinterhold.Dto.Loan.LoanUpdateDto;
import com.example.AppWinterhold.Entity.Loan;
import com.example.AppWinterhold.Service.abs.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoanServiceImp implements LoanService {

    @Autowired
    private LoanRepository loanRepository;
    @Override
    public List<LoanIndexDto> getListLoanBySearch(Integer page, String title, String name) {
        Integer row = 10;
        Pageable paging = PageRequest.of(page-1,row, Sort.by("id").descending());
        return loanRepository.getListLoanBySearch(title,name,paging);
    }

    @Override
    public Long getCountPage(String title, String name) {
        Integer row = 10;
        Double totalData =(double) loanRepository.getCountPage(title,name);
        Long totaPage =(long)  Math.ceil(totalData/row);

        return totaPage;
    }

    @Override
    public Long getCountDenda(LocalDate loanDate, LocalDate dueLoan) {

        Long between = ChronoUnit.DAYS.between(loanDate,dueLoan) * 2000;

        return between;
    }

    @Override
    public void insert(LoanInsertDto dto) {

        Loan en ;
        if(dto.getDenda() == null) {
         en = new Loan(dto.getId(),dto.getCustomerNumber(),dto.getBookCode(),dto.getLoanDate(),dto.getDueDate(),dto.getReturnDate(),dto.getNote(),0,null);
        }
        else {
            en = new Loan(dto.getId(),dto.getCustomerNumber(),dto.getBookCode(),dto.getLoanDate(),dto.getDueDate(),dto.getReturnDate(),dto.getNote(),0,dto.getDenda());
        }

        loanRepository.save(en);
    }



    @Override
    public LoanInsertDto getLoanById(Long id) {
//        List<Loan> findByCustomer = loanRepository.findBy()

        return loanRepository.getLoanById(id);
    }

    @Override
    public List<LoanIndexDto> getAll() {
        return loanRepository.getAll();
    }

    @Override
    public void delete(Long id) {
        loanRepository.deleteById(id);
    }

    @Override
    public void update(LoanUpdateDto dto) {
        Loan en = new Loan(dto.getId(),dto.getCustomerNumber(),dto.getBookCode(),
                dto.getLoanDate(),dto.getDueDate(),dto.getReturnDate(),dto.getNote(),0,dto.getDenda());
        loanRepository.save(en);
    }

    @Override
    public List<LoanInsertDto> getAllByInsert() {
        return loanRepository.getAllByInsert();
    }

    @Override
    public Long checkLoanBooks(String code){
        return loanRepository.CheckBook(code);
    }
}
