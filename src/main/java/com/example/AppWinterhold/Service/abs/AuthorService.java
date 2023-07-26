package com.example.AppWinterhold.Service.abs;

import com.example.AppWinterhold.Dto.Author.AuthorIndexDto;
import com.example.AppWinterhold.Dto.Author.AuthorIndexDtoV2;
import com.example.AppWinterhold.Dto.Author.AuthorInsertDto;
import com.example.AppWinterhold.Dto.Author.AuthorUpdateDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface AuthorService {
    public List<AuthorIndexDto> getListAuthorBySearch(Integer page, String name) throws JsonProcessingException;

    public List<AuthorIndexDto> getListAuthorBySearchV2(AuthorIndexDtoV2 authorIndexDtoV2) throws JsonProcessingException;

    public List<AuthorIndexDto> getAll();

    Long getCountPage(String name);

    void insert(AuthorInsertDto dto);

    AuthorIndexDto getAuthorById(Long id);

    Boolean delete(Long id);

    AuthorInsertDto getAuthorByIdinsert(Long id);

    void update(AuthorUpdateDto dto);
}
