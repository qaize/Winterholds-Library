package com.example.AppWinterhold.Controller.Model;

import com.example.AppWinterhold.Dto.Category.CategoryInsertDto;
import com.example.AppWinterhold.Dto.Category.CategoryUpdateDto;
import com.example.AppWinterhold.Service.abs.BookService;
import com.example.AppWinterhold.Service.abs.CategoryService;
import com.example.AppWinterhold.Service.imp.AccountServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookService bookService;

    @Autowired
    private AccountServiceImp account;

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(defaultValue = "") String name,
                        @RequestParam(defaultValue = "1") Integer page) {

        model.addAttribute("userLogin", account.getCurrentUserLogin());

        model.addAttribute("name", name);
        var listCategory = categoryService.getListCategoryBySearch(page, name);
        Long totalPage = categoryService.getCountPage(name);
        model.addAttribute("listCategory", listCategory);
        if (totalPage == 0) {
            page = 0;
        }
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);

        return "Category/index";
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
            return "redirect:/category/index";
        }

    }

    @GetMapping("/update")
    public String update(Model model, @RequestParam(required = true) String categoryName) {
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
            return "redirect:/category/index";
        }

    }

    @GetMapping("/detail")
    public String detail(Model model,
                         @RequestParam(required = true) String categoryName,
                         @RequestParam(defaultValue = "") String title,
                         @RequestParam(defaultValue = "") String author
    ) {
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("title", title);
        model.addAttribute("author", author);
        var categoryDto = categoryService.getCategoryByCategoryName(categoryName);
        var listBooks = bookService.getlistBooksByCategoryName(categoryName, title, author);
        model.addAttribute("categoryDto", categoryDto);
        model.addAttribute("listBooks", listBooks);
        return "Category/detail";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam(required = true) String categoryName) {

        Boolean result = categoryService.delete(categoryName);
        if (!result) {
            return "Category/delete";
        }
        return "redirect:/category/index";
    }

    @GetMapping("/valid")
    public String valid(Model model, @RequestParam String categoryName) {
        return "redirect:/category/detail?categoryName=" + categoryName;
    }
}
