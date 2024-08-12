package com.example.winterhold.dto.category;

import com.example.winterhold.validation.BookPlacing;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@BookPlacing(message = "This Place Already Taken",isle = "isle",bay = "bay",floor = "floor")
public class CategoryUpdateDto {

    @Size(max = 10, message = "min 10 char")
    @NotBlank(message = "Category name cannot blank!")
    @NotNull(message = "Cannot be Null")
    private String name; /*100 NN*/

    @Min(value = 1,message = "minimal 1")
    @NotNull(message = "Floor cannot null")
    private Integer floor;/*NN*/

    @NotBlank(message = "Isle Cannot blank")
    @NotNull(message = "Isle cannot null")
    @Size(max = 2,message = "Isle must not more than 2 digits")
    private String isle;/*10 NN*/

    @NotBlank(message = "Bay Cannot blank")
    @NotNull(message = "Bay cannot null")
    private String bay;/*10 NN*/

}
