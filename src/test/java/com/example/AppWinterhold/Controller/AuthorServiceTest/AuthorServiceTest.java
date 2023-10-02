package com.example.AppWinterhold.Controller.AuthorServiceTest;


import com.example.AppWinterhold.Dao.AuthorRepository;
import com.example.AppWinterhold.Dto.Author.AuthorIndexDto;
import com.example.AppWinterhold.Dto.Rest.Request.Author.AuthorRequestDTO;
import com.example.AppWinterhold.Entity.Author;
import com.example.AppWinterhold.Service.imp.AuthorServiceImp;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebMvc
public class AuthorServiceTest {

    @InjectMocks
    AuthorServiceImp authorServiceImp;

    @Mock
    AuthorRepository authorRepository;

    @Test
    @DisplayName(value = "Test Get All Author By Tuple")
    public void testGetAllAuthorByTuple() throws JsonProcessingException {

        AuthorRequestDTO authorRequestDTO = new AuthorRequestDTO(1,5);


//        when(authorRepository.findById(Mockito.any())).thenReturn()

        AuthorIndexDto response = authorServiceImp.getAuthorById(12L);
//        Assertions.assertEquals();


    }



}
