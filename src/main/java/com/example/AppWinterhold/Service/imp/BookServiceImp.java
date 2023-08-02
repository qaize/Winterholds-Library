package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Dao.BookRepository;
import com.example.AppWinterhold.Dto.Book.BookIndexDto;
import com.example.AppWinterhold.Dto.Book.BookInsertDto;
import com.example.AppWinterhold.Dto.Book.BookUpdateDto;
import com.example.AppWinterhold.Dto.Rest.ResponseCrudRestDto;
import com.example.AppWinterhold.Entity.Book;
import com.example.AppWinterhold.Service.abs.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class BookServiceImp implements BookService {

    @Autowired
    private BookRepository bookRepository;


    @Override
    public List<BookIndexDto> getlistBooksByCategoryName(String categoryName) {
        return bookRepository.getlistBooksByCategoryName(categoryName);
    }

    @Override
    public List<BookIndexDto> getlistBooksByCategoryName(String categoryName, String title, String author) {
        return bookRepository.getlistBooksByCategoryName(categoryName, title, author);
    }

    @Override
    public void insert(BookInsertDto dto) {
        String code = generateBookCodeByCategory(dto.getCategoryName());
        Book en = new Book(code, dto.getTitle(), dto.getCategoryName(),
                dto.getAuthorId().longValue(), dto.getIsBorrowed(), dto.getSummary(),
                dto.getReleaseDate(), dto.getTotalPage());

        bookRepository.save(en);
    }

    @Override
    public ResponseCrudRestDto getAll() {
        List<BookIndexDto> data = bookRepository.getAll();
        ResponseCrudRestDto response = new ResponseCrudRestDto();

        try {

            response.setMessage("OK");
            response.setStatus(HttpStatus.OK);
            response.setObject(data);
        } catch (Exception e) {
            response.setMessage("INTERNAL ERROR ");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setObject(null);

        }


        return response;
    }

    @Override
    public List<BookIndexDto> getlistBooksByAuthorId(Long id) {
        return bookRepository.getlistBooksByAuthorId(id);
    }

    @Override
    public BookUpdateDto getBooksById(String bookCode) {
        return bookRepository.getBooksById(bookCode);
    }

    @Override
    public List<BookIndexDto> getAvailableBook() {
        return bookRepository.getAvailableBook();
    }

    @Override
    public BookIndexDto getBooksBycode(String bookCode) {
        return bookRepository.getBooksBycode(bookCode);
    }

    @Override
    public ResponseCrudRestDto getBooksBycode2(String bookCode) {
        BookIndexDto data = bookRepository.getBooksBycode(bookCode);
        ResponseCrudRestDto response = new ResponseCrudRestDto();
        try {

            response.setMessage("SUCCESS");
            response.setStatus(HttpStatus.OK);
            response.setObject(data);
        } catch (Exception e) {
            response.setMessage("INTERNAL ERROR ");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setObject(null);

        }

        return response;
    }

    @Override
    public Boolean delete(String code) {

        Long data = bookRepository.getCountBooksByCode(code);

        if (data > 0) {
            return false;
        }
        bookRepository.deleteById(code);
        return true;
    }

    @Override
    public void update(BookUpdateDto dto) {
        Book en = new Book(dto.getCode(), dto.getTitle(), dto.getCategoryName(),
                dto.getAuthorId().longValue(), dto.getIsBorrowed(), dto.getSummary(),
                dto.getReleaseDate(), dto.getTotalPage());
        bookRepository.save(en);

    }

    @Override
    public BookUpdateDto getBooksBycodeUpdate(String bookCode) {
        return bookRepository.getBooksBycodeUpdate(bookCode);
    }

    @Override
    public String generateBookCodeByCategory(String categoryName) {

        int randomed = 1000;
        Random randomer = new Random();
        int generatedValue = randomer.nextInt(randomed);
        String generated = categoryName.substring(0, 2).toUpperCase() + generatedValue;
        boolean checker = true;
//       IF THIS BOOKS CODE WAS USED
        while (checker) {
            if (bookRepository.getCountBooksByCode(generated) > 0) {
                generatedValue++;
            } else {
                generated = categoryName.substring(0, 2).toUpperCase() + generatedValue;
                checker = false;
            }
        }
        return generated;
    }

}
