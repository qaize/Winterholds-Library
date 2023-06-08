package com.example.AppWinterhold.Dao;

import com.example.AppWinterhold.Dto.Author.AuthorIndexDto;
import com.example.AppWinterhold.Dto.Author.AuthorInsertDto;
import com.example.AppWinterhold.Entity.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author,Long> {

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Author.AuthorIndexDto
            (
                a.id, a.title, a.firstName, a.lastName, a.birthDate,
                a.deceasedDate,a.education,a.summary,a.createdBy,a.modifiedBy
            )
            FROM Author as a
                WHERE CONCAT(a.firstName,' ',a.lastName) LIKE %:name%
            """)
    List<AuthorIndexDto> getListAuthorBySearch(@Param("name") String name, Pageable paging);

    @Query("""
            SELECT COUNT(a.id)
            FROM Author as a
                WHERE CONCAT(a.firstName,' ',a.lastName) LIKE %:name%
            """)
    Long getCountPage(String name);

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Author.AuthorIndexDto
            (
                a.id, a.title, a.firstName, a.lastName, a.birthDate,
                a.deceasedDate,a.education,a.summary,a.createdBy,a.modifiedBy
            )
            FROM Author as a
            """)
    List<AuthorIndexDto> getAll();

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Author.AuthorIndexDto
            (
                a.id, a.title, a.firstName, a.lastName, a.birthDate,
                a.deceasedDate,a.education,a.summary,a.createdBy,a.modifiedBy
            )
            FROM Author as a
                WHERE a.id = :id
            """)
    AuthorIndexDto getAuthorById(Long id);

    @Query("""
            SELECT COUNT(b.code)
            FROM Book AS b
                WHERE b.authorId = :id
            """)
    Long getCountBooks(Long id);

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Author.AuthorInsertDto
            (
                a.id, a.title, a.firstName, a.lastName, a.birthDate,
                a.deceasedDate,a.education,a.summary
            )
            FROM Author as a
                WHERE a.id = :id
            """)
    AuthorInsertDto getAuthorByIdinsert(Long id);
}
