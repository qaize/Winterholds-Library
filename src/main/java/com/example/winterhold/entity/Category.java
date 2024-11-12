package com.example.winterhold.entity;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "Category")
public class Category {

    @Id
    @Column(name = "Name")
    private String name; /*100 NN*/
    @Column(name = "Floor")
    private Integer floor;/*NN*/
    @Column(name = "Isle")
    private String isle;/*10 NN*/
    @Column(name = "Bay")
    private String bay;/*10 NN*/

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryName",insertable = false,updatable = false)
    private List<Book> bookList;

    public Category(String name, Integer floor, String isle, String bay) {
        this.name = name;
        this.floor = floor;
        this.isle = isle;
        this.bay = bay;
    }
}
