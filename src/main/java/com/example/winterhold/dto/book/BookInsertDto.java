package com.example.winterhold.dto.book;

import com.example.winterhold.validation.TodayTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class BookInsertDto {

//    @BookCode(message = "Code Already Used,Please Insert Diffrent Code")
//    @NotBlank(message = "Please Insert Book Code ")
//    @Size(max = 20, message = "Max 20 charachter")
//    @NotNull(message = "this Field cannot be null")
//    private String code;/*20 NN*/

    @Size(max = 100, message = "Max 100 charachter")
    @NotNull(message = "this Field cannot be null")
    @NotBlank(message = "Please Insert The Title")
    private String title;/*100 NN*/

    @Size(max = 100, message = "Max 100 charachter")
    @NotNull(message = "this Field cannot be null")
    @NotBlank(message = "Fill the form")
    private String categoryName;/*100 NN*/

    @NotNull(message = "this Field cannot be null")
    private Long authorId;/* NN*/

    @NotNull(message = "this Field cannot be null")
    private Boolean isBorrowed;/*NN*/

    @Size(max = 500, message = "Max 500 charachter")
    private String summary;/*500 N*/

    @TodayTime(message = "cannot more than today")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;/*N*/

    private Integer totalPage; /*N*/

    @Min(value = 1,message = "Minimal 1 book")
    private Integer quantity;
}
