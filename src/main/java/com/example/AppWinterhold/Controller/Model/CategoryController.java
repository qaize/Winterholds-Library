package com.example.AppWinterhold.Controller.Model;

import com.example.AppWinterhold.Dto.BaseResponseDTO;
import com.example.AppWinterhold.Dto.Category.CategoryIndexDto;
import com.example.AppWinterhold.Dto.Category.CategoryInsertDto;
import com.example.AppWinterhold.Dto.Category.CategoryUpdateDto;
import com.example.AppWinterhold.Dto.Models.DataDTO;
import com.example.AppWinterhold.Service.abs.BookService;
import com.example.AppWinterhold.Service.abs.CategoryService;
import com.example.AppWinterhold.Service.imp.AccountServiceImp;
import com.example.AppWinterhold.Service.imp.LoanServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookService bookService;

    @Autowired
    private AccountServiceImp account;

    @Autowired
    private LoanServiceImp loanServiceImp;

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(defaultValue = "") String name,
                        @RequestParam(defaultValue = "1") Integer page) {

        DataDTO<List<CategoryIndexDto>> data = categoryService.getListCategoryBySearch(page, name);

        model.addAttribute("newNotification",loanServiceImp.getNotification());
        model.addAttribute("name", name);
        model.addAttribute("flag", data.getFlag());
        model.addAttribute("message", data.getMessage());
        model.addAttribute("totalPage", data.getTotalPage());
        model.addAttribute("currentPage", page);
        model.addAttribute("listCategory", data.getData());

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

            return "redirect:/category/index";
        }
    }

    @GetMapping("/detail")
    public String detail(Model model,
                         @RequestParam String categoryName,
                         @RequestParam(defaultValue = "") String title,
                         @RequestParam(defaultValue = "") String author){

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
    public String delete(@RequestParam(required = true) String categoryName) {

        Boolean result = categoryService.delete(categoryName);

        if (!result) {return "Category/delete";}

        return "redirect:/category/index";
    }

    @GetMapping("/valid")
    public String valid(Model model, @RequestParam String categoryName) {

        return "redirect:/category/detail?categoryName=" + categoryName;
    }
}
