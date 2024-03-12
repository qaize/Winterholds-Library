package com.example.winterhold.controller.model;

import com.example.winterhold.Dto.Book.BookInsertDto;
import com.example.winterhold.Dto.Book.BookUpdateDto;
import com.example.winterhold.Service.abs.AuthorService;
import com.example.winterhold.Service.abs.BookService;
import com.example.winterhold.Utility.Dropdown;
import com.example.winterhold.constants.MvcRedirectConst;
import com.example.winterhold.constants.WinterholdConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    
    private final AuthorService authorService;

    @GetMapping("/insert")
    public String insert(Model model, @RequestParam String categoryName) {

        BookInsertDto dto = new BookInsertDto();
        var dropdownAuthor = Dropdown.dropdownAuthor(authorService.getAllAuthor());
        dto.setIsBorrowed(false);
        dto.setCategoryName(categoryName);

        model.addAttribute("categoryName", categoryName);
        model.addAttribute(WinterholdConstants.CONTROLLER_DROPDOWN_AUTHOR, dropdownAuthor);
        model.addAttribute("dto", dto);

        return "Book/insert";
    }

    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("dto") BookInsertDto dto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            Boolean borrowed = false;
            var dropdownAuthor = Dropdown.dropdownAuthor(authorService.getAllAuthor());
            dto.setIsBorrowed(borrowed);

            model.addAttribute("dto", dto);
            model.addAttribute(WinterholdConstants.CONTROLLER_DROPDOWN_AUTHOR, dropdownAuthor);

            return "Book/insert";
        } else {

            bookService.insert(dto);

            return MvcRedirectConst.REDIRECT_CATEGORY_DETAIL.concat(dto.getCategoryName());
        }
    }

    @GetMapping("/update")
    public String update(Model model, @RequestParam String bookCode) {

        BookUpdateDto dto = bookService.getBooksById(bookCode);
        var dropdownAuthor = Dropdown.dropdownAuthor(authorService.getAllAuthor());

        model.addAttribute(WinterholdConstants.CONTROLLER_DROPDOWN_AUTHOR, dropdownAuthor);
        model.addAttribute("dto", dto);

        return "Book/update";


    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("dto") BookUpdateDto dto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            var dropdownAuthor = Dropdown.dropdownAuthor(authorService.getAllAuthor());

            model.addAttribute("dto", dto);
            model.addAttribute(WinterholdConstants.CONTROLLER_DROPDOWN_AUTHOR, dropdownAuthor);

            return "Book/update";
        } else {
            BookUpdateDto data = bookService.getBooksById(dto.getCode());
            dto.setInBorrow(data.getInBorrow());
            dto.setIsBorrowed(data.getInBorrow() < dto.getQuantity());
            bookService.update(dto);

            return MvcRedirectConst.REDIRECT_CATEGORY_DETAIL.concat(dto.getCategoryName());
        }
    }

    @GetMapping("/delete")
    public String delete(@RequestParam String bookCode) {

        var data = bookService.getBooksById(bookCode);
        Boolean result = bookService.delete(bookCode);

        if (Boolean.FALSE.equals(result)) {
            return "Category/delete";
        }

        return MvcRedirectConst.REDIRECT_CATEGORY_DETAIL.concat(data.getCategoryName());
    }

}
