package com.example.winterhold.controller.rest;

import com.example.winterhold.dto.category.CategoryInsertDto;
import com.example.winterhold.dto.rest.InsertFailedDto;
import com.example.winterhold.dto.rest.ResponseCrudRestDto;
import com.example.winterhold.dto.rest.ValidatorRestDto;
import com.example.winterhold.service.abs.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/category")
public class CategoryRestController {


    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategory")
    public ResponseEntity<Object> getAuthor() {
        try {
            var list = categoryService.getAll();
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Runtime Error on the server");
        }
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestParam(required = true) String categoryName) {
        try {
            var list = categoryService.getCategoryByCategoryName(categoryName);
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Runtime Error on the server");
        }
    }


    @PostMapping
    public ResponseEntity<Object> post(@Valid @RequestBody CategoryInsertDto dto, BindingResult bindingResult) {
        try {
            if (!bindingResult.hasErrors()) {
                categoryService.insert(dto);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseCrudRestDto(HttpStatus.OK, "Berhasil Insert", dto));
            } else {
                List<ValidatorRestDto> list = new ArrayList<>();
                List<FieldError> errorList = bindingResult.getFieldErrors();
                List<ObjectError> objectErrorList = bindingResult.getGlobalErrors();
                for (FieldError err : errorList
                ) {
                    var data = new ValidatorRestDto(err.getField(), err.getDefaultMessage());
                    list.add(data);
                }

                for (ObjectError err : objectErrorList
                ) {
                    var data = new ValidatorRestDto(err.getObjectName(), err.getDefaultMessage());
                    list.add(data);
                }

                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new InsertFailedDto(HttpStatus.UNPROCESSABLE_ENTITY, "Validate Error", list));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Runtime Error on the server");
        }

    }

    @PutMapping
    public ResponseCrudRestDto put(@Valid @RequestBody CategoryInsertDto dto, BindingResult bindingResult) {

        try {
            if (!bindingResult.hasErrors()) {
                categoryService.insert(dto);
                return new ResponseCrudRestDto(HttpStatus.OK, "Berhasil Update", dto);
            } else {
                List<ValidatorRestDto> list = new ArrayList<>();
                List<FieldError> errorList = bindingResult.getFieldErrors();
                List<ObjectError> objectErrorList = bindingResult.getGlobalErrors();
                for (FieldError err : errorList
                ) {
                    var data = new ValidatorRestDto(err.getField(), err.getDefaultMessage());
                    list.add(data);
                }

                for (ObjectError err : objectErrorList
                ) {
                    var data = new ValidatorRestDto(err.getObjectName(), err.getDefaultMessage());
                    list.add(data);
                }
                return new ResponseCrudRestDto(HttpStatus.UNPROCESSABLE_ENTITY, "Validation Failed", list);

            }

        } catch (Exception e) {
            return new ResponseCrudRestDto(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", dto);
        }
    }

    @DeleteMapping("/deleteCategory={categoryName}")
    public ResponseCrudRestDto delete(@PathVariable(required = true) String categoryName) {

        try {
            Boolean result = categoryService.delete(categoryName);
            if (!result) {
                return new ResponseCrudRestDto(HttpStatus.UNPROCESSABLE_ENTITY, "Category Name " + categoryName + " Cannot Be Deleted");
            }
            return new ResponseCrudRestDto(HttpStatus.OK, "Category Name " + categoryName + " Deleted");

        } catch (Exception e) {
            return new ResponseCrudRestDto(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error Server");
        }
    }
}
