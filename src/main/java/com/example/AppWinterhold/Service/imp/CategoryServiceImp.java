package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Dao.CategoryRepository;
import com.example.AppWinterhold.Dto.Author.AuthorInsertDto;
import com.example.AppWinterhold.Dto.Category.CategoryIndexDto;
import com.example.AppWinterhold.Dto.Category.CategoryInsertDto;
import com.example.AppWinterhold.Dto.Category.CategoryUpdateDto;
import com.example.AppWinterhold.Entity.Author;
import com.example.AppWinterhold.Entity.Category;
import com.example.AppWinterhold.Service.abs.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Size;
import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<CategoryIndexDto> getListCategoryBySearch(Integer page, String name) {
        Integer row = 10;
        Pageable paging = PageRequest.of(page-1,row, Sort.by("id"));
        return categoryRepository.getListCategoryBySearch(name,paging);
    }

    @Override
    public Long getCountPage(String name) {
        Integer row = 10;
        Double totalData =(double) categoryRepository.getCountPage(name);
        Long totaPage =(long)  Math.ceil(totalData/row);

        return totaPage;
    }

    @Override
    public void insert(CategoryInsertDto dto) {
        Category en = new Category(dto.getName(),dto.getFloor(),
                dto.getIsle(),dto.getBay());
        categoryRepository.save(en);
    }

    @Override
    public CategoryIndexDto getCategoryByCategoryName(String categoryName) {
        return categoryRepository.getCategoryByCategoryName(categoryName);
    }

    @Override
    public CategoryInsertDto getCategoryByCategoryInsert(String categoryName) {
        return categoryRepository.getCategoryByCategoryInsert(categoryName);
    }

    @Override
    public Boolean delete(String categoryName) {
        Long data = categoryRepository.getCountbooks(categoryName);
        if(data>0){
            return false;
        }
        categoryRepository.deleteById(categoryName);
        return true;
    }

    @Override
    public List<CategoryIndexDto> getAll() {
        return categoryRepository.getAll();
    }

    @Override
    public void update(CategoryUpdateDto dto) {
        Category en = new Category(dto.getName(),dto.getFloor(),
                dto.getIsle(),dto.getBay());
        categoryRepository.save(en);
    }

    @Override
    public Long categoryChecker(String isle, Integer floor, String bay) {
        return categoryRepository.validatePlacement(isle,floor,bay);
    }
}
