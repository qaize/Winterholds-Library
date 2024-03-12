package com.example.winterhold.Dao;

import com.example.winterhold.Dto.Category.CategoryIndexDto;
import com.example.winterhold.Dto.Category.CategoryInsertDto;
import com.example.winterhold.Entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query("""
            SELECT 
                new com.example.winterhold.Dto.Category.CategoryIndexDto
                ( c.name, c.floor, c.isle, c.bay,COUNT(b.code) )
            FROM 
                Category as c
                            LEFT JOIN c.bookList AS b
            WHERE 
                c.name LIKE %:name%   
            GROUP BY
                c.name, c.floor, c.isle, c.bay
            """)
    Page<CategoryIndexDto> getListCategoryBySearch(String name, Pageable paging);

    @Query("""
            SELECT 
                COUNT(c.name)
            FROM 
                Category as c
            WHERE 
                c.name LIKE %:name% 
            """)
    Long getCountPage(String name);

    @Query("""
            SELECT 
                new com.example.winterhold.Dto.Category.CategoryIndexDto
                ( c.name, c.floor, c.isle, c.bay)
            FROM 
                Category as c
            WHERE 
                c.name = :name
            """)
    CategoryIndexDto getCategoryByCategoryName(@Param("name") String categoryName);

    @Query("""
            SELECT 
                COUNT(c.name)
            FROM 
                Category as c
            WHERE 
                c.name = :s
            """)
    Long getCountCategory(String s);

    @Query("""
            SELECT 
                new com.example.winterhold.Dto.Category.CategoryInsertDto
                (c.name, c.floor, c.isle, c.bay)
            FROM 
                Category as c
            WHERE 
                c.name = :categoryName
            """)
    CategoryInsertDto getCategoryByCategoryInsert(String categoryName);

    @Query("""
            SELECT 
                COUNT(b.categoryName)
            FROM 
                Book AS b
            WHERE 
                b.categoryName = :categoryName
            """)
    Long getCountbooks(String categoryName);

    @Query("""
            SELECT 
                new com.example.winterhold.Dto.Category.CategoryIndexDto
                (c.name, c.floor, c.isle, c.bay,COUNT(b.code))
            FROM 
                Category as c
                                LEFT JOIN c.bookList AS b             
            GROUP BY 
                c.name, c.floor, c.isle, c.bay
            """)
    List<CategoryIndexDto> getAll();

    @Query("""
            SELECT 
                COUNT(c.name)
            FROM 
                Category AS c
            WHERE 
                c.isle = :isle AND c.floor = :floor 
                AND c.bay = :bay
            """)
    Long validatePlacement(String isle, Integer floor, String bay);
}
