package com.example.winterhold.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@Getter @Setter
@NoArgsConstructor
@Entity
public class Product {
    @Id
    private String id;
    private String name;
}
