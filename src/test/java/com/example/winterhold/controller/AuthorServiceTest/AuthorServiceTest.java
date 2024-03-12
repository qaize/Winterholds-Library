package com.example.winterhold.controller.AuthorServiceTest;


import com.example.winterhold.Dao.AuthorRepository;
import com.example.winterhold.Dto.Author.AuthorIndexDto;
import com.example.winterhold.Dto.Rest.Request.Author.AuthorRequestDTO;
import com.example.winterhold.Service.imp.AuthorServiceImp;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
