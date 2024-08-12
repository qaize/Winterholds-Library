package com.example.winterhold.service.imp;

import com.example.winterhold.dao.BookRepository;
import com.example.winterhold.dto.BaseResponseDTO;
import com.example.winterhold.dto.book.BookIndexDto;
import com.example.winterhold.dto.book.BookInsertDto;
import com.example.winterhold.dto.book.BookUpdateDto;
import com.example.winterhold.entity.Book;
import com.example.winterhold.service.abs.BookService;
import com.example.winterhold.utility.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

import static com.example.winterhold.constants.ActionConstants.*;

@Service
public class BookServiceImp implements BookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImp.class);

    private BookRepository bookRepository;

    private ObjectMapper objectMapper;

    @Autowired
    public BookServiceImp(BookRepository bookRepository, ObjectMapper objectMapper) {
        this.bookRepository = bookRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<BookIndexDto> getlistBooksByCategoryName(String categoryName) {
        return bookRepository.getlistBooksByCategoryName(categoryName);
    }
    @Override
    public List<BookIndexDto> getlistBooksByCategoryName(String categoryName, String title, String author) {
        return bookRepository.getlistBooksByCategoryName(categoryName, title, author);
    }

    @Override
    public void insert(BookInsertDto dto) {bookRepository.save(mapInsertBook(dto));}

    private Book mapInsertBook(BookInsertDto dto) {
        String code = generateBookCodeByCategory(dto.getCategoryName());
        Book data = new Book(code, dto.getTitle(), dto.getCategoryName(),
                dto.getAuthorId().longValue(), dto.getIsBorrowed(), dto.getSummary(),
                dto.getReleaseDate(), dto.getTotalPage(), dto.getQuantity(), 0);

        return data;
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
    public BookUpdateDto getBooksBycodeUpdate(String bookCode) { return bookRepository.getBooksBycodeUpdate(bookCode);}

    @Override
    public BaseResponseDTO<Object> getAll() {

        try {
            List<BookIndexDto> data = bookRepository.getAll();
            LOGGER.info(SUCCESS_GET_DATA, objectMapper.writeValueAsString(data));
            return ResponseUtil.insertSuccessResponse(data);
        } catch (Exception e) {
            LOGGER.error(FAILED_GET_DATA, e.getMessage());
            return ResponseUtil.insertFailResponse(e.getMessage());
        }
    }

    @Override
    public BaseResponseDTO<Object> getBooksBycode2(String bookCode) {
        try {
            BookIndexDto data = bookRepository.getBooksBycode(bookCode);
            LOGGER.info(SUCCESS_GET_DATA, objectMapper.writeValueAsString(data));
            return ResponseUtil.insertSuccessResponse(data);
        } catch (Exception e) {
            LOGGER.error(FAILED_GET_DATA, e.getMessage());
            return ResponseUtil.insertFailResponse(e.getMessage());
        }
    }

    @Override
    public Boolean delete(String code) {
        try {
            Long data = bookRepository.getCountBooksByCode(code);
            LOGGER.info(SUCCESS_DELETE_DATA,code);
            return data > 0 ? doDelete(code) : false;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    public Boolean doDelete(String code) {
        bookRepository.deleteById(code);
        LOGGER.info(SUCCESS_DELETE_DATA, code);
        return true;
    }

    @Override
    public void update(BookUpdateDto dto) {
        Book en = new Book(dto.getCode(), dto.getTitle(), dto.getCategoryName(),
                dto.getAuthorId(), dto.getIsBorrowed(), dto.getSummary(),
                dto.getReleaseDate(), dto.getTotalPage(), dto.getQuantity(), dto.getInBorrow());
        bookRepository.save(en);
    }

    @Override
    public String generateBookCodeByCategory(String categoryName) {

        int randomed = 1000;
        Random randomer = new Random();

        int generatedValue = randomer.nextInt(randomed);

        String generated = "";
        boolean checker = true;

        //IF THIS BOOKS CODE WAS USED
        while (checker) {
            generated = categoryName.substring(0, 2).toUpperCase() + generatedValue;
            Long isAvailable = bookRepository.getCountBooksByCode(generated);
            if (isAvailable > 0) {
                generatedValue++;
            } else {
                generated = categoryName.substring(0, 2).toUpperCase() + generatedValue;
                checker = false;
            }
        }
        return generated;
    }

}
