package com.example.AppWinterhold.Service.abs;

import com.example.AppWinterhold.Dto.Category.CategoryIndexDto;
import com.example.AppWinterhold.Dto.Category.CategoryInsertDto;
import com.example.AppWinterhold.Dto.Category.CategoryUpdateDto;

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
    List<CategoryIndexDto> getListCategoryBySearch(Integer page, String name);
}
