package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Dao.LoanRepository;
import com.example.AppWinterhold.Dao.LogsIncomeRepository;
import com.example.AppWinterhold.Dto.Loan.LoanIndexDto;
import com.example.AppWinterhold.Dto.Loan.LoanInsertDto;
import com.example.AppWinterhold.Dto.Loan.LoanUpdateDto;
import com.example.AppWinterhold.Entity.Loan;
import com.example.AppWinterhold.Entity.LogsIncome;
import com.example.AppWinterhold.Service.abs.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class LoanServiceImp implements LoanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoanServiceImp.class);

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LogsIncomeRepository logsIncomeRepository;
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

    @Override
    public List<Loan> getOnDenda() {


        return loanRepository.getOnDenda();
    }

    @Override
    public void goPayOff(Long id) {
        Loan data = loanRepository.findById(id).get();

        List<LogsIncome> logList = logsIncomeRepository.findAll();


        if(logList.size()==0){
            LOGGER.info("MASUK");
            LogsIncome log = new LogsIncome(UUID.randomUUID().toString(),"PELUNASAN","asem",data.getDenda().doubleValue(),0.0,LocalDate.now());
            data.setDenda(0L);
            loanRepository.save(data);
            logsIncomeRepository.save(log);
        }


//        else{
//            LogsIncome income = logsIncomeRepository.findLatestData();
//            LogsIncome log = new LogsIncome(UUID.randomUUID().toString(),"PELUNASAN","asem",data.getDenda().doubleValue(),0.0,LocalDate.now());
//            data.setDenda(0L);
//            loanRepository.save(data);
//            logsIncomeRepository.save(log);
//
//        }

    }

}
