package com.example.AppWinterhold.Controller.Model;

import com.example.AppWinterhold.Dto.Author.AuthorInsertDto;
import com.example.AppWinterhold.Dto.Author.AuthorUpdateDto;
import com.example.AppWinterhold.Service.abs.AuthorService;
import com.example.AppWinterhold.Service.abs.BookService;
import com.example.AppWinterhold.Service.imp.AccountServiceImp;
import com.example.AppWinterhold.Utility.Dropdown;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookService bookService;

    @Autowired
    private AccountServiceImp accountServiceImp;

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(defaultValue = "") String name,
                        @RequestParam(defaultValue = "1") Integer page) throws JsonProcessingException {

        model.addAttribute("userLogin", accountServiceImp.getCurrentUserLogin());

        model.addAttribute("name", name);
        var listAuthor = authorService.getListAuthorBySearch(page, name);
        Long totalPage = authorService.getCountPage(name);
        model.addAttribute("listAuthor", listAuthor);
        if (totalPage == 0) {
            page = 0;
        }
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);

        return "Author/index";
    }

    @GetMapping("/insert")
    public String insert(Model model) {
        AuthorInsertDto dto = new AuthorInsertDto();
        model.addAttribute("dto", dto);
        model.addAttribute("dropdownTitle", Dropdown.dropdownTitle());
        return "Author/insert";
    }

    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("dto") AuthorInsertDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("dto", dto);
            model.addAttribute("dropdownTitle", Dropdown.dropdownTitle());
            return "Author/insert";
        } else {
            authorService.insert(dto);
            return "redirect:/author/index";
        }
    }

    @GetMapping("/detail")
    public String detail(Model model, @RequestParam(required = true) Long id) {
        model.addAttribute("authorId", id);
        var authorDto = authorService.getAuthorById(id);
        var listBooks = bookService.getlistBooksByAuthorId(id);
        model.addAttribute("authorDto", authorDto);
        model.addAttribute("listBooks", listBooks);
        return "Author/detail";
    }

    @GetMapping("/update")
    public String update(Model model, @RequestParam(required = false) Long id) {
        AuthorInsertDto dto = authorService.getAuthorByIdinsert(id);
        model.addAttribute("dropdownTitle", Dropdown.dropdownTitle());
        model.addAttribute("dto", dto);
        return "Author/update";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("dto") AuthorUpdateDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("dto", dto);
            model.addAttribute("dropdownTitle", Dropdown.dropdownTitle());
            return "Author/update";
        } else {
            authorService.update(dto);
            return "redirect:/author/index";
        }

    }

    @GetMapping("/delete")
    public String delete(@RequestParam(required = true) Long id) {

        Boolean result = authorService.delete(id);
        if (!result) {
            return "Author/delete";
        }
        return "redirect:/author/index";
    }
}
