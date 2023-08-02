package com.example.AppWinterhold.Service.abs;

import com.example.AppWinterhold.Dto.Book.BookIndexDto;
import com.example.AppWinterhold.Dto.Book.BookInsertDto;
import com.example.AppWinterhold.Dto.Book.BookUpdateDto;
import com.example.AppWinterhold.Dto.Rest.ResponseCrudRestDto;

import java.util.List;

public interface BookService {
    List<BookIndexDto> getlistBooksByCategoryName(String categoryName);
    List<BookIndexDto> getlistBooksByCategoryName(String categoryName,String title,String author);

    void insert(BookInsertDto dto);

    ResponseCrudRestDto getAll();
//    List<BookIndexDto> getAll();

    List<BookIndexDto> getlistBooksByAuthorId(Long id);

    BookUpdateDto getBooksById(String bookCode);

    List<BookIndexDto> getAvailableBook();

    BookIndexDto getBooksBycode(String bookCode);

    ResponseCrudRestDto getBooksBycode2(String bookCode);

    Boolean delete(String code);

    void update(BookUpdateDto dto);

    BookUpdateDto getBooksBycodeUpdate(String bookCode);


    String generateBookCodeByCategory(String categoryName);
}
