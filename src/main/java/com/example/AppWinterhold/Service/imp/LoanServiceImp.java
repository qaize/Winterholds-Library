package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Dao.CustomerRepository;
import com.example.AppWinterhold.Dao.LoanRepository;
import com.example.AppWinterhold.Dao.LoanRequestRepository;
import com.example.AppWinterhold.Dao.LogsIncomeRepository;
import com.example.AppWinterhold.Dto.Book.BookUpdateDto;
import com.example.AppWinterhold.Dto.Loan.*;
import com.example.AppWinterhold.Dto.Models.DataDTO;
import com.example.AppWinterhold.Entity.Customer;
import com.example.AppWinterhold.Entity.Loan;
import com.example.AppWinterhold.Entity.LogsIncome;
import com.example.AppWinterhold.Entity.RequestLoan;
import com.example.AppWinterhold.Service.abs.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.AppWinterhold.Const.actionConst.*;


@Service
public class LoanServiceImp implements LoanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoanServiceImp.class);

    private LoanRepository loanRepository;
    private LogsIncomeRepository logsIncomeRepository;
    private LogServiceImpl logService;
    private CustomerServiceImp customerServiceImp;
    private CustomerRepository customerRepository;
    private BookServiceImp bookService;
    private LoanRequestRepository loanRequestRepository;

    @Autowired
    public LoanServiceImp(LoanRepository loanRepository, LogsIncomeRepository logsIncomeRepository, LogServiceImpl logService, CustomerServiceImp customerServiceImp,
                          CustomerRepository customerRepository, BookServiceImp bookService, LoanRequestRepository loanRequestRepository) {
        this.loanRepository = loanRepository;
        this.logsIncomeRepository = logsIncomeRepository;
        this.logService = logService;
        this.customerServiceImp = customerServiceImp;
        this.customerRepository = customerRepository;
        this.bookService = bookService;
        this.loanRequestRepository = loanRequestRepository;
    }


    @Override
    public DataDTO<List<LoanIndexDto>> getListLoanBySearch(Integer page, String title, String name) {
        Integer row = 5;
        int flag = 0;
        String message = "";
        try {

            Pageable paging = PageRequest.of(page - 1, row, Sort.by("id").descending());
            Page<LoanIndexDto> rawDataLoan = loanRepository.getListLoanBySearch(title, name, paging);

            if (rawDataLoan.getContent().isEmpty()) {
                flag = 1;
                message = INDEX_EMPTY;
            }

            return DataDTO.<List<LoanIndexDto>>builder()
                    .flag(flag)
                    .data(mapIndexLoan(rawDataLoan.getContent()))
                    .message(message)
                    .totalPage((long) rawDataLoan.getTotalPages())
                    .build();
        } catch (Exception e) {
            return DataDTO.<List<LoanIndexDto>>builder()
                    .data(new ArrayList<>())
                    .totalPage(0L)
                    .build();
        }
    }


    @Override
    public boolean returnBook(Long id) {


        var data = getLoanById(id);
        var book = bookService.getBooksById(data.getBookCode());
        var customer = customerServiceImp.getCustomerByEntity(data.getCustomerNumber());

        if (data.getReturnDate() == null) {
            book.setInBorrow(book.getInBorrow() - 1);
            data.setReturnDate(LocalDate.now());
            data.setDenda(getCountDenda(data));

            if (book.getQuantity() > book.getInBorrow()) {
                book.setIsBorrowed(false);
            }

            if (!(data.getDenda() > 0)) {
                customer.setLoanCount(customerServiceImp.loanCountSetter(data.getCustomerNumber(), "Return"));
            }

            extendLoan(data);
            bookService.update(book);
            customerServiceImp.updateWithEntity(customer);
            return true;
        }


        return false;
    }

    @Override
    public boolean newLoanRequest(RequestLoanDTO requestNew) {
        try {
            List<RequestLoan> initalData = loanRequestRepository.findAll(Sort.by("id").descending());
            RequestLoan requestNewLoan;
            if (initalData.isEmpty()) {
                requestNewLoan = new RequestLoan(1L,
                        requestNew.getMembershipNumber(),
                        requestNew.getBookCode(),
                        LocalDateTime.now(), false);
                loanRequestRepository.save(requestNewLoan);
            }else {
                 requestNewLoan = new RequestLoan(initalData.get(0).getId()+1L,
                        requestNew.getMembershipNumber(),
                        requestNew.getBookCode(),
                        LocalDateTime.now(), false);
                loanRequestRepository.save(requestNewLoan);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    @Override
    public DataDTO<List<RequestLoanIndexDTO>> getRequestLoanByCurrentLogin(String currentLogin, Integer page) {
        int flag = 0;
        String message = "";
        try {

            Pageable pages = PageRequest.of(page - 1, 5, Sort.by("requestDate").descending());
            Page<RequestLoanIndexDTO> fetchedData = loanRequestRepository.findRequestLoanById(currentLogin, pages);

            if (fetchedData.getContent().isEmpty()) {
                flag = 1;
                message = INDEX_EMPTY;
            }

            return DataDTO.<List<RequestLoanIndexDTO>>builder()
                    .totalPage((long) fetchedData.getTotalPages())
                    .flag(flag)
                    .message(message)
                    .data(fetchedData.getContent())
                    .build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return DataDTO.<List<RequestLoanIndexDTO>>builder()
                    .message(e.getMessage())
                    .data(new ArrayList<>())
                    .totalPage(0L)
                    .build();
        }
    }

    @Override
    public Long getCountPage(String title, String name) {
        Integer row = 5;
        Double totalData = (double) loanRepository.getCountPage(title, name);
        Long totaPage = (long) Math.ceil(totalData / row);

        return totaPage;
    }

    @Override
    public Long getCountDenda(Loan loan) {

        Long denda = 0L;
        if (loan.getReturnDate() != null && loan.getDenda() != null) {
            denda = ChronoUnit.DAYS.between(loan.getDueDate(), loan.getReturnDate()) * 2000;
        } else {
            denda = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now()) * 2000;
            if (denda < 0) {
                denda = 0L;
            }
        }
        return denda;
    }

    @Override
    public void insert(LoanInsertDto newData) {
        try {
            // Update book and customer property during create new loan
            BookUpdateDto updateBook = updateBookProperty(newData.getBookCode());
            Customer updateCustomer = updateCustomerProperty(newData.getCustomerNumber());
            Loan insertNewLoan = mapInsert(newData);

            chainUpdateLoan(updateCustomer, updateBook, insertNewLoan);
            LOGGER.info(SUCCESS_INSERT_DATA, insertNewLoan.getId());
            logService.saveLogs(LOAN, SUCCESS, INSERT);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            logService.saveLogs(LOAN, FAILED, INSERT);
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
    public void goPayOff(Long id) {

        try {
            recordPaymentHistory(id);
        } catch (Exception e) {
            logService.saveLogs(LOAN, e.getMessage(), PAY);
        }
    }

    private void recordPaymentHistory(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLogin = authentication.getName();

        Timestamp date = Timestamp.from(Instant.now());
        Loan loanDataToUpdate = loanRepository.findById(id).get();
        LogsIncome previousLogs = logsIncomeRepository.findAll(Sort.by("transactionDate").descending()).get(0);
        Customer updateCustomer = customerServiceImp.getCustomerByEntity(loanDataToUpdate.getCustomerNumber());

        if (previousLogs == null) {
            // First time logs
            LogsIncome log = new LogsIncome(UUID.randomUUID().toString(), "PELUNASAN DENDA ID :" + loanDataToUpdate.getId() + "/" + loanDataToUpdate.getCustomerNumber(), currentLogin, loanDataToUpdate.getDenda().doubleValue(), loanDataToUpdate.getDenda().doubleValue(), date);
            logsIncomeRepository.save(log);
            logService.saveLogs(LOAN, SUCCESS, PAY);
        } else {
            LogsIncome incomingPaymentLogs = new LogsIncome(UUID.randomUUID().toString(), "PELUNASAN DENDA ID :" + loanDataToUpdate.getId() + "/" + loanDataToUpdate.getCustomerNumber(), currentLogin, loanDataToUpdate.getDenda().doubleValue(), addNewPayment(previousLogs.getTotal(), loanDataToUpdate.getDenda()), date);
            loanDataToUpdate.setDenda(0L);
            updateCustomer.setLoanCount(customerServiceImp.loanCountSetter(updateCustomer.getMembershipNumber(), "Return"));

            // Update Customer and loan status then save it into log income table
            chainUpdateLogsIncome(incomingPaymentLogs, updateCustomer, loanDataToUpdate);
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
        Pageable paging = PageRequest.of(page - 1, row, Sort.by("returnDate").descending());
        return loanRepository.getListLoanHistoryBySearch(paging);
    }

    @Override
    public Long getCountHistoryPage() {
        Integer row = 10;
        Double totalData = (double) loanRepository.getCountHistoryPage();
        Long totaPage = (long) Math.ceil(totalData / row);

        return totaPage;
    }


    private List<LoanIndexDto> mapIndexLoan(List<LoanIndexDto> data) {
        List<LoanIndexDto> listLoanDetails = data;
        for (LoanIndexDto loanDetails : listLoanDetails) {

            if (loanDetails.getReturnDate() != null) {

                loanDetails.setDayLeft(loanDetails.getReturnDate().isBefore(loanDetails.getDueDate()) ? "On Time" : "Late");
                loanDetails.setLoanStatus("Returned");
            } else if (loanDetails.getDueDate().equals(LocalDate.now())) {
                loanDetails.setDayLeft("Last day");
                loanDetails.setLoanStatus("On Loan");
            } else {
                Long dif = ChronoUnit.DAYS.between(LocalDate.now(), loanDetails.getDueDate());
                loanDetails.setDayLeft(dif > 0 ? dif.toString() : "Late");
                loanDetails.setLoanStatus("On Loan");
            }
        }
        return listLoanDetails;
    }


    private void chainUpdateLoan(Customer customerData, BookUpdateDto bookData, Loan loanData) {
        customerServiceImp.updateWithEntity(customerData);
        bookService.update(bookData);
        loanRepository.save(loanData);
    }

    private Loan mapInsert(LoanInsertDto newData) {
        Loan newDataLoan;
        if (newData.getDenda() == null) {
            newDataLoan = new Loan(newData.getId(), newData.getCustomerNumber(), newData.getBookCode(), LocalDate.now(), LocalDate.now().plusDays(5), null, newData.getNote(), 0, null);
            logService.saveLogs(LOAN, SUCCESS, INSERT);
        } else {
            newDataLoan = new Loan(newData.getId(), newData.getCustomerNumber(), newData.getBookCode(), LocalDate.now(), LocalDate.now().plusDays(5), null, newData.getNote(), 0, newData.getDenda());
            logService.saveLogs(LOAN, SUCCESS, INSERT);
        }
        return newDataLoan;
    }


    private Customer updateCustomerProperty(String membershipNumber) {
        Customer updatedCustomer = customerServiceImp.getCustomerByEntity(membershipNumber);

        updatedCustomer.setLoanCount(customerServiceImp.loanCountSetter(membershipNumber, "loan"));
        return updatedCustomer;
    }

    private BookUpdateDto updateBookProperty(String bookCode) {

        BookUpdateDto updatedBook = bookService.getBooksById(bookCode);

        if (updatedBook.getQuantity() > updatedBook.getInBorrow()) {
            updatedBook.setInBorrow(updatedBook.getInBorrow() + 1);
        }

        if (updatedBook.getQuantity() == updatedBook.getInBorrow()) {
            updatedBook.setIsBorrowed(true);
        }

        return updatedBook;
    }


    private Double addNewPayment(Double total, Long denda) {
        return total + denda;
    }

    private void chainUpdateLogsIncome(LogsIncome newLog, Customer updateCustomer, Loan updateLoan) {
        logsIncomeRepository.save(newLog);
        logService.saveLogs(LOAN, SUCCESS, PAY);
        customerRepository.save(updateCustomer);
        loanRepository.save(updateLoan);
    }

}
