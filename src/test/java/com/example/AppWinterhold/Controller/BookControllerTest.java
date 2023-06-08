package com.example.AppWinterhold.Controller;

import com.example.AppWinterhold.Dao.BookRepository;
import com.example.AppWinterhold.Dto.Book.BookIndexDto;
import com.example.AppWinterhold.Dto.Rest.ResponseCrudRestDto;
import com.example.AppWinterhold.Service.imp.BookServiceImp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BookControllerTest {

    @MockBean
    BookServiceImp bookServiceImp;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Mock
    private BookRepository bookRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testGetAllBooks() throws Exception {

        BookIndexDto bookIndexDto = new BookIndexDto();

        List<BookIndexDto> bookIndexDtoList = new ArrayList<>();

        bookIndexDtoList.add(bookIndexDto);

        ResponseCrudRestDto baseResponse = new ResponseCrudRestDto();
        baseResponse.setMessage("SUCCESS");
        baseResponse.setStatus(HttpStatus.OK);
        baseResponse.setObject(bookIndexDtoList);
        when(bookServiceImp.getAll()).thenReturn(baseResponse);
        String jsonStr = objectMapper.writeValueAsString(bookIndexDtoList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/book/getBooks")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(jsonStr);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testGetBooksByCode() throws Exception {

        BookIndexDto bookIndexDto = new BookIndexDto();
        bookIndexDto.setCode("ASDF");

        ResponseCrudRestDto baseResponse = new ResponseCrudRestDto();
        when(bookServiceImp.getBooksBycode2("ASDF")).thenReturn(baseResponse);
        String jsonStr = objectMapper.writeValueAsString(bookIndexDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/book?code="+bookIndexDto.getCode())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(jsonStr);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }


}
