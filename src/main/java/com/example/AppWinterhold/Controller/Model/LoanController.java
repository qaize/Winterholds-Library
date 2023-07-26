package com.example.AppWinterhold.Controller.Model;

import com.example.AppWinterhold.Dao.LoanRepository;
import com.example.AppWinterhold.Dto.Loan.LoanIndexDto;
import com.example.AppWinterhold.Dto.Loan.LoanInsertDto;
import com.example.AppWinterhold.Dto.Loan.LoanUpdateDto;
import com.example.AppWinterhold.Entity.Loan;
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

    @Autowired
    private LoanService loanService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private AccountServiceImp account;

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
            if (val.getReturnDate() == null && LocalDate.now().isBefore(val.getDueDate())) {
                Long between = ChronoUnit.DAYS.between(LocalDate.now(), val.getDueDate());
                if (between == 0) {
                    val.setDayLeft(between.toString());
                    val.setLateLoan("Last Day");

                } else {
                    val.setDayLeft(between.toString());
                    val.setLateLoan("On Loan");
                }
            } else if (val.getReturnDate() == null && LocalDate.now().isAfter(val.getDueDate())) {
                Long between = ChronoUnit.DAYS.between(val.getDueDate(), LocalDate.now());
                val.setDayLeft("WARNING!");
                val.setLateLoan("Late : -" + between.toString() + " Days");
            } else if (val.getReturnDate() != null && LocalDate.now().isAfter(val.getDueDate())) {
                Long between = ChronoUnit.DAYS.between(val.getDueDate(), val.getReturnDate());
                val.setDayLeft("END");
                val.setLateLoan("Late : -" + between.toString() + " Days");

            } else if (val.getReturnDate() == null && LocalDate.now().isEqual(val.getDueDate())) {
//                Long between = ChronoUnit.DAYS.between(val.getDueDate(),val.getReturnDate());
                val.setDayLeft("On Loan");
                val.setLateLoan("Last day loan");

            } else {
                val.setDayLeft("END");
                val.setLateLoan("On Time");
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
        var getLoan = loanService.getAllByInsert();
        var getBook = bookService.getAvailableBook();
        var dropdownCustomer = Dropdown.dropdownCustomer(getCustomer);
        var dropdownBook = Dropdown.dropdownBook(getBook);
        model.addAttribute("dropdownCustomer", dropdownCustomer);
        model.addAttribute("dropdownBook", dropdownBook);
        dto.setDueDate(LocalDate.now());
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
            dto.setDueDate(LocalDate.now());
            model.addAttribute("dropdownCustomer", dropdownCustomer);
            model.addAttribute("dropdownBook", dropdownBook);
            model.addAttribute("dto", dto);
            model.addAttribute("insert", insert);
            return "Loan/insert";
        } else {
            dto.setDueDate(dto.getLoanDate().plusDays(5));
            var book = bookService.getBooksById(dto.getBookCode());
            if (!book.getIsBorrowed()) {
                book.setIsBorrowed(!book.getIsBorrowed());
                bookService.insert(book);
            }
            loanService.insert(dto);
            return "redirect:/loan/index";
        }

    }

    @GetMapping("/return")
    public String ret(@RequestParam(required = true) Long id) {
        var data = loanService.getLoanById(id);
        var book = bookService.getBooksById(data.getBookCode());
        if (data.getReturnDate() == null) {
            book.setIsBorrowed(false);
            bookService.insert(book);
            data.setReturnDate(LocalDate.now());

            Long denda = loanService.getCountDenda(data.getLoanDate(), data.getReturnDate());
            data.setDenda(denda);
            loanService.insert(data);
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

    @GetMapping("/denda")
    public String denda(Model model) {
        List<Loan> loanDto = loanService.getOnDenda();
        model.addAttribute("dataDenda", loanDto);
        return "loan/denda";
    }

    @GetMapping("/payment")
    public String payment(Model model, @RequestParam(required = true) Long id) {
        loanService.goPayOff(id);
        return "loan/denda";
    }

    @GetMapping("/update")
    public String update(Model model, @RequestParam(required = true) Long id) {

        String update = "update";
        LoanInsertDto dto = loanService.getLoanById(id);
        var books = bookService.getBooksById(dto.getBookCode());
        if (dto.getReturnDate() == null) {
            books.setIsBorrowed(!books.getIsBorrowed());
            bookService.insert(books);
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
            bookService.insert(books);
//            var extend = loanRepository.getExtendById(dto.getId());
            loanService.update(dto);
            return "redirect:/loan/index";
        }

    }
}


