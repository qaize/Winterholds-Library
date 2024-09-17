package com.example.winterhold.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "master_account")
public class MasterAccount {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "MembershipNumber")
    private String membershipNumber;

    @Column(name = "balance")
    private int balance;

}
