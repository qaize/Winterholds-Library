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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static com.example.AppWinterhold.Const.actionConst.*;


@Service
public class LoanServiceImp implements LoanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoanServiceImp.class);

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LogsIncomeRepository logsIncomeRepository;

    @Autowired
    private LogServiceImpl logService;

    @Override
    public List<LoanIndexDto> getListLoanBySearch(Integer page, String title, String name) {
        Integer row = 5;
        Pageable paging = PageRequest.of(page - 1, row, Sort.by("id").descending());
        return loanRepository.getListLoanBySearch(title, name, paging);
    }

    @Override
    public Long getCountPage(String title, String name) {
        Integer row = 5;
        Double totalData = (double) loanRepository.getCountPage(title, name);
        Long totaPage = (long) Math.ceil(totalData / row);

        return totaPage;
    }

    @Override
    public Long getCountDenda(LocalDate dueDate) {

        Long between = ChronoUnit.DAYS.between(dueDate, LocalDate.now()) * 2000;

        return between;
    }

    @Override
    public void insert(LoanInsertDto dto) {

        try {
            Loan en;
            if (dto.getDenda() == null) {
                en = new Loan(dto.getId(), dto.getCustomerNumber(), dto.getBookCode(), dto.getLoanDate(), dto.getLoanDate().plusDays(5), null, dto.getNote(), 0, null);
                logService.saveLogs(LOAN, SUCCESS, INSERT);
            } else {
                en = new Loan(dto.getId(), dto.getCustomerNumber(), dto.getBookCode(), dto.getLoanDate(), dto.getLoanDate().plusDays(5), null, dto.getNote(), 0, dto.getDenda());
                logService.saveLogs(LOAN, SUCCESS, INSERT);
            }
            loanRepository.save(en);
        } catch (Exception e) {
            logService.saveLogs(LOAN, e.getMessage(), INSERT);
        }
    }


    @Override
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).get();
    }

    @Override
    public List<LoanIndexDto> getAll() {
        return loanRepository.getAll();
    }

    @Override
    public void delete(Long id) {
        try {
            loanRepository.deleteById(id);
            logService.saveLogs(LOAN, SUCCESS, DELETE);
        } catch (Exception e) {
            logService.saveLogs(LOAN, e.getMessage(), DELETE);
        }
    }

    //    UNSUSED SERVICES
    @Override
    public void update(LoanUpdateDto dto) {
        Loan en = new Loan(dto.getId(), dto.getCustomerNumber(), dto.getBookCode(),
                dto.getLoanDate(), dto.getDueDate(), dto.getReturnDate(), dto.getNote(),
                0, dto.getDenda());
        loanRepository.save(en);
    }


    @Override
    public Long checkLoanBooks(String code) {
        return loanRepository.CheckBook(code);
    }

    @Override
    public void goPayOff(Long id) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String authName = authentication.getName();

            Timestamp date = Timestamp.from(Instant.now());
            Loan data = loanRepository.findById(id).get();
            Sort sort = Sort.by("transactionDate").descending();
            List<LogsIncome> logList = logsIncomeRepository.findAll(sort);
            if (logList.size() == 0) {
                LogsIncome log = new LogsIncome(UUID.randomUUID().toString(), "PELUNASAN DENDA ID :" + data.getId() + "/" + data.getCustomerNumber(), authName, data.getDenda().doubleValue(), 0.0, date);
                logsIncomeRepository.save(log);
                logService.saveLogs(LOAN, SUCCESS, PAY);
            } else {
                LogsIncome income = logList.get(0);
                Double total = income.getTotal();
                //NOT YET USED SERVICE BUT MAYBE LATER WILL ADD SOME FEATURE ON IT
                if (income.getSource().equals("PENGELUARAN")) {
                    total = total - data.getDenda();
                    LogsIncome log = new LogsIncome(UUID.randomUUID().toString(), "PENGELUARAN ID :" + data.getId() + "/" + data.getCustomerNumber(), authName, data.getDenda().doubleValue(), total, date);
                    logsIncomeRepository.save(log);
                    logService.saveLogs(LOAN, SUCCESS, PAY);

                } else {
                    total = total + data.getDenda();
                    LogsIncome log = new LogsIncome(UUID.randomUUID().toString(), "PELUNASAN DENDA ID :" + data.getId() + "/" + data.getCustomerNumber(), authName, data.getDenda().doubleValue(), total, date);
                    logsIncomeRepository.save(log);
                    logService.saveLogs(LOAN, SUCCESS, PAY);
                }
                data.setDenda(0L);
                loanRepository.save(data);
            }

        } catch (Exception e) {
            logService.saveLogs(LOAN, e.getMessage(), PAY);
        }

    }

    @Override
    public void extendLoan(Loan data) {
        try {
            loanRepository.save(data);
            logService.saveLogs(LOAN, SUCCESS, EXTEND);
        } catch (Exception e) {
            logService.saveLogs(LOAN, e.getMessage(), EXTEND);
        }
    }

    @Override
    public List<LoanIndexDto> getOnDenda(Integer page) {
        Integer row = 5;
        Pageable paging = PageRequest.of(page - 1, row, Sort.by("id").descending());
        return loanRepository.getOnDenda(paging);
    }

    @Override
    public Long getCountPageDenda() {
        Integer row = 5;
        Double totalData = (double) loanRepository.getCountPageDenda();
        Long totaPage = (long) Math.ceil(totalData / row);
        return totaPage;
    }

    @Override
    public List<LogsIncome> getLoanPaymentHistory(Integer page) {

        Integer row = 5;
        Pageable paging = PageRequest.of(page - 1, row, Sort.by("transactionDate").descending());
        return logsIncomeRepository.getPageOnPaymentHistory(paging);
    }

    @Override
    public Long getCountPaymentHistory() {
        Integer row = 5;
        Double totalData = (double) logsIncomeRepository.getCountTotalPaymentHistory();
        Long totaPage = (long) Math.ceil(totalData / row);
        return totaPage;
    }

    @Override
    public List<LoanIndexDto> getListLoanHistoryBySearch(Integer page) {
        Integer row = 10;
        Pageable paging = PageRequest.of(page - 1, row, Sort.by("id").descending());
        return loanRepository.getListLoanHistoryBySearch(paging);
    }

    @Override
    public Long getCountHistoryPage() {
        Integer row = 10;
        Double totalData = (double) loanRepository.getCountHistoryPage();
        Long totaPage = (long) Math.ceil(totalData / row);

        return totaPage;
    }

}
