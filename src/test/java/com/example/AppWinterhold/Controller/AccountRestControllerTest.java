//package com.example.AppWinterhold.Controller;
//
//import com.example.AppWinterhold.Dao.AccountRepository;
//import com.example.AppWinterhold.Dto.Account.AccountIndexDto;
//import com.example.AppWinterhold.Service.imp.AccountServiceImp;
//import com.example.AppWinterhold.Service.imp.BookServiceImp;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@AutoConfigureMockMvc
//public class AccountRestControllerTest {
//
//    @InjectMocks
//    AccountServiceImp accountServiceImp;
//
//    @Autowired
//    MockMvc mockMvc;
//
//
//    @Mock
//    AccountRepository accountRepository;
//
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    @Autowired
//    WebApplicationContext context;
//
//    @BeforeEach
//    public void setUp(){
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//    }
//
//    @Test
//    public void testGetAll() throws Exception {
//
//        List<AccountIndexDto> accountIndexDtoList = new ArrayList<>();
//        AccountIndexDto accountIndexDto = mapAccountIndexDto();
//        accountIndexDtoList.add(accountIndexDto);
//
//        when(accountServiceImp.getlist()).thenReturn(accountIndexDtoList);
//
//        String jsonStr = objectMapper.writeValueAsString(accountIndexDtoList);
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/account/getAccount")
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(jsonStr);
//
//        mockMvc.perform(requestBuilder).andExpect(status().isOk());
//    }
//
//    private AccountIndexDto mapAccountIndexDto(){
//        AccountIndexDto accountIndexDto = new AccountIndexDto();
//        accountIndexDto.setUsername("test");
//        accountIndexDto.setPassword("test");
//
//        return accountIndexDto;
//    }
//}
