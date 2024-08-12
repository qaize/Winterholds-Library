package com.example.winterhold.service.abs;

import com.example.winterhold.dto.author.AuthorIndexDto;
import com.example.winterhold.dto.author.AuthorIndexDtoV2;
import com.example.winterhold.dto.author.AuthorInsertDto;
import com.example.winterhold.dto.author.AuthorUpdateDto;
import com.example.winterhold.dto.BaseResponseDTO;
import com.example.winterhold.dto.models.DataDTO;
import com.example.winterhold.dto.rest.Request.Author.AuthorRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AuthorService {
    Boolean delete(Long id);
    Long getCountPage(String name);
    void update(AuthorUpdateDto dto);
    void insert(AuthorInsertDto dto);
    AuthorIndexDto getAuthorById(Long id) throws JsonProcessingException;
    public List<AuthorIndexDto> getAllAuthor();
    AuthorInsertDto getAuthorByIdinsert(Long id);
    DataDTO<List<AuthorIndexDto>> getListAuthorBySearch(Integer page, String name) throws JsonProcessingException;
    List<AuthorIndexDto> getListAuthorBySearchV2(AuthorIndexDtoV2 authorIndexDtoV2) throws JsonProcessingException;

    ResponseEntity<Object> getAllAuthorTuple(AuthorRequestDTO authorRequestDTO);

    Page<AuthorIndexDto> getAllAuthorWithPage(Integer page,String nama);

    ResponseEntity<Object> geAllAuthorByTupple(AuthorRequestDTO authorRequestDTO);

    BaseResponseDTO<String> getHtml() throws JsonProcessingException;
}
