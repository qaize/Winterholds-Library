package com.example.winterhold.Dto.Author;
import lombok.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AuthorIndexDto {

    private Long id;

    private String title; /*10 char NN*/
    private String firstName;/*50 char NN*/

    private String lastName; /*50 char N*/
    private String fullname; /*50 char N*/

    private LocalDate birthDate;/*NN*/

    private LocalDate deceasedDate;/*N*/
    private String deceasedDateStr;/*N*/

    private Long age;

    private String education;/*50 char N*/

    private String summary; /*500 char N*/
    private String createdBy; /*500 char N*/
    private String modifiedBy; /*500 char N*/

    public AuthorIndexDto(Long id, String title, String firstName, String lastName, LocalDate birthDate,
                          LocalDate deceasedDate, String education, String summary, String createdBy,String modifiedBy) {
        this.id = id;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.deceasedDate = deceasedDate;
        this.education = education;
        this.summary = summary;
        this.age = ChronoUnit.YEARS.between(birthDate,LocalDate.now());
        this.deceasedDateStr = deceasedDate==null ? "Alive" : "Deceased";
        this.fullname = title+"."+firstName+" "+lastName;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }
}
