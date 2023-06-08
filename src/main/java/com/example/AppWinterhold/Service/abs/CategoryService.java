package com.example.AppWinterhold.Service.abs;

import com.example.AppWinterhold.Dto.Category.CategoryIndexDto;
import com.example.AppWinterhold.Dto.Category.CategoryInsertDto;
import com.example.AppWinterhold.Dto.Category.CategoryUpdateDto;

import java.util.List;

public interface CategoryService {
    List<CategoryIndexDto> getListCategoryBySearch(Integer page, String name);

    Long getCountPage(String name);

    void insert(CategoryInsertDto dto);

    CategoryIndexDto getCategoryByCategoryName(String categoryName);
    CategoryInsertDto getCategoryByCategoryInsert(String categoryName);

    Boolean delete(String categoryName);

    List<CategoryIndexDto> getAll();

    void update(CategoryUpdateDto dto);

    Long categoryChecker(String isle,Integer floor, String bay);
}
