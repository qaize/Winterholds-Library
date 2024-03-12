package com.example.winterhold.Controller.Rest;

import com.example.winterhold.Dto.Loan.LoanIndexDto;
import com.example.winterhold.Dto.Loan.LoanInsertDto;
import com.example.winterhold.Dto.Rest.InsertFailedDto;
import com.example.winterhold.Dto.Rest.ResponseCrudRestDto;
import com.example.winterhold.Dto.Rest.ValidatorRestDto;
import com.example.winterhold.Service.abs.LoanService;
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
@RequestMapping("/api/loan")
public class LoanRestController {


    @Autowired
    private LoanService loanService;

    @GetMapping("/getLoan")
    public ResponseEntity<Object> getAuthor() {
        try {
            var list = loanService.getAll();
            for (LoanIndexDto dto : list
            ) {
                System.out.println(dto.getId());

            }
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Runtime Error on the server");
        }
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestParam(required = true) Long id) {
        try {
            var list = loanService.getLoanById(id);
            System.out.println(list.getBookCode());
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Runtime Error on the server");
        }
    }


    @PostMapping
    public ResponseEntity<Object> post(@Valid @RequestBody LoanInsertDto dto, BindingResult bindingResult) {
        try {
            if (!bindingResult.hasErrors()) {
                System.out.println(dto.getId());
                System.out.println(dto.getBookCode());
                loanService.insert(dto);
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
    public ResponseCrudRestDto put(@Valid @RequestBody LoanInsertDto dto, BindingResult bindingResult) {

        try {
            if (!bindingResult.hasErrors()) {
                loanService.insert(dto);
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

    @DeleteMapping("/deleteLoan={id}")
    public ResponseCrudRestDto delete(@PathVariable(required = true) Long id) {

        try {
            loanService.delete(id);
            ResponseCrudRestDto obj = new ResponseCrudRestDto(HttpStatus.OK, "id " + id + " Deleted");
            return new ResponseCrudRestDto(HttpStatus.OK, "id " + id + " Deleted");

        } catch (Exception e) {
            return new ResponseCrudRestDto(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error Server");
        }


    }
}
