package com.example.winterhold.service.imp;

import com.example.winterhold.repository.CategoryRepository;
import com.example.winterhold.dto.category.CategoryIndexDto;
import com.example.winterhold.dto.category.CategoryInsertDto;
import com.example.winterhold.dto.category.CategoryUpdateDto;
import com.example.winterhold.entity.Category;
import com.example.winterhold.service.abs.CategoryService;
import com.example.winterhold.service.abs.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

import static com.example.winterhold.constants.ActionConstants.*;


@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImp.class);

    private final CategoryRepository categoryRepository;

    private final LogServiceImpl logService;

    private final NotificationService notificationService;

    @Override
    public String getListCategoryBySearch(Integer page, String name, Model model) {
        int row = 10;
        int flag = 0;
        String message = "";

        Pageable paging = PageRequest.of(page - 1, row, Sort.by("id"));
        Page<CategoryIndexDto> listCategory = categoryRepository.getListCategoryBySearch(name, paging);

        if (listCategory.getContent().isEmpty()) {
            message = INDEX_EMPTY;
            flag = 1;
        }

        model.addAttribute("newNotification", notificationService.getNotification());
        model.addAttribute("name", name);
        model.addAttribute("flag", flag);
        model.addAttribute("message", message);
        model.addAttribute("totalPage", listCategory.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("listCategory", listCategory);
        return "Category/index";
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
        return data > 0 ? doDelete(categoryName) : false;
    }

    private Boolean doDelete(String categoryName) {
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
