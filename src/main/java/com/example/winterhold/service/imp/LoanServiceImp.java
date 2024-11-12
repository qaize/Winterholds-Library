package com.example.winterhold.service.imp;

import com.example.winterhold.constants.ActionConstants;
import com.example.winterhold.controller.model.BaseController;
import com.example.winterhold.repository.*;
import com.example.winterhold.dto.book.BookUpdateDto;
import com.example.winterhold.dto.CurrentLoginDetailDTO;
import com.example.winterhold.dto.loan.*;
import com.example.winterhold.dto.models.DataDTO;
import com.example.winterhold.entity.*;
import com.example.winterhold.service.abs.LoanService;
import com.example.winterhold.service.abs.NotificationService;
import com.example.winterhold.utility.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class LoanServiceImp implements LoanService {

    private final LoanRepository loanRepository;
    private final LogsIncomeRepository logsIncomeRepository;
    private final LogServiceImpl logService;
    private final CustomerServiceImp customerServiceImp;
    private final CustomerRepository customerRepository;
    private final BookServiceImp bookService;
    private final LoanRequestRepository loanRequestRepository;
    private final BookRepository bookRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;


    @Override
    public DataDTO<List<LoanIndexDto>> getListLoanBySearch(Integer page, String title, String name) {
        int row = 5;
        int flag = 0;
        String message = "";
        try {

            Pageable paging = PageRequest.of(page - 1, row, Sort.by("id").descending());
            Page<LoanIndexDto> rawDataLoan = loanRepository.getListLoanBySearch(title, name, paging);

            if (rawDataLoan.getContent().isEmpty()) {
                flag = 1;
                message = ActionConstants.INDEX_EMPTY;
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

        String currentLogin = new BaseController().getCurrentLogin();
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

            if (data.getDenda() <= 0) {
                customer.setLoanCount(loanCountSetter(data.getCustomerNumber(), ActionConstants.RETURN));
            }
            Notification returnNotification = notificationService.sendNotification(UUID.randomUUID(), customer.getMembershipNumber(), "Returned by admin", book.getTitle().concat(": ".concat(book.getCategoryName())), LocalDateTime.now(), currentLogin);

            extendLoan(data);
            notificationRepository.save(returnNotification);
            bookService.update(book);
            customerServiceImp.updateWithEntity(customer);
            return true;
        }


        return false;
    }


    @Override
    public Long getCountPage(String title, String name) {
        Integer row = 5;
        Double totalData = (double) loanRepository.getCountPage(title, name);
        return (long) Math.ceil(totalData / row);
    }

    @Override
    public Long getCountDenda(Loan loan) {

        long fee;
        if (loan.getReturnDate() != null && loan.getDenda() != null) {
            fee = ChronoUnit.DAYS.between(loan.getDueDate(), loan.getReturnDate()) * 2000;
        } else {
            fee = ChronoUnit.DAYS.between(loan.getDueDate(), LocalDate.now()) * 2000;
            if (fee < 0) {
                fee = 0L;
            }
        }
        return fee;
    }

    @Override
    public void insert(LoanInsertDto newData) {
        try {
            String currentLogin = new BaseController().getCurrentLogin();
            // Update book and customer property during create new loan
            BookUpdateDto updateBook = updateBookProperty(newData.getBookCode());
            Customer updateCustomer = updateCustomerProperty(newData.getCustomerNumber());
            Loan insertNewLoan = mapInsert(newData);

            Notification insertNotification = notificationService.sendNotification(UUID.randomUUID(), updateCustomer.getMembershipNumber(), "New loan inserted by admin", updateBook.getTitle().concat(": ".concat(updateBook.getCategoryName())), LocalDateTime.now(), currentLogin);

            chainUpdateLoan(updateCustomer, updateBook, insertNewLoan);
            log.info(ActionConstants.SUCCESS_INSERT_DATA, insertNewLoan.getId());
            logService.saveLogs(ActionConstants.LOAN, ActionConstants.SUCCESS, ActionConstants.INSERT);
            notificationRepository.save(insertNotification);
        } catch (Exception e) {
            log.error(e.getMessage());
            logService.saveLogs(ActionConstants.LOAN, ActionConstants.FAILED, ActionConstants.INSERT);
        }
    }


    @Override
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElseThrow(() -> new RuntimeException("NOT_FOUND"));
    }

    @Override
    public List<LoanIndexDto> getAll() {
        return loanRepository.getAll();
    }

    @Override
    public void delete(Long id) {
        try {
            loanRepository.deleteById(id);
            logService.saveLogs(ActionConstants.LOAN, ActionConstants.SUCCESS, ActionConstants.DELETE);
        } catch (Exception e) {
            logService.saveLogs(ActionConstants.LOAN, e.getMessage(), ActionConstants.DELETE);
        }
    }

    //    UNUSED SERVICES
    @Override
    public void update(LoanUpdateDto dto) {
        Loan en = new Loan(dto.getId(), dto.getCustomerNumber(), dto.getBookCode(),
                dto.getLoanDate(), dto.getDueDate(), dto.getReturnDate(), dto.getNote(),
                0, dto.getDenda());
        loanRepository.save(en);
    }


    @Override
    public void payment(Long id) {

        try {
            recordPaymentHistory(id);
        } catch (Exception e) {
            log.error("ERROR IN PAYMENT", e);
            logService.saveLogs(ActionConstants.LOAN, e.getMessage(), ActionConstants.PAY);
        }
    }

    private void recordPaymentHistory(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLogin = authentication.getName();

        Timestamp date = Timestamp.from(Instant.now());
        Loan loanDataToUpdate = loanRepository.findById(id).orElseThrow();
        LogsIncome previousLogs = logsIncomeRepository.findAll(Sort.by("transactionDate").descending()).get(0);
        Customer updateCustomer = customerServiceImp.getCustomerByEntity(loanDataToUpdate.getCustomerNumber());
        String transactionID = CommonUtil.generateTransactionId(String.valueOf(loanDataToUpdate.getId()),updateCustomer.getMembershipNumber());

        if (previousLogs == null) {
            // First time logs
            LogsIncome log = new LogsIncome(UUID.randomUUID().toString(),transactionID, currentLogin, loanDataToUpdate.getDenda().doubleValue(), loanDataToUpdate.getDenda().doubleValue(), date);
            logsIncomeRepository.save(log);
            logService.saveLogs(ActionConstants.LOAN, ActionConstants.SUCCESS, ActionConstants.PAY);
        } else {
            LogsIncome incomingPaymentLogs = new LogsIncome(UUID.randomUUID().toString(), transactionID, currentLogin, loanDataToUpdate.getDenda().doubleValue(), addNewPayment(previousLogs.getTotal(), loanDataToUpdate.getDenda()), date);
            loanDataToUpdate.setDenda(0L);
            updateCustomer.setLoanCount(loanCountSetter(updateCustomer.getMembershipNumber(), "Return"));

            // Update Customer and loan status then save it into log income table
            updateLogsIncome(incomingPaymentLogs, updateCustomer, loanDataToUpdate);
        }
    }


    private Integer loanCountSetter(String customer, String flags) {
        Integer data = customerRepository.getLoanCountCurrentCustomer(customer);
        if (flags.equals("Return")) {
            if (data != 0) {
                data = data - 1;
            }
        } else {
            data = data + 1;
        }
        return data;
    }


    @Override
    public void extendLoan(Loan data) {
        try {
            loanRepository.save(data);
            logService.saveLogs(ActionConstants.LOAN, ActionConstants.SUCCESS, ActionConstants.EXTEND);
        } catch (Exception e) {
            logService.saveLogs(ActionConstants.LOAN, e.getMessage(), ActionConstants.EXTEND);
        }
    }

    @Override
    public List<LoanIndexDto> getOnDenda(Integer page) {
        int row = 5;
        Pageable paging = PageRequest.of(page - 1, row, Sort.by("id").descending());
        return loanRepository.getOnDenda(paging);
    }

    @Override
    public Long getCountPageDenda() {
        Integer row = 5;
        Double totalData = (double) loanRepository.getCountPageDenda();
        return (long) Math.ceil(totalData / row);
    }

    @Override
    public List<LogsIncome> getLoanPaymentHistory(Integer page) {

        int row = 5;
        Pageable paging = PageRequest.of(page - 1, row, Sort.by("transactionDate").descending());
        return logsIncomeRepository.getPageOnPaymentHistory(paging);
    }

    @Override
    public Long getCountPaymentHistory() {
        Integer row = 5;
        Double totalData = (double) logsIncomeRepository.getCountTotalPaymentHistory();
        return (long) Math.ceil(totalData / row);
    }

    @Override
    public List<LoanIndexDto> getListLoanHistoryBySearch(Integer page) {
        int row = 10;
        Pageable paging = PageRequest.of(page - 1, row, Sort.by("returnDate").descending());
        return loanRepository.getListLoanHistoryBySearch(paging);
    }

    @Override
    public Long getCountHistoryPage() {
        Integer row = 10;
        Double totalData = (double) loanRepository.getCountHistoryPage();
        return  (long) Math.ceil(totalData / row);
    }

    @Transactional
    @Override
    public DataDTO<Boolean> newLoanRequest(RequestLoanDTO requestNew) {
        String currentLogin = new BaseController().getCurrentLogin();

        String message = "";
        int flag = 0;
        try {
            boolean dataReturn = true;
            //            check books if available
            if (bookRepository.validateAvailableBookByBookCode(requestNew.getBookCode()) == 1) {
                dataReturn = false;
                message = "Books is out of stock";
                flag = 1;
            }
            //            Check Customer if not banned/expire/denda/
            if (customerRepository.validateAvailableCustomerByMembershipNumber(requestNew.getMembershipNumber()) == 0) {
                dataReturn = false;
                message = "This Customer cannot request the books, please contact admin!";
                flag = 1;
            }
            //Customer if already loan the books
            if (loanRepository.validateReplicateBookLoan(requestNew.getMembershipNumber(), requestNew.getBookCode()) == 1) {
                dataReturn = false;
                message = "this user already loan this books!";
                flag = 1;
            }
            //Customer if already request the books
            if (loanRequestRepository.validateRepicateRequest(requestNew.getMembershipNumber(), requestNew.getBookCode()) == 1) {
                dataReturn = false;
                message = "You already request this books!";
                flag = 1;
            }


            if (dataReturn) {
                Customer customer = customerRepository.findById(requestNew.getMembershipNumber()).orElseThrow();
                List<RequestLoan> loanList = loanRequestRepository.findAll(Sort.by("id").descending());
                RequestLoan requestNewLoan;
                if (loanList.isEmpty()) {

                    requestNewLoan = new RequestLoan(1L,
                            requestNew.getMembershipNumber(),
                            requestNew.getBookCode(),
                            LocalDateTime.now(), false, true);
                    customer.setRequestCount(customer.getRequestCount() + 1);
                } else {
                    requestNewLoan = new RequestLoan(loanList.get(0).getId() + 1L,
                            requestNew.getMembershipNumber(),
                            requestNew.getBookCode(),
                            LocalDateTime.now(), false, true);
                    customer.setRequestCount(customer.getRequestCount() + 1);
                }
                Notification userNotification = notificationService.sendNotification(UUID.randomUUID(), "admin", "Request new loan", requestNewLoan.getBookCode().concat(" by ").concat(requestNew.getMembershipNumber()), LocalDateTime.now(), currentLogin);

                notificationRepository.save(userNotification);
                customerRepository.save(customer);
                loanRequestRepository.save(requestNewLoan);
            }
            return DataDTO.<Boolean>builder()
                    .data(dataReturn)
                    .message(message)
                    .flag(flag)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DataDTO.<Boolean>builder()
                    .data(false)
                    .message(e.getMessage())
                    .flag(1)
                    .build();
        }
    }

    @Override
    public DataDTO<List<RequestLoanIndexDTO>> fetchLoanRequest(CurrentLoginDetailDTO currentLogin, Integer page) {
        int flag = 0;
        String message = "";
        try {

            Pageable pages = PageRequest.of(page - 1, 5, Sort.by("requestDate").descending());
            Page<RequestLoanIndexDTO> fetchedData;
            if (currentLogin.getRole().contains("customer")) {
                fetchedData = loanRequestRepository.findRequestLoanById(currentLogin.getUsername(), pages);
            } else {
                fetchedData = loanRequestRepository.findAllRequestLoan(pages);
            }

            if (fetchedData.getContent().isEmpty()) {
                flag = 1;
                message = ActionConstants.INDEX_EMPTY;
            }

            return DataDTO.<List<RequestLoanIndexDTO>>builder()
                    .totalPage((long) fetchedData.getTotalPages())
                    .flag(flag)
                    .message(message)
                    .data(fetchedData.getContent())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DataDTO.<List<RequestLoanIndexDTO>>builder()
                    .message(e.getMessage())
                    .data(new ArrayList<>())
                    .totalPage(0L)
                    .build();
        }
    }


    @Override
    public DataDTO<Boolean> cancelLoan(Long id) {

        String currentLogin = new BaseController().getCurrentLogin();

        boolean returnData = true;
        int flag = 0;
        String message = "";
        try {

            RequestLoan data = loanRequestRepository.findById(id).orElseThrow();
            Customer updateCustomer = customerRepository.findById(data.getMembershipNumber()).orElseThrow();
            Notification deleteNotification;
            if (data.isStatus()) {
                flag = 1;
                message = "This request already accepted";
                returnData = false;
            } else {
                if (updateCustomer.getRequestCount() > 0) {
                    updateCustomer.setRequestCount(updateCustomer.getRequestCount() - 1);
                }
                deleteNotification = notificationService.sendNotification(UUID.randomUUID(), updateCustomer.getMembershipNumber(), "Request canceled", "", LocalDateTime.now(), currentLogin);
                data.setIsActive(false);
                notificationRepository.save(deleteNotification);
            }

            loanRequestRepository.save(data);

            return DataDTO.<Boolean>builder()
                    .message(message)
                    .flag(flag)
                    .data(returnData)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }


    @Override
    public DataDTO<List<LoanIndexDto>> getListLoanByMembershipNumber(String username) {
        String message = "";
        int flag = 1;
        try {
            Pageable pages = PageRequest.of(0, 3, Sort.by("id").descending());
            Page<LoanIndexDto> data = loanRepository.getCustomerOnLoan(username, pages);
            List<LoanIndexDto> mappedData;

            if (data.getContent().isEmpty()) {
                message = ActionConstants.INDEX_EMPTY;
                flag = 0;
                mappedData = new ArrayList<>();
            } else {
                mappedData = mapIndexLoan(data.getContent());
            }

            return DataDTO.<List<LoanIndexDto>>builder()
                    .data(mappedData)
                    .flag(flag)
                    .message(message)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public DataDTO<Boolean> insertByRequestId(Long id) {

        BaseController currentLogin = new BaseController();

        boolean returnData = true;
        String message = "";
        int flag = 0;
        try {
            RequestLoan requestLoanData = loanRequestRepository.findById(id).orElseThrow();

            if (customerRepository.validateAvailableCustomerByMembershipNumber(requestLoanData.getMembershipNumber()) == 0) {
                returnData = false;
                message = "this customer unable to loan, please contact admin!";
                flag = 1;
            }

            if (loanRepository.validateReplicateBookLoan(requestLoanData.getMembershipNumber(), requestLoanData.getBookCode()) == 1) {
                returnData = false;
                message = "this user already loan this books!";
                flag = 1;
            }

            if (returnData) {

                BookUpdateDto updateBook = updateBookProperty(requestLoanData.getBookCode());
                Customer updateCustomer = updateCustomerProperty(requestLoanData.getMembershipNumber());
                updateCustomer.setRequestCount(updateCustomer.getRequestCount() - 1);

                Long lastIdLoan = loanRepository.findLatestData().get(0).getId();
                Loan requestedLoan = new Loan(lastIdLoan + 1, requestLoanData.getMembershipNumber(), requestLoanData.getBookCode(), LocalDate.now(), LocalDate.now().plusDays(5), null, "Order By Request", 0, 0L);
                requestLoanData.setStatus(true);
                requestLoanData.setIsActive(false);
                Notification sendNotification = notificationService.sendNotification(UUID.randomUUID(), updateCustomer.getMembershipNumber(), "Request Loan Accepted", "Thank you for Loan", LocalDateTime.now(), currentLogin.getCurrentLogin());
                chainUpdateLoan(updateCustomer, updateBook, requestedLoan);
                notificationRepository.save(sendNotification);
                loanRequestRepository.save(requestLoanData);
            }

            return DataDTO.<Boolean>builder()
                    .data(returnData)
                    .message(message)
                    .flag(flag)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return DataDTO.<Boolean>builder().build();
        }
    }



    private List<LoanIndexDto> mapIndexLoan(List<LoanIndexDto> data) {
        for (LoanIndexDto loanDetails : data) {

            if (loanDetails.getReturnDate() != null) {

                loanDetails.setDayLeft(loanDetails.getReturnDate().isBefore(loanDetails.getDueDate()) ? "On Time" : "Late");
                loanDetails.setLoanStatus("Returned");
            } else if (loanDetails.getDueDate().equals(LocalDate.now())) {
                loanDetails.setDayLeft("Last day");
                loanDetails.setLoanStatus("On Loan");
            } else {
                long dif = ChronoUnit.DAYS.between(LocalDate.now(), loanDetails.getDueDate());
                loanDetails.setDayLeft(dif > 0 ? Long.toString(dif) : "Late");
                loanDetails.setLoanStatus("On Loan");
            }
        }
        return data;
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
            logService.saveLogs(ActionConstants.LOAN, ActionConstants.SUCCESS, ActionConstants.INSERT);
        } else {
            newDataLoan = new Loan(newData.getId(), newData.getCustomerNumber(), newData.getBookCode(), LocalDate.now(), LocalDate.now().plusDays(5), null, newData.getNote(), 0, newData.getDenda());
            logService.saveLogs(ActionConstants.LOAN, ActionConstants.SUCCESS, ActionConstants.INSERT);
        }
        return newDataLoan;
    }


    private Customer updateCustomerProperty(String membershipNumber) {
        Customer updatedCustomer = customerServiceImp.getCustomerByEntity(membershipNumber);

        updatedCustomer.setLoanCount(loanCountSetter(membershipNumber, "loan"));
        return updatedCustomer;
    }

    private BookUpdateDto updateBookProperty(String bookCode) {

        BookUpdateDto updatedBook = bookService.getBooksById(bookCode);

        if (updatedBook.getQuantity() > updatedBook.getInBorrow()) {
            updatedBook.setInBorrow(updatedBook.getInBorrow() + 1);
        }

        if (updatedBook.getQuantity().equals(updatedBook.getInBorrow())) {
            updatedBook.setIsBorrowed(true);
        }

        return updatedBook;
    }


    private Double addNewPayment(Double total, Long denda) {
        return total + denda;
    }

    private void updateLogsIncome(LogsIncome newLog, Customer updateCustomer, Loan updateLoan) {
        logsIncomeRepository.save(newLog);
        logService.saveLogs(ActionConstants.LOAN, ActionConstants.SUCCESS, ActionConstants.PAY);
        customerRepository.save(updateCustomer);
        loanRepository.save(updateLoan);
    }

}
