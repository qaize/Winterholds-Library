package com.example.winterhold.Dto.Author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AuthorIndexDtoV2 {

    private String title; /*10 char NN*/
    private String firstName;/*50 char NN*/

    private String lastName; /*50 char N*/
    private String fullname; /*50 char N*/

    private Integer page;
    private Integer dataCount;



    public AuthorIndexDtoV2( String title, String firstName, String lastName,  Integer page) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullname = title+"."+firstName+" "+lastName;
        this.page = page;
    }
}
