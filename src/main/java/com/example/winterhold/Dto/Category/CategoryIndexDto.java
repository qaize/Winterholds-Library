package com.example.winterhold.Dto.Category;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class CategoryIndexDto {

    private String name; /*100 NN*/

    private Integer floor;/*NN*/

    private String isle;/*10 NN*/

    private String bay;/*10 NN*/

    private Long totalBooks;

    public CategoryIndexDto(String name, Integer floor, String isle, String bay) {
        this.name = name;
        this.floor = floor;
        this.isle = isle;
        this.bay = bay;
    }
}
