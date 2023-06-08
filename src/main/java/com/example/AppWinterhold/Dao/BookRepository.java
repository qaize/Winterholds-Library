package com.example.AppWinterhold.Dao;

import com.example.AppWinterhold.Dto.Book.BookIndexDto;
import com.example.AppWinterhold.Dto.Book.BookInsertDto;
import com.example.AppWinterhold.Dto.Book.BookUpdateDto;
import com.example.AppWinterhold.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,String> {


    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Book.BookIndexDto
            (
              
            b.code,b.title,b.categoryName,CONCAT(a.title,'.',a.firstName,' ',a.lastName),b.isBorrowed,b.summary,
            b.releaseDate,b.totalPage
          
            )
            FROM Book AS b
                LEFT JOIN b.author AS a
                LEFT JOIN b.category AS c
          
            WHERE b.categoryName LIKE %:categoryName%
            
            """)
    List<BookIndexDto> getlistBooksByCategoryName(@Param("categoryName") String categoryName);
    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Book.BookIndexDto
            (
              
            b.code,b.title,b.categoryName,CONCAT(a.title,'.',a.firstName,' ',a.lastName),b.isBorrowed,b.summary,
            b.releaseDate,b.totalPage
          
            )
            FROM Book AS b
                LEFT JOIN b.author AS a
                LEFT JOIN b.category AS c
          
            WHERE b.title LIKE %:title% AND b.categoryName LIKE %:categoryName% AND CONCAT(a.firstName,' ',a.lastName) LIKE %:author%
            
            """)
    List<BookIndexDto> getlistBooksByCategoryName(@Param("categoryName") String categoryName, String title, String author);

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Book.BookIndexDto
            (
              
            b.code,b.title,b.categoryName,CONCAT(a.title,'.',a.firstName,' ',a.lastName),b.isBorrowed,b.summary,
            b.releaseDate,b.totalPage
            )
            FROM Book AS b
                LEFT JOIN b.author AS a
                LEFT JOIN b.category AS c
            """)
    List<BookIndexDto> getAll();

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Book.BookIndexDto
            (
              
            b.code,b.title,b.categoryName,CONCAT(a.title,'.',a.firstName,' ',a.lastName),b.isBorrowed,b.summary,
            b.releaseDate,b.totalPage
            )
            FROM Book AS b
                LEFT JOIN b.author AS a
                
                WHERE a.id = :id
            """)
    List<BookIndexDto> getlistBooksByAuthorId(Long id);

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Book.BookInsertDto
            (
              
            b.code,b.title,b.categoryName,b.authorId,b.isBorrowed,b.summary,
            b.releaseDate,b.totalPage
            )
            FROM Book AS b
                WHERE b.code = :bookCode
            """)
    BookInsertDto getBooksById(String bookCode);

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Book.BookIndexDto
            (
            b.code,b.title,b.categoryName,CONCAT(a.title,'.',a.firstName,' ',a.lastName),b.isBorrowed,b.summary,
            b.releaseDate,b.totalPage
            )
            FROM Book AS b
                LEFT JOIN b.author AS a
                
                WHERE b.isBorrowed = false
            """)
    List<BookIndexDto> getAvailableBook();

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Book.BookIndexDto
            (
              
            b.code,b.title,b.categoryName,CONCAT(a.title,'.',a.firstName,' ',a.lastName),b.isBorrowed,b.summary,
            b.releaseDate,b.totalPage
            )
            FROM Book AS b
                LEFT JOIN b.author AS a
      
                WHERE b.code = :bookCode
            """)
    BookIndexDto getBooksBycode(String bookCode);

    @Query("""
            SELECT COUNT(l.id)
            FROM Loan AS l
              WHERE l.bookCode = :code
            """)
    Long getCountBooks(String code);

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Book.BookUpdateDto
            (
              
            b.code,b.title,b.categoryName,b.authorId,b.isBorrowed,b.summary,
            b.releaseDate,b.totalPage
            )
            FROM Book AS b
                WHERE b.code = :bookCode
            """)
    BookUpdateDto getBooksBycodeUpdate(String bookCode);

}
