package com.example.AppWinterhold.Controller.Model;

import com.example.AppWinterhold.Dao.LoanRepository;
import com.example.AppWinterhold.Dto.CurrentLoginDetailDTO;
import com.example.AppWinterhold.Dto.Loan.*;
import com.example.AppWinterhold.Dto.Models.DataDTO;
import com.example.AppWinterhold.Entity.Loan;
import com.example.AppWinterhold.Entity.LogsIncome;
import com.example.AppWinterhold.Entity.RequestLoan;
import com.example.AppWinterhold.Service.abs.BookService;
import com.example.AppWinterhold.Service.abs.CategoryService;
import com.example.AppWinterhold.Service.abs.CustomerService;
import com.example.AppWinterhold.Service.abs.LoanService;
import com.example.AppWinterhold.Service.imp.AccountServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.example.AppWinterhold.Utility.Dropdown.dropdownBook;
import static com.example.AppWinterhold.Utility.Dropdown.dropdownCustomer;
import static com.example.AppWinterhold.Const.actionConst.INDEX_EMPTY;

@Controller
@RequestMapping("/loan")
public class LoanController {

    private LoanService loanService;
    private CustomerService customerService;
    private BookService bookService;
    private CategoryService categoryService;
    private LoanRepository loanRepository;
    private AccountServiceImp account;

    @Autowired
    public LoanController(LoanService loanService, CustomerService customerService, BookService bookService, CategoryService categoryService, LoanRepository loanRepository, AccountServiceImp account) {
        this.loanService = loanService;
        this.customerService = customerService;
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.loanRepository = loanRepository;
        this.account = account;
    }

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
                Long dif = ChronoUnit.DAYS.between(LocalDate.now(), val.getDueDate());
                val.setDayLeft(dif > 0 ? dif.toString() : "Late");
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

        return "Loan/index";
    }


    @GetMapping("/insert-by-request")
    public String insertFromRequest(@RequestParam Long id) {
        DataDTO<Boolean> data = loanService.insertByRequestId(id);
        return data.getData() ? "redirect:/loan/index" : "Loan/valid";
    }

    @GetMapping("/insert")
    public String insert(Model model) {

        LoanInsertDto dto = new LoanInsertDto();
        String insert = "insert";

        var getCustomer = customerService.getAvaliableCustomer();
        var getBook = bookService.getAvailableBook();
        var dropdownCustomer = dropdownCustomer(getCustomer);
        var dropdownBook = dropdownBook(getBook);

        model.addAttribute("dropdownCustomer", dropdownCustomer);
        model.addAttribute("dropdownBook", dropdownBook);
        model.addAttribute("dto", dto);
        model.addAttribute("insert", insert);

        return "Loan/insert";
    }

    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("dto") LoanInsertDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {

            String insert = "insert";
            var getCustomer = customerService.getAvaliableCustomer();
            var getBook = bookService.getAvailableBook();
            var dropdownCustomer = dropdownCustomer(getCustomer);
            var dropdownBook = dropdownBook(getBook);

            model.addAttribute("dropdownCustomer", dropdownCustomer);
            model.addAttribute("dropdownBook", dropdownBook);
            model.addAttribute("dto", dto);
            model.addAttribute("insert", insert);

            return "Loan/insert";
        } else {

            loanService.insert(dto);

            return "redirect:/loan/index";
        }
    }

    @GetMapping("/return")
    public String ret(@RequestParam Long id) {
        return loanService.returnBook(id) ? "redirect:/loan/index" : "Loan/valid";
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

        return "loan/detail";
    }

    //NOT USED
    @GetMapping("/update")
    public String update(Model model, @RequestParam(required = true) Long id) {

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
            return "redirect:/loan/index";
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
//            var extend = loanRepository.getExtendById(dto.getId());
            loanService.update(dto);
            return "redirect:/loan/index";
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

        return "loan/denda";
    }

    @GetMapping("/payment")
    public String payment(Model model, @RequestParam(required = true) Long id) {

        loanService.goPayOff(id);

        return "redirect:/loan/denda";
    }

    @GetMapping("/extend")
    public String extend(Model model, @RequestParam Long id) {

        Loan data = loanService.getLoanById(id);


        if (LocalDate.now().isAfter(data.getDueDate())) {
            model.addAttribute("validationHeader", "Unable to Extend");
            model.addAttribute("validationReason", "User was late, please return the book and loan again");

            return "loan/valid";
        } else {

            if (data.getExtend() < 4) {

                Integer counter = data.getExtend();
                data.setExtend(counter + 1);
                data.setDueDate(data.getDueDate().plusDays(2));
                loanService.extendLoan(data);

                return "redirect:/loan/index";
            } else {

                model.addAttribute("validationHeader", "Unable to Extend");
                model.addAttribute("validationReason", "User was reached maximum extendable");

                return "loan/valid";
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

        return "loan/paymentHistory";
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

        model.addAttribute("validationHeader","Unable To Request");
        model.addAttribute("validationReason",newLoanReq.getMessage());
        model.addAttribute("flag",newLoanReq.getFlag());
        return newLoanReq.getData() ? "redirect:/loan/request-loan-list" : "Loan/valid";
    }


    @GetMapping("/request-loan-list")
    public String requestLoanList(Model model,@RequestParam(defaultValue = "1") Integer page ){
        BaseController baseController = new BaseController();
        CurrentLoginDetailDTO currentLogin = baseController.getCurrentLoginDetail();

        DataDTO<List<RequestLoanIndexDTO>> data = loanService.getRequestLoanByCurrentLogin(currentLogin,page);
        DataDTO<List<LoanIndexDto>> dataLoan = loanService.getListLoanByMembershipNumber(currentLogin.getUsername());

        model.addAttribute("flag",data.getFlag());
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPage",data.getTotalPage());
        model.addAttribute("message",data.getMessage());
        model.addAttribute("list",data.getData());
        model.addAttribute("listLoan",dataLoan.getData());
        return "Loan/RequestList";
    }

}


