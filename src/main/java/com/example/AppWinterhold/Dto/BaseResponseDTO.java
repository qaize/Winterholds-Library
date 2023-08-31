package com.example.AppWinterhold.Dto;


import com.example.AppWinterhold.Dto.Author.AuthorIndexDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponseDTO {

    private Object data;
    private MetaData metaData;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MetaData {
        private long Total;
        private Integer totalPage;
        private Integer size;

    }


}
