package com.example.AppWinterhold.Entity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

}
