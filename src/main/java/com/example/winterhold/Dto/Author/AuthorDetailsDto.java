package com.example.winterhold.Dto.Author;


import java.time.LocalDate;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AuthorDetailsDto {
    private Long id;

    private String title; /*10 char NN*/
    private String firstName;/*50 char NN*/

    private String lastName; /*50 char N*/

    private LocalDate birthDate;/*NN*/

    private LocalDate deceasedDate;/*N*/

    private String education;/*50 char N*/

    private String summary; /*500 char N*/
}
