package com.example.winterhold.service.abs;

import com.example.winterhold.dto.category.CategoryIndexDto;
import com.example.winterhold.dto.category.CategoryInsertDto;
import com.example.winterhold.dto.category.CategoryUpdateDto;
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
