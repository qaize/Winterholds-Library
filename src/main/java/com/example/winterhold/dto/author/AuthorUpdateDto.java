package com.example.winterhold.dto.author;
import com.example.winterhold.validation.TodayTime;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AuthorUpdateDto {
    @NotNull
    private Long id;
    @NotBlank(message = "Title Cannot Blank!")
    @NotNull(message = "title cannot null")
    @Size(max = 10, message = "min 10 char")
    private String title; /*10 char NN*/

    @NotBlank(message = "not blank")
    @NotNull(message = "First name cannot null")
    @Size(max = 10, message = "max 10 char")
    private String firstName;/*50 char NN*/

    @Size(max = 50, message = "max 50 char")
    private String lastName; /*50 char N*/

    @TodayTime(message = "You Cannot insert date more than today! ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Please Insert Your Birth Date")
    private LocalDate birthDate;/*NN*/

    @TodayTime(message = "You Cannot insert date more than today! ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deceasedDate;/*N*/

    @Size(max = 50, message = "max 50 char")
    private String education;/*50 char N*/

    @Size(max = 500, message = "max 500 char")
    private String summary; /*500 char N*/
}
