package com.example.winterhold.controller.model;

import com.example.winterhold.constants.MvcRedirectConst;
import com.example.winterhold.dto.category.CategoryInsertDto;
import com.example.winterhold.dto.category.CategoryUpdateDto;
import com.example.winterhold.service.abs.BookService;
import com.example.winterhold.service.abs.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final BookService bookService;

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(defaultValue = "") String name,
                        @RequestParam(defaultValue = "1") Integer page) {
        return categoryService.getListCategoryBySearch(page, name, model);
    }

    @GetMapping("/insert")
    public String insert(Model model) {

        CategoryInsertDto dto = new CategoryInsertDto();

        model.addAttribute("dto", dto);

        return "Category/insert";
    }

    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("dto") CategoryInsertDto dto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("dto", dto);

            return "Category/insert";
        } else {

            categoryService.insert(dto);

            return MvcRedirectConst.REDIRECT_CATEGORY_INDEX;
        }
    }

    @GetMapping("/update")
    public String update(Model model, @RequestParam String categoryName) {

        CategoryInsertDto dto = categoryService.getCategoryByCategoryInsert(categoryName);

        model.addAttribute("dto", dto);

        return "Category/update";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("dto") CategoryUpdateDto dto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("dto", dto);

            return "Category/update";
        } else {

            categoryService.update(dto);

            return MvcRedirectConst.REDIRECT_CATEGORY_INDEX;
        }
    }

    @GetMapping("/detail")
    public String detail(Model model,
                         @RequestParam String categoryName,
                         @RequestParam(defaultValue = "") String title,
                         @RequestParam(defaultValue = "") String author) {

        var categoryDto = categoryService.getCategoryByCategoryName(categoryName);
        var listBooks = bookService.getlistBooksByCategoryName(categoryName, title, author);

        model.addAttribute("title", title);
        model.addAttribute("author", author);
        model.addAttribute("categoryDto", categoryDto);
        model.addAttribute("listBooks", listBooks);
        model.addAttribute("categoryName", categoryName);

        return "Category/detail";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam String categoryName) {

        Boolean result = categoryService.delete(categoryName);

        if (Boolean.FALSE.equals(result)) {
            return "Category/delete";
        }

        return MvcRedirectConst.REDIRECT_CATEGORY_INDEX;
    }

    @GetMapping("/valid")
    public String valid(Model model, @RequestParam String categoryName) {

        return "redirect:/category/detail?categoryName=" + categoryName;
    }
}
