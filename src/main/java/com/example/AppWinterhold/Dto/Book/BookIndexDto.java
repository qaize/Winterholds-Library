package com.example.AppWinterhold.Dto.Book;
import lombok.*;
import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class BookIndexDto {

    private String code;/*20 NN*/

    private String title;/*100 NN*/

    private String categoryName;/*100 NN*/

    private String authorName;/* NN*/

    private Boolean isBorrowed;/*NN*/
    private String isBorrowedStr;/*NN*/

    private String summary;/*500 N*/

    private LocalDate releaseDate;/*N*/

    private Integer totalPage; /*N*/

    public BookIndexDto(String code, String title, String categoryName, String authorName,
                        Boolean isBorrowed, String summary, LocalDate releaseDate,
                        Integer totalPage) {
        this.code = code;
        this.title = title;
        this.categoryName = categoryName;
        this.authorName = authorName;
        this.isBorrowed = isBorrowed;
        this.summary = summary;
        this.releaseDate = releaseDate;
        this.totalPage = totalPage;
        this.isBorrowedStr = isBorrowed ? "Not Available" : "Available";
    }
}
