package com.example.winterhold.dto.category;
import com.example.winterhold.validation.BookPlacing;
import com.example.winterhold.validation.CategoryName;
import jakarta.validation.constraints.Min;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@BookPlacing(message = "This Place Already Taken",isle = "isle",bay = "bay",floor = "floor")
public class CategoryInsertDto {

    @CategoryName(message = "Category ini telah Tersedia")
    @NotBlank(message = "Category name cannot blank!")
    @Size(max = 10, message = "min 10 char")
    @NotNull(message = "Category Name Cannot Null")
    private String name; /*100 NN*/

    @Min(value = 1,message = "minimal 1")
    @NotNull(message = "Floor cannot null")
    private Integer floor;/*NN*/

    @NotBlank(message = "Isle Cannot blank")
    @NotNull(message = "Isle cannot null")
    private String isle;/*10 NN*/

    @NotBlank(message = "Bay Cannot blank")
    @NotNull(message = "Bay cannot null")
    private String bay;/*10 NN*/
}
