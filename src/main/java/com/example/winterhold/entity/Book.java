package com.example.winterhold.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Data
@Builder
@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "Code")
    private String code;/*20 NN*/
    @Column(name = "Title")
    private String title;/*100 NN*/

    @Column(name = "CategoryName")
    private String categoryName;/*100 NN*/

    @Column(name = "AuthorId")
    private Long authorId;/* NN*/

    @Column(name = "IsBorrowed")
    private Boolean isBorrowed;/*NN*/
    @Column(name = "Summary")
    private String summary;/*500 N*/
    @Column(name = "ReleaseDate")
    private LocalDate releaseDate;/*N*/
    @Column(name = "TotalPage")
    private Integer totalPage; /*N*/

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "inBorrow")
    private Integer inBorrow;

    @ManyToOne
    @JoinColumn(name = "AuthorId",insertable = false,updatable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "CategoryName",insertable = false,updatable = false)
    private Category category;



    public Book(String code, String title, String categoryName, Long authorId, Boolean isBorrowed, String summary,
                LocalDate releaseDate, Integer totalPage, Integer quantity, Integer inBorrow) {
        this.code = code;
        this.title = title;
        this.categoryName = categoryName;
        this.authorId = authorId;
        this.isBorrowed = isBorrowed;
        this.summary = summary;
        this.releaseDate = releaseDate;
        this.totalPage = totalPage;
        this.quantity = quantity;
        this.inBorrow = inBorrow;
    }
}
