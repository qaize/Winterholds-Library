package com.example.AppWinterhold.Controller.Model;

import com.example.AppWinterhold.Dto.Book.BookInsertDto;
import com.example.AppWinterhold.Dto.Book.BookUpdateDto;
import com.example.AppWinterhold.Service.abs.AuthorService;
import com.example.AppWinterhold.Service.abs.BookService;
import com.example.AppWinterhold.Service.imp.LoanServiceImp;
import com.example.AppWinterhold.Utility.Dropdown;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private LoanServiceImp loanServiceImp;

    @Autowired
    private AuthorService authorService;

    @GetMapping("/insert")
    public String insert(Model model, @RequestParam(required = true) String categoryName) {

        BookInsertDto dto = new BookInsertDto();
//        Boolean borrowed = false;
        model.addAttribute("categoryName", categoryName);
        var dropdownauthor = Dropdown.dropdownAuthor(authorService.getAll());
        model.addAttribute("dropdownAuthor", dropdownauthor);
        dto.setIsBorrowed(false);
        dto.setCategoryName(categoryName);
        model.addAttribute("dto", dto);
        return "Book/insert";
    }

    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("dto") BookInsertDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("dto", dto);
            Boolean borrowed = false;
            var dropdownauthor = Dropdown.dropdownAuthor(authorService.getAll());
            dto.setIsBorrowed(borrowed);
            model.addAttribute("dropdownAuthor", dropdownauthor);
            return "Book/insert";
        } else {
            bookService.insert(dto);
            return "redirect:/category/detail?categoryName=" + dto.getCategoryName();
        }
    }

    @GetMapping("/update")
    public String update(Model model, @RequestParam(required = true) String bookCode) {
        BookInsertDto dto = bookService.getBooksById(bookCode);
        var dropdownauthor = Dropdown.dropdownAuthor(authorService.getAll());
        model.addAttribute("dropdownAuthor", dropdownauthor);
        model.addAttribute("dto", dto);

        return "Book/update";


    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("dto") BookUpdateDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("dto", dto);
            var dropdownauthor = Dropdown.dropdownAuthor(authorService.getAll());
            model.addAttribute("dropdownAuthor", dropdownauthor);
            return "Book/update";
        } else {
            bookService.update(dto);
            return "redirect:/category/detail?categoryName=" + dto.getCategoryName();
        }

    }

    @GetMapping("/delete")
    public String delete(@RequestParam(required = true) String bookCode) {
        var data = bookService.getBooksById(bookCode);
        Boolean result = bookService.delete(bookCode);
        if (!result) {
            return "Category/delete";
        }
        return "redirect:/category/detail?categoryName=" + data.getCategoryName();
    }

    @GetMapping("/borrow")
    public String borrow(Model model, @RequestParam(required = true) String bookCode, @RequestParam(required = true) String categoryName) {
        var data = bookService.getBooksBycodeUpdate(bookCode);
        if (loanServiceImp.checkLoanBooks(bookCode) > 0) {
            model.addAttribute("categoryName", categoryName);
            return "Category/valid";
        } else {
            data.setIsBorrowed(!data.getIsBorrowed());
            bookService.update(data);
            return "redirect:/category/detail?categoryName=" + categoryName;
        }
    }
}
