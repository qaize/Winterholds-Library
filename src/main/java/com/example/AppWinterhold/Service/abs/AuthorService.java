package com.example.AppWinterhold.Service.abs;

import com.example.AppWinterhold.Dto.Author.AuthorIndexDto;
import com.example.AppWinterhold.Dto.Author.AuthorIndexDtoV2;
import com.example.AppWinterhold.Dto.Author.AuthorInsertDto;
import com.example.AppWinterhold.Dto.Author.AuthorUpdateDto;
import com.example.AppWinterhold.Dto.Rest.Request.Author.AuthorRequestDTO;
import com.example.AppWinterhold.Entity.Author;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import javax.persistence.Tuple;
import java.util.List;

public interface AuthorService {
    Boolean delete(Long id);
    Long getCountPage(String name);
    void update(AuthorUpdateDto dto);
    void insert(AuthorInsertDto dto);
    AuthorIndexDto getAuthorById(Long id) throws JsonProcessingException;
    public List<AuthorIndexDto> getAllAuthor();
    AuthorInsertDto getAuthorByIdinsert(Long id);
    public List<AuthorIndexDto> getListAuthorBySearch(Integer page, String name) throws JsonProcessingException;
    public List<AuthorIndexDto> getListAuthorBySearchV2(AuthorIndexDtoV2 authorIndexDtoV2) throws JsonProcessingException;

    ResponseEntity<Object> getAllAuthorTuple(AuthorRequestDTO authorRequestDTO);

    Page<AuthorIndexDto> getAllAuthorWithPage();

    ResponseEntity<Object> geAllAuthorByTupple(AuthorRequestDTO authorRequestDTO);
}
