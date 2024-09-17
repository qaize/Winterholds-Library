package com.example.winterhold.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "Account")
public class Account {

    @Id
    @Column(name = "Username")
    private String username; /* 20 char not nll */

    @Column(name = "Password")
    private String password; /*200 char not nll*/

    @Column(name = "isLocked")
    private Boolean islocked;

    @Column(name="wrongCount")
    private Integer countWrong;

    @Column(name="name")
    private String userLogin;

    @Column(name = "role")
    private String role;

}
