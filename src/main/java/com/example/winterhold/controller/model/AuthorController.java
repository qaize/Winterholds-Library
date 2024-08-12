package com.example.winterhold.controller.model;

import com.example.winterhold.constants.MvcRedirectConst;
import com.example.winterhold.dto.author.AuthorIndexDto;
import com.example.winterhold.dto.author.AuthorInsertDto;
import com.example.winterhold.dto.author.AuthorUpdateDto;
import com.example.winterhold.dto.models.DataDTO;
import com.example.winterhold.service.abs.AuthorService;
import com.example.winterhold.service.abs.BookService;
import com.example.winterhold.service.abs.NotificationService;
import com.example.winterhold.utility.Dropdown;
import com.example.winterhold.constants.WinterholdConstants;
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

    private final NotificationService notificationService;

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
        model.addAttribute("newNotification",notificationService.getNotification());

        return "Author/index";
    }

    @GetMapping("/insert")
    public String insert(Model model) {

        AuthorInsertDto dto = new AuthorInsertDto();

        model.addAttribute("dto", dto);
        model.addAttribute(WinterholdConstants.CONTROLLER_DROPDOWN_TITLE, Dropdown.dropdownTitle());

        return "Author/insert";
    }

    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("dto") AuthorInsertDto dto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("dto", dto);
            model.addAttribute(WinterholdConstants.CONTROLLER_DROPDOWN_TITLE, Dropdown.dropdownTitle());

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

        model.addAttribute(WinterholdConstants.CONTROLLER_DROPDOWN_TITLE, Dropdown.dropdownTitle());
        model.addAttribute("dto", dto);

        return "Author/update";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("dto") AuthorUpdateDto dto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("dto", dto);
            model.addAttribute(WinterholdConstants.CONTROLLER_DROPDOWN_TITLE, Dropdown.dropdownTitle());

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
