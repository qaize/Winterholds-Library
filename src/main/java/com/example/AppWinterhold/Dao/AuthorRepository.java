package com.example.AppWinterhold.Dao;

import com.example.AppWinterhold.Dto.Author.AuthorIndexDto;
import com.example.AppWinterhold.Dto.Author.AuthorInsertDto;
import com.example.AppWinterhold.Entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Tuple;
import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("""
            SELECT 
                new com.example.AppWinterhold.Dto.Author.AuthorIndexDto
                (   a.id, a.title, a.firstName, a.lastName, a.birthDate,
                    a.deceasedDate,a.education,a.summary,a.createdBy,a.modifiedBy )
            FROM 
                Author as a
            WHERE 
                CONCAT(a.firstName,' ',a.lastName) LIKE %:name%
            """)
    List<AuthorIndexDto> getListAuthorBySearch(@Param("name") String name, Pageable paging);

    @Query("""
            SELECT 
                COUNT(a.id)
            FROM
                Author as a
            WHERE 
                CONCAT(a.firstName,' ',a.lastName) LIKE %:name%
            """)
    Long getCountPage(String name);

    @Query("""
            SELECT 
                new com.example.AppWinterhold.Dto.Author.AuthorIndexDto
                (
                    a.id, a.title, a.firstName, a.lastName, a.birthDate,
                    a.deceasedDate,a.education,a.summary,a.createdBy,a.modifiedBy
                )
            FROM 
                Author as a
            """)
    List<AuthorIndexDto> getAll();

    @Query("""
            SELECT 
                new com.example.AppWinterhold.Dto.Author.AuthorIndexDto
                (
                    a.id, a.title, a.firstName, a.lastName, a.birthDate,
                    a.deceasedDate,a.education,a.summary,a.createdBy,a.modifiedBy
                )
            FROM 
                Author as a
            WHERE 
                a.id = :id
            """)
    AuthorIndexDto getAuthorById(Long id);

    @Query("""
            SELECT 
                COUNT(b.code)
            FROM 
                Book AS b
            WHERE b.authorId = :id
            """)
    Long getCountBooks(Long id);

    @Query("""
            SELECT 
                new com.example.AppWinterhold.Dto.Author.AuthorInsertDto
                (
                    a.id, a.title, a.firstName, a.lastName, a.birthDate,
                    a.deceasedDate,a.education,a.summary
                )
            FROM 
                Author as a
            WHERE 
                a.id = :id
            """)
    AuthorInsertDto getAuthorByIdinsert(Long id);

    @Query(nativeQuery = true,value = "" +
            "SELECT \n" +
            " a.Id as id, \n" +
            " a.Title as title,\n" +
            " a.FirstName as firstName, \n" +
            " a.LastName as lastName, \n" +
            " format(a.BirthDate,'yyyy-MM-dd') as birthDate, \n" +
            " format(a.DeceasedDate ,'yyyy-MM-dd') as deceasedDate, \n" +
            " a.Education as education, \n" +
            " a.Summary as summary, \n" +
            " a.createdBy  as createdby, \n" +
            " a.modifiedBy as modifiedBy \n" +
            "FROM Author AS a")
    List<Tuple> getAllByTupple(Pageable page);

    @Query("""
            SELECT 
                new com.example.AppWinterhold.Dto.Author.AuthorIndexDto
                (
                    a.id, a.title, a.firstName, a.lastName, a.birthDate,
                    a.deceasedDate,a.education,a.summary,a.createdBy,a.modifiedBy
                )
            FROM 
                Author as a
            """)
    Page<AuthorIndexDto> getAllAuthorWithPage(Pageable page);

    @Query(nativeQuery = true,value = """
            SELECT\s
             Id as id,\s
             Title as title,
             FirstName as firstName,\s
             LastName as lastName,\s
             format(BirthDate,'yyyy-MM-dd') as birthDate,\s
             format(DeceasedDate ,'yyyy-MM-dd') as deceasedDate,\s
             Education as education,\s
             Summary as summary,\s
             createdBy  as createdby,\s
             modifiedBy as modifiedBy\s
            FROM Author
            """)
    Page<Tuple> getPageAuthorByTuple(Pageable pages);
}
