package com.example.winterhold.service.abs;

import com.example.winterhold.Dto.BaseResponseDTO;
import com.example.winterhold.Dto.Book.BookIndexDto;
import com.example.winterhold.Dto.Book.BookInsertDto;
import com.example.winterhold.Dto.Book.BookUpdateDto;

import java.util.List;

public interface BookService {
    Boolean delete(String code);
    BaseResponseDTO<Object> getAll();
    void insert(BookInsertDto dto);
    void update(BookUpdateDto dto);
    List<BookIndexDto> getAvailableBook();
    BookUpdateDto getBooksById(String bookCode);
    BookIndexDto getBooksBycode(String bookCode);
    List<BookIndexDto> getlistBooksByAuthorId(Long id);
    BaseResponseDTO<Object> getBooksBycode2(String bookCode);
    BookUpdateDto getBooksBycodeUpdate(String bookCode);
    String generateBookCodeByCategory(String categoryName);
    List<BookIndexDto> getlistBooksByCategoryName(String categoryName);
    List<BookIndexDto> getlistBooksByCategoryName(String categoryName,String title,String author);

}
