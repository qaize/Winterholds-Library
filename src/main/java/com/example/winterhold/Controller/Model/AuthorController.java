package com.example.winterhold.Controller.Model;

import com.example.winterhold.Const.MvcRedirectConst;
import com.example.winterhold.Dto.Author.AuthorIndexDto;
import com.example.winterhold.Dto.Author.AuthorInsertDto;
import com.example.winterhold.Dto.Author.AuthorUpdateDto;
import com.example.winterhold.Dto.Models.DataDTO;
import com.example.winterhold.Service.abs.AuthorService;
import com.example.winterhold.Service.abs.BookService;
import com.example.winterhold.Service.imp.LoanServiceImp;
import com.example.winterhold.Utility.Dropdown;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    private final BookService bookService;


    private final LoanServiceImp loanServiceImp;

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(defaultValue = "") String name,
                        @RequestParam(defaultValue = "1") Integer page) throws JsonProcessingException {

        DataDTO<List<AuthorIndexDto>> data = authorService.getListAuthorBySearch(page, name);

        model.addAttribute("name", name);
        model.addAttribute("flag", data.getFlag());
        model.addAttribute("message", data.getMessage());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", data.getTotalPage());
        model.addAttribute("listAuthor", data.getData());
        model.addAttribute("newNotification",loanServiceImp.getNotification());

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

            return MvcRedirectConst.REDIRECT_AUTHOR_INDEX;
        }
    }

    @GetMapping("/detail")
    public String detail(Model model, @RequestParam(required = true) Long id) throws JsonProcessingException {

        var authorDto = authorService.getAuthorById(id);
        var listBooks = bookService.getlistBooksByAuthorId(id);

        model.addAttribute("authorId", id);
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
            return MvcRedirectConst.REDIRECT_AUTHOR_INDEX;
        }

    }

    @GetMapping("/delete")
    public String delete(Model model, @RequestParam Long id) {

        Boolean result = authorService.delete(id);

        if (Boolean.FALSE.equals(result)) {
            model.addAttribute("h2", "Some book registered to this author");
            return "Author/delete";
        }
        return MvcRedirectConst.REDIRECT_AUTHOR_INDEX;

    }
}
