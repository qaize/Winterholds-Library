package com.example.AppWinterhold.Controller.Rest;

import com.example.AppWinterhold.Dao.BookRepository;
import com.example.AppWinterhold.Dto.Author.AuthorInsertDto;
import com.example.AppWinterhold.Dto.Book.BookInsertDto;
import com.example.AppWinterhold.Dto.Rest.InsertFailedDto;
import com.example.AppWinterhold.Dto.Rest.ResponseCrudRestDto;
import com.example.AppWinterhold.Dto.Rest.ValidatorRestDto;
import com.example.AppWinterhold.Entity.Book;
import com.example.AppWinterhold.Service.abs.AuthorService;
import com.example.AppWinterhold.Service.abs.BookService;
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
@RequestMapping("/api/book")
public class BookRestController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/getBooks")
    public ResponseEntity<Object> getAuthor(){
        try{
            var list = bookService.getAll();
            return ResponseEntity.status(HttpStatus.OK).body(list);
        }
        catch  (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Runtime Error on the server");
        }
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestParam(required = true) String code){
        try{
            var list = bookService.getBooksBycode2(code);
            System.out.println("SUCCESS 2");
            return ResponseEntity.status(HttpStatus.OK).body(list);
        }
        catch  (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Runtime Error on the server");
        }
    }

//    @GetMapping
//    public List<Book> getAllBooks(){return bookRepository.findAll();}


    @PostMapping
    public ResponseEntity<Object> post(@Valid @RequestBody BookInsertDto dto, BindingResult bindingResult){
        try{
            if(!bindingResult.hasErrors()){
                bookService.insert(dto);
                return ResponseEntity.status(HttpStatus.OK).body(dto);
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

    @PutMapping
    public ResponseCrudRestDto put(@Valid @RequestBody BookInsertDto dto, BindingResult bindingResult){

        try{
            if(!bindingResult.hasErrors()){
                bookService.insert(dto);
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

    @DeleteMapping("/deleteBook={code}")
    public ResponseCrudRestDto delete(@PathVariable(required = true) String code){

        try{
            Boolean result = bookService.delete(code);
            if(!result){
                return new ResponseCrudRestDto(HttpStatus.UNPROCESSABLE_ENTITY,"Code "+code+" Cannot Be Deleted");
            }
            return new ResponseCrudRestDto(HttpStatus.OK,"Code "+code+" Deleted");

        }catch (Exception e){
            return new ResponseCrudRestDto(HttpStatus.INTERNAL_SERVER_ERROR,"Internal Error Server");
        }
    }
}
