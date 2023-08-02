package com.example.AppWinterhold.Controller.Model;

import com.example.AppWinterhold.Dao.LoanRepository;
import com.example.AppWinterhold.Dto.Loan.LoanIndexDto;
import com.example.AppWinterhold.Dto.Loan.LoanInsertDto;
import com.example.AppWinterhold.Dto.Loan.LoanUpdateDto;
import com.example.AppWinterhold.Entity.Loan;
import com.example.AppWinterhold.Entity.LogsIncome;
import com.example.AppWinterhold.Service.abs.BookService;
import com.example.AppWinterhold.Service.abs.CategoryService;
import com.example.AppWinterhold.Service.abs.CustomerService;
import com.example.AppWinterhold.Service.abs.LoanService;
import com.example.AppWinterhold.Service.imp.AccountServiceImp;
import com.example.AppWinterhold.Utility.Dropdown;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

        model.addAttribute("userLogin", account.getCurrentUserLogin());


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

        model.addAttribute("listLoan", listLoan);
        if (totalPage == 0) {
            page = 0;
        }
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);

        return "Loan/LoanHistory";
    }

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(defaultValue = "") String title,
                        @RequestParam(defaultValue = "") String name,
                        @RequestParam(defaultValue = "1") Integer page) {

        model.addAttribute("userLogin", account.getCurrentUserLogin());

        model.addAttribute("name", name);
        model.addAttribute("title", title);
        var listLoan = loanService.getListLoanBySearch(page, title, name);
        Long totalPage = loanService.getCountPage(title, name);

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

        model.addAttribute("listLoan", listLoan);
        if (totalPage == 0) {
            page = 0;
        }
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);

        return "Loan/index";
    }

    @GetMapping("/insert")
    public String insert(Model model) {
        LoanInsertDto dto = new LoanInsertDto();

        String insert = "insert";
        var getCustomer = customerService.getAvaliableCustomer();
        var getBook = bookService.getAvailableBook();
        var dropdownCustomer = Dropdown.dropdownCustomer(getCustomer);
        var dropdownBook = Dropdown.dropdownBook(getBook);
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
            var dropdownCustomer = Dropdown.dropdownCustomer(getCustomer);
            var dropdownBook = Dropdown.dropdownBook(getBook);
            model.addAttribute("dropdownCustomer", dropdownCustomer);
            model.addAttribute("dropdownBook", dropdownBook);
            model.addAttribute("dto", dto);
            model.addAttribute("insert", insert);
            return "Loan/insert";
        } else {
            var book = bookService.getBooksById(dto.getBookCode());
            var customer = customerService.getCustomerByEntity(dto.getCustomerNumber());
            if (!book.getIsBorrowed()) {
                book.setIsBorrowed(!book.getIsBorrowed());
                customer.setLoanCount(customerService.loanCountSetter(dto.getCustomerNumber(), "loan"));
                bookService.update(book);
                customerService.updateWithEntity(customer);
            }
            loanService.insert(dto);
            return "redirect:/loan/index";
        }

    }

    @GetMapping("/return")
    public String ret(@RequestParam(required = true) Long id) {
        var data = loanService.getLoanById(id);
        var book = bookService.getBooksById(data.getBookCode());
        var customer = customerService.getCustomerByEntity(data.getCustomerNumber());
        if (data.getReturnDate() == null) {
            book.setIsBorrowed(false);
            bookService.update(book);
            customer.setLoanCount(customerService.loanCountSetter(data.getCustomerNumber(), "Return"));
            customerService.updateWithEntity(customer);
            data.setReturnDate(LocalDate.now());
            Long denda = loanService.getCountDenda(data.getDueDate());
            data.setDenda(denda);

            loanService.extendLoan(data);
            return "redirect:/loan/index";
        } else {
            return "Loan/valid";
        }
    }


    @GetMapping("/detail")
    public String detail(Model model, @RequestParam(required = true) Long id) {
        model.addAttribute("authorId", id);
        var loanDto = loanService.getLoanById(id);
        var customer = customerService.getCustomerByMember(loanDto.getCustomerNumber());
        var books = bookService.getBooksBycode(loanDto.getBookCode());
        var category = categoryService.getCategoryByCategoryName(books.getCategoryName());
        model.addAttribute("books", books);
        model.addAttribute("category", category);
        model.addAttribute("customer", customer);
        model.addAttribute("loanDto", loanDto);
        return "loan/detail";
    }

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
            var dropdownCustomer = Dropdown.dropdownCustomer(getCustomer);
            var dropdownBook = Dropdown.dropdownBook(getBook);
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
//            var getCustomer = customerService.getAvaliableCustomer();
            var getBook = bookService.getAvailableBook();
            var getCustomer = customerService.getAvaliableCustomerEdit(dto.getCustomerNumber());
//            var getBook = bookService.getAvailableBook();
            var dropdownCustomer = Dropdown.dropdownCustomer(getCustomer);
//            var dropdownBook = Dropdown.dropdownBook(getBook);
            var dropdownBook = Dropdown.dropdownBook(getBook);
//            dto.setDueDate(LocalDate.now());
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
        List<LoanIndexDto> loanDto = loanService.getOnDenda(page);

        Long totalPage = loanService.getCountPageDenda();
        model.addAttribute("dataDenda", loanDto);
        if (totalPage == 0) {
            page = 0;
        }
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
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
        if (data.getExtend() < 4) {
            Integer counter = data.getExtend();
            data.setExtend(counter + 1);
            data.setDueDate(data.getDueDate().plusDays(2));
            loanService.extendLoan(data);
            return "redirect:/loan/index";
        } else {

            model.addAttribute("extendValidationHeader", "Unable to Extend");
            model.addAttribute("extendValidationReason", "User was reached maximum extendable");
            return "loan/valid";
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

}


