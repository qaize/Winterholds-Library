package com.example.winterhold.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "Author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "Title")
    private String title; /*10 char NN*/
    @Column(name = "FirstName")
    private String firstName;/*50 char NN*/
    @Column(name = "LastName")
    private String lastName; /*50 char N*/
    @Column(name = "BirthDate")
    private LocalDate birthDate;/*NN*/
    @Column(name = "DeceasedDate")
    private LocalDate deceasedDate;/*N*/
    @Column(name = "Education")
    private String education;/*50 char N*/
    @Column(name = "Summary")
    private String summary; /*500 char N*/

    @Column(name = "createdBy")
    private String createdBy; /*500 char N*/
    @Column(name = "modifiedBy")
    private String modifiedBy; /*500 char N*/
}
