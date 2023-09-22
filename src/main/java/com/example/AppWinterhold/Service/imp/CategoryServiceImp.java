package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Dao.CategoryRepository;
import com.example.AppWinterhold.Dto.BaseResponseDTO;
import com.example.AppWinterhold.Dto.Category.CategoryIndexDto;
import com.example.AppWinterhold.Dto.Category.CategoryInsertDto;
import com.example.AppWinterhold.Dto.Category.CategoryUpdateDto;
import com.example.AppWinterhold.Entity.Category;
import com.example.AppWinterhold.Service.abs.CategoryService;
import com.example.AppWinterhold.Utility.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.AppWinterhold.Const.actionConst.*;


@Service
public class CategoryServiceImp implements CategoryService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImp.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LogServiceImpl logService;

    @Override
    public BaseResponseDTO<List<CategoryIndexDto>> getListCategoryBySearch(Integer page, String name) {
        int row = 10;
        try {
            Pageable paging = PageRequest.of(page - 1, row, Sort.by("id"));
            Page<CategoryIndexDto> listCategory =  categoryRepository.getListCategoryBySearch(name, paging);
            LOGGER.info(SUCCESS_GET_DATA);
            return BaseResponseDTO.<List<CategoryIndexDto>>builder()
                    .status("1")
                    .httpStatus(HttpStatus.OK)
                    .message("OK")
                    .data(listCategory.getContent())
                    .metaData(new BaseResponseDTO.MetaData(listCategory.getTotalElements(),listCategory.getTotalPages(), listCategory.getSize()))
                    .build();
        } catch (Exception e) {
            LOGGER.error(FAILED_GET_DATA, e.getMessage());
            return BaseResponseDTO.<List<CategoryIndexDto>>builder()
                    .status("0")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .data(new ArrayList<>()).build();
        }
    }


    @Override
    public Long getCountPage(String name) {
        Integer row = 10;
        Double totalData = (double) categoryRepository.getCountPage(name);
        Long totaPage = (long) Math.ceil(totalData / row);

        return totaPage;
    }

    @Override
    public void insert(CategoryInsertDto dto) {
        try {
            Category en = new Category(dto.getName(), dto.getFloor(),
                    dto.getIsle().toUpperCase(), dto.getBay());
            categoryRepository.save(en);
            logService.saveLogs(CATEGORY, SUCCESS, INSERT);
        } catch (Exception e) {
            logService.saveLogs(CATEGORY, e.getMessage(), INSERT);
        }
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
        if (data > 0) {
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
        Category en = new Category(dto.getName(), dto.getFloor(),
                dto.getIsle(), dto.getBay());
        categoryRepository.save(en);
    }

    @Override
    public Long categoryChecker(String isle, Integer floor, String bay) {
        return categoryRepository.validatePlacement(isle, floor, bay);
    }
}
