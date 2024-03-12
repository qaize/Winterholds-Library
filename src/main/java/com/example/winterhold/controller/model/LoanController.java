package com.example.winterhold.controller.model;

import com.example.winterhold.Dto.CurrentLoginDetailDTO;
import com.example.winterhold.Dto.Loan.*;
import com.example.winterhold.Dto.Models.DataDTO;
import com.example.winterhold.Entity.Loan;
import com.example.winterhold.Entity.LogsIncome;
import com.example.winterhold.service.abs.*;
import com.example.winterhold.constants.ActionConstants;
import com.example.winterhold.constants.MvcRedirectConst;
import com.example.winterhold.constants.WinterholdConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.example.winterhold.Utility.Dropdown.dropdownBook;
import static com.example.winterhold.Utility.Dropdown.dropdownCustomer;

@Controller
@RequestMapping("/loan")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final CustomerService customerService;
    private final BookService bookService;
    private final CategoryService categoryService;
    private final NotificationService notificationService;

    @Value(ActionConstants.INSERT)
    private String insert;

    @GetMapping("/history")
    public String loanHistory(Model model, @RequestParam(defaultValue = "1") Integer page) {

        var listLoan = loanService.getListLoanHistoryBySearch(page);
        Long totalPage = loanService.getCountHistoryPage();

        for (LoanIndexDto val : listLoan
        ) {
            if (val.getReturnDate() != null) {
                val.setDayLeft(val.getReturnDate().isBefore(val.getDueDate()) ? "On Time" : "Late");
                val.setLoanStatus("Returned");
            } else {
                long dif = ChronoUnit.DAYS.between(LocalDate.now(), val.getDueDate());
                val.setDayLeft(dif > 0 ? String.valueOf(dif) : "Late");
                val.setLoanStatus("On Loan");
            }
        }

        if (totalPage == 0) {
            page = 0;
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("listLoan", listLoan);
        model.addAttribute("totalPage", totalPage);

        return "Loan/LoanHistory";
    }

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(defaultValue = "") String title,
                        @RequestParam(defaultValue = "") String name,
                        @RequestParam(defaultValue = "1") Integer page) {

        DataDTO<List<LoanIndexDto>> index = loanService.getListLoanBySearch(page, title, name);

        model.addAttribute("name", name);
        model.addAttribute("flag", index.getFlag());
        model.addAttribute("empty",index.getMessage());
        model.addAttribute("title", title);
        model.addAttribute("currentPage", page);
        model.addAttribute("listLoan", index.getData());
        model.addAttribute("totalPage", index.getTotalPage());
        model.addAttribute("newNotification",notificationService.getNotification());

        return "Loan/index";
    }

    @GetMapping("/insert")
    public String insert(Model model) {

        LoanInsertDto dto = new LoanInsertDto();
         

        var getCustomer = customerService.getAvaliableCustomer();
        var getBook = bookService.getAvailableBook();
        var dropdownCustomer = dropdownCustomer(getCustomer);
        var dropdownBook = dropdownBook(getBook);

        model.addAttribute("dropdownCustomer", dropdownCustomer);
        model.addAttribute("dropdownBook", dropdownBook);
        model.addAttribute("dto", dto);
        model.addAttribute(ActionConstants.INSERT, insert);

        return "Loan/insert";
    }

    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("dto") LoanInsertDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {

             
            var getCustomer = customerService.getAvaliableCustomer();
            var getBook = bookService.getAvailableBook();
            var dropdownCustomer = dropdownCustomer(getCustomer);
            var dropdownBook = dropdownBook(getBook);

            model.addAttribute("dropdownCustomer", dropdownCustomer);
            model.addAttribute("dropdownBook", dropdownBook);
            model.addAttribute("dto", dto);
            model.addAttribute(ActionConstants.INSERT, insert);

            return "Loan/insert";
        } else {

            loanService.insert(dto);

            return MvcRedirectConst.REDIRECT_LOAN_INDEX;
        }
    }

    @GetMapping("/return")
    public String ret(@RequestParam Long id) {
        return loanService.returnBook(id) ? MvcRedirectConst.REDIRECT_LOAN_INDEX : "Loan/valid";
    }


    @GetMapping("/detail")
    public String detail(Model model, @RequestParam Long id) {

        var loanDto = loanService.getLoanById(id);
        var books = bookService.getBooksBycode(loanDto.getBookCode());
        var customer = customerService.getCustomerByMember(loanDto.getCustomerNumber());
        var category = categoryService.getCategoryByCategoryName(books.getCategoryName());
        var denda = loanService.getCountDenda(loanDto);

        model.addAttribute("books", books);
        model.addAttribute("historyDenda", denda);
        model.addAttribute("authorId", id);
        model.addAttribute("category", category);
        model.addAttribute("customer", customer);
        model.addAttribute("loanDto", loanDto);

        return "Loan/detail";
    }

    //NOT USED
    @GetMapping("/update")
    public String update(Model model, @RequestParam Long id) {

        String update = "update";
        Loan dto = loanService.getLoanById(id);
        var books = bookService.getBooksById(dto.getBookCode());

        if (dto.getReturnDate() == null) {

            books.setIsBorrowed(!books.getIsBorrowed());
            bookService.update(books);
            var getCustomer = customerService.getAvaliableCustomerEdit(dto.getCustomerNumber());
            var getBook = bookService.getAvailableBook();
            var dropdownCustomer = dropdownCustomer(getCustomer);
            var dropdownBook = dropdownBook(getBook);

            model.addAttribute("dropdownCustomer", dropdownCustomer);
            model.addAttribute("dropdownBook", dropdownBook);
            model.addAttribute("dto", dto);
            model.addAttribute("update", update);
            return "Loan/insert";
        } else {
            return MvcRedirectConst.REDIRECT_LOAN_INDEX;
        }
    }


    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("dto") LoanUpdateDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {

            String update = "update";
            var getBook = bookService.getAvailableBook();
            var getCustomer = customerService.getAvaliableCustomerEdit(dto.getCustomerNumber());
            var dropdownCustomer = dropdownCustomer(getCustomer);
            var dropdownBook = dropdownBook(getBook);

            model.addAttribute("dropdownCustomer", dropdownCustomer);
            model.addAttribute("dropdownBook", dropdownBook);
            model.addAttribute("dto", dto);
            model.addAttribute("update", update);
            return "Loan/insert";
        } else {
            var books = bookService.getBooksById(dto.getBookCode());
            books.setIsBorrowed(!books.getIsBorrowed());
            bookService.update(books);
            loanService.update(dto);
            return MvcRedirectConst.REDIRECT_LOAN_INDEX;
        }

    }


    @GetMapping("/denda")
    public String denda(Model model, @RequestParam(defaultValue = "1") Integer page) {
        int flag = 0;
        List<LoanIndexDto> loanDto = loanService.getOnDenda(page);
        Long totalPage = loanService.getCountPageDenda();
        if (totalPage == 0) {
            page = 0;
        }

        if (loanDto.isEmpty()) {
            flag = 1;
            model.addAttribute("empty", "Seems there is mosquito flying around");
        }
        model.addAttribute("flag", flag);
        model.addAttribute("dataDenda", loanDto);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", page);

        return "Loan/denda";
    }

    @GetMapping("/payment")
    public String payment(Model model, @RequestParam Long id) {

        loanService.payment(id);

        return "redirect:/loan/denda";
    }

    @GetMapping("/extend")
    public String extend(Model model, @RequestParam Long id) {

        Loan data = loanService.getLoanById(id);


        if (LocalDate.now().isAfter(data.getDueDate())) {
            model.addAttribute(WinterholdConstants.CONTROLLER_VALIDATION_HEADER, "Unable to Extend");
            model.addAttribute(WinterholdConstants.CONTROLLER_VALIDATION_REASON, "User was late, please return the book and loan again");

            return "Loan/valid";
        } else {

            if (data.getExtend() < 4) {

                Integer counter = data.getExtend();
                data.setExtend(counter + 1);
                data.setDueDate(data.getDueDate().plusDays(2));
                loanService.extendLoan(data);

                return MvcRedirectConst.REDIRECT_LOAN_INDEX;
            } else {

                model.addAttribute(WinterholdConstants.CONTROLLER_VALIDATION_HEADER, "Unable to Extend");
                model.addAttribute(WinterholdConstants.CONTROLLER_VALIDATION_REASON, "User was reached maximum extendable");

                return "Loan/valid";
            }
        }

    }

    @GetMapping("/paymentHistory")
    public String paymentHistory(Model model, @RequestParam(defaultValue = "1") Integer page) {

        List<LogsIncome> logs = loanService.getLoanPaymentHistory(page);
        Long totalPage = loanService.getCountPaymentHistory();
        if (totalPage == 0) {
            page = 0;
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("logs", logs);

        return "Loan/paymentHistory";
    }


    @GetMapping("/request-loan")
    public String requestLoan(
            Model model,
            @RequestParam String bookCode
    ){
        BaseController baseController = new BaseController();
        String currentLogin = baseController.getCurrentLogin();
        RequestLoanDTO requestNew = new RequestLoanDTO(currentLogin,bookCode);
        DataDTO<Boolean> newLoanReq = loanService.newLoanRequest(requestNew);

        model.addAttribute(WinterholdConstants.CONTROLLER_VALIDATION_HEADER,"Unable To Request");
        model.addAttribute(WinterholdConstants.CONTROLLER_VALIDATION_REASON,newLoanReq.getMessage());
        model.addAttribute("flag",newLoanReq.getFlag());
        return Boolean.TRUE.equals(newLoanReq.getData()) ? "redirect:/loan/request-loan-list" : "Loan/valid";
    }


    @GetMapping("/request-loan-list")
    public String requestLoanList(Model model,@RequestParam(defaultValue = "1") Integer page ){
        BaseController baseController = new BaseController();
        CurrentLoginDetailDTO currentLogin = baseController.getCurrentLoginDetail();

        DataDTO<List<RequestLoanIndexDTO>> data = loanService.fetchLoanRequest(currentLogin,page);
        DataDTO<List<LoanIndexDto>> dataLoan = loanService.getListLoanByMembershipNumber(currentLogin.getUsername());

        model.addAttribute("flag",data.getFlag());
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPage",data.getTotalPage());
        model.addAttribute("message",data.getMessage());
        model.addAttribute("list",data.getData());
        model.addAttribute("listLoan",dataLoan.getData());
        model.addAttribute("newNotification",notificationService.getNotification());
        return "Loan/RequestList";
    }

    @GetMapping("/insert-by-request")
    public String insertFromRequest(Model model, @RequestParam Long id) {
        DataDTO<Boolean> data = loanService.insertByRequestId(id);

        model.addAttribute(WinterholdConstants.CONTROLLER_VALIDATION_HEADER,"Unable To Request");
        model.addAttribute(WinterholdConstants.CONTROLLER_VALIDATION_REASON,data.getMessage());
        model.addAttribute("flag",data.getFlag());
        return Boolean.TRUE.equals(data.getData()) ? MvcRedirectConst.REDIRECT_LOAN_INDEX : "Loan/valid";
    }


    @GetMapping(value = "/delete-request-loan")
    public String deleteRequest(Model model,@RequestParam Long id){
        DataDTO<Boolean> deleteRequest = loanService.cancelLoan(id);

        model.addAttribute(WinterholdConstants.CONTROLLER_VALIDATION_HEADER,"Unable To Request");
        model.addAttribute(WinterholdConstants.CONTROLLER_VALIDATION_REASON,deleteRequest.getMessage());
        model.addAttribute("flag",deleteRequest.getFlag());
        return Boolean.TRUE.equals(deleteRequest.getData()) ? "redirect:/loan/request-loan-list" : "Loan/valid";
    }

}


