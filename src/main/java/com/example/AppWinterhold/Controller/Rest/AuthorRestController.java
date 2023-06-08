package com.example.AppWinterhold.Controller.Rest;

import com.example.AppWinterhold.Dto.Account.AccountInsertDto;
import com.example.AppWinterhold.Dto.Author.AuthorIndexDto;
import com.example.AppWinterhold.Dto.Author.AuthorIndexDtoV2;
import com.example.AppWinterhold.Dto.Author.AuthorInsertDto;
import com.example.AppWinterhold.Dto.Author.AuthorUpdateDto;
import com.example.AppWinterhold.Dto.Rest.InsertFailedDto;
import com.example.AppWinterhold.Dto.Rest.ResponseCrudRestDto;
import com.example.AppWinterhold.Dto.Rest.ValidatorRestDto;
import com.example.AppWinterhold.Service.abs.AuthorService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
@RequestMapping("/api/author")
public class AuthorRestController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/getAuthor")
    public ResponseEntity<Object> getAuthor(){
        try{
            List<AuthorIndexDto> mapperAuthor = new ArrayList<>();

            var list = authorService.getAll();
            for (AuthorIndexDto map:list
                 ) {
                mapperAuthor.add(map);

            }

            return ResponseEntity.status(HttpStatus.OK).body(mapperAuthor);
        }
        catch  (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Runtime Error on the server");
        }
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestParam(required = true) Long id){
        try{
            var list = authorService.getAuthorById(id);
            return ResponseEntity.status(HttpStatus.OK).body(list);
        }
        catch  (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Runtime Error on the server");
        }
    }



    @PostMapping
    public ResponseEntity<Object> posted(@Valid @RequestBody AuthorInsertDto dto, BindingResult bindingResult){
        try{
            if(!bindingResult.hasErrors()){
                authorService.insert(dto);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseCrudRestDto(HttpStatus.OK,"Berhasil Insert",dto));
            }


            else{
                List<ValidatorRestDto> list = new ArrayList<>();
                List<FieldError> errorList = bindingResult.getFieldErrors();
                List<ObjectError> objectErrorList = bindingResult.getGlobalErrors();
                for (FieldError err:errorList
                ) {
                    var data = new ValidatorRestDto(err.getField(),err.getDefaultMessage());
                    list.add(data);
                }

                for (ObjectError err:objectErrorList
                ) {
                    var data = new ValidatorRestDto(err.getObjectName(),err.getDefaultMessage());
                    list.add(data);
                }

                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new InsertFailedDto(HttpStatus.UNPROCESSABLE_ENTITY,"Validate Error",list));
            }
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Runtime Error on the server");
        }

    }
    @GetMapping("/getDataPaging")
    public ResponseEntity<Object> getDataPaging(@RequestBody AuthorIndexDtoV2 dto) throws JsonProcessingException {
                var list = authorService.getListAuthorBySearchV2(dto);
                return ResponseEntity.status(HttpStatus.OK).body(list);

    }

    @PutMapping
    public ResponseCrudRestDto put(@Valid @RequestBody AuthorUpdateDto dto, BindingResult bindingResult){

        try{
            if(!bindingResult.hasErrors()){
                authorService.update(dto);
                return new ResponseCrudRestDto(HttpStatus.OK,"Berhasil Update",dto);
            }
            else{
                List<ValidatorRestDto> list = new ArrayList<>();
                List<FieldError> errorList = bindingResult.getFieldErrors();
                List<ObjectError> objectErrorList = bindingResult.getGlobalErrors();
                for (FieldError err:errorList
                ) {
                    var data = new ValidatorRestDto(err.getField(),err.getDefaultMessage());
                    list.add(data);
                }

                for (ObjectError err:objectErrorList
                ) {
                    var data = new ValidatorRestDto(err.getObjectName(),err.getDefaultMessage());
                    list.add(data);
                }
                return new ResponseCrudRestDto(HttpStatus.UNPROCESSABLE_ENTITY,"Validation Failed",list);

            }

        }catch (Exception e){
            return new ResponseCrudRestDto(HttpStatus.INTERNAL_SERVER_ERROR,"Internal Server Error",dto);
        }
    }

    @DeleteMapping("/deleteAuthor={id}")
    public ResponseCrudRestDto delete(@PathVariable(required = true) Long id){

        try{
            Boolean result = authorService.delete(id);
            if(!result){
                return new ResponseCrudRestDto(HttpStatus.UNPROCESSABLE_ENTITY,"Id "+id+" Cannot Be Deleted");
            }
            return new ResponseCrudRestDto(HttpStatus.OK,"Id "+id+" Deleted");

        }catch (Exception e){
            return new ResponseCrudRestDto(HttpStatus.INTERNAL_SERVER_ERROR,"Internal Error Server");
        }
    }
}