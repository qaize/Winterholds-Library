package com.example.winterhold.Service.abs;

import com.example.winterhold.Dto.Category.CategoryIndexDto;
import com.example.winterhold.Dto.Category.CategoryInsertDto;
import com.example.winterhold.Dto.Category.CategoryUpdateDto;
import org.springframework.ui.Model;

import java.util.List;

public interface CategoryService {

    Long getCountPage(String name);
    List<CategoryIndexDto> getAll();
    void insert(CategoryInsertDto dto);
    void update(CategoryUpdateDto dto);
    Boolean delete(String categoryName);
    Long categoryChecker(String isle,Integer floor, String bay);
    CategoryIndexDto getCategoryByCategoryName(String categoryName);
    CategoryInsertDto getCategoryByCategoryInsert(String categoryName);
    String getListCategoryBySearch(Integer page, String name, Model model);
}
