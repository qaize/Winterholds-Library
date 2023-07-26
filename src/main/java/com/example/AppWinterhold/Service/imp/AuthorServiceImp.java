package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Controller.Model.BaseController;
import com.example.AppWinterhold.Dao.AuthorRepository;
import com.example.AppWinterhold.Dto.Author.AuthorIndexDto;
import com.example.AppWinterhold.Dto.Author.AuthorIndexDtoV2;
import com.example.AppWinterhold.Dto.Author.AuthorInsertDto;
import com.example.AppWinterhold.Dto.Author.AuthorUpdateDto;
import com.example.AppWinterhold.Dto.BaseResponseDTO;
import com.example.AppWinterhold.Entity.Author;
import com.example.AppWinterhold.Service.abs.AuthorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.pro.packaged.B;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.AppWinterhold.Const.actionConst.*;

@Service
public class AuthorServiceImp implements AuthorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorServiceImp.class);

    final private LogServiceImpl logService;
    private AuthorRepository authorRepository;
    private BaseController baseController;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public AuthorServiceImp(AuthorRepository authorRepository,BaseController baseController, LogServiceImpl logService) {
        this.authorRepository = authorRepository;
        this.baseController = baseController;
        this.logService = logService;
    }

    @Override
    public List<AuthorIndexDto> getListAuthorBySearch(Integer page, String name) throws JsonProcessingException {
        BaseResponseDTO responseDTO = new BaseResponseDTO();

        Integer row = 10;
//        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> http = new HttpEntity<String>("Params",httpHeaders);
        Pageable paging = PageRequest.of(page-1,row, Sort.by("id").descending());

//        ResponseEntity< String > result = restTemplate.exchange("http://localhost:7081/winterhold/api/author/getAuthor", HttpMethod.GET, http,
//                String.class);
//
//        BaseResponseDTO res = objectMapper.readValue(result.getBody(),BaseResponseDTO.class);
//
//        for (AuthorIndexDto a:res.getData()
//             ) {
//        System.out.println(a.getFullname());
//
//        }


        return authorRepository.getListAuthorBySearch(name,paging);
    }
    @Override
    public List<AuthorIndexDto> getListAuthorBySearchV2(AuthorIndexDtoV2 authorIndexDtoV2) throws JsonProcessingException {
//        Integer row = 10;
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        ObjectMapper objectMapper = new ObjectMapper();
//        String data = objectMapper.writeValueAsString(authorIndexDtoV2);
//        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        HttpEntity<String> http = new HttpEntity<String>(data,httpHeaders);
        Pageable paging = PageRequest.of(authorIndexDtoV2.getPage()-1,authorIndexDtoV2.getDataCount(), Sort.by("id").descending());

        return authorRepository.getListAuthorBySearch(authorIndexDtoV2.getFullname(),paging);
    }

    @Override
    public List<AuthorIndexDto> getAll() {
        List<Author> a = authorRepository.findAll();
        Integer length = a.size();
        return authorRepository.getAll();
    }

    @Override
    public Long getCountPage(String name) {
        Integer row = 10;
        Double totalData =(double) authorRepository.getCountPage(name);
        Long totaPage =(long)  Math.ceil(totalData/row);

        return totaPage;
    }

    @Override
    public void insert(AuthorInsertDto dto) {
        Author en = new Author();
        try{
            InsertAccountMapper(en,dto);
            authorRepository.save(en);
            logService.saveLogs(AUTHOR,SUCCESS, INSERT);
        }
        catch(Exception ex){
            logService.saveLogs(AUTHOR,FAILED, INSERT);
        }
    }

    public void InsertAccountMapper(Author author, AuthorInsertDto authorInsertDto){
        String createdBy = baseController.getCurrentLogin();
        author.setId(authorInsertDto.getId());
        author.setTitle(authorInsertDto.getTitle());
        author.setFirstName(authorInsertDto.getFirstName());
        author.setLastName(authorInsertDto.getLastName());
        author.setBirthDate(authorInsertDto.getBirthDate());
        author.setDeceasedDate(authorInsertDto.getDeceasedDate());
        author.setEducation(authorInsertDto.getEducation());
        author.setSummary(authorInsertDto.getSummary());
        author.setCreatedBy(createdBy == null || createdBy.equalsIgnoreCase("")
                ? "Unknown":createdBy);
        author.setModifiedBy("");

    }

    public void UpdateAccountMapper(Author author, AuthorUpdateDto authorInsertDto){
        AuthorIndexDto authIndex = authorRepository.getAuthorById(authorInsertDto.getId());
        String modifiedBy = baseController.getCurrentLogin();
        author.setId(authorInsertDto.getId());
        author.setTitle(authorInsertDto.getTitle());
        author.setFirstName(authorInsertDto.getFirstName());
        author.setLastName(authorInsertDto.getLastName());
        author.setBirthDate(authorInsertDto.getBirthDate());
        author.setDeceasedDate(authorInsertDto.getDeceasedDate());
        author.setEducation(authorInsertDto.getEducation());
        author.setSummary(authorInsertDto.getSummary());
        author.setCreatedBy(authIndex.getCreatedBy());
        author.setModifiedBy(modifiedBy == baseController.getCurrentLogin() ? baseController.getCurrentLogin() : "Unknown");
    }

    @Override
    public AuthorIndexDto getAuthorById(Long id) {
        Optional<Author> author = authorRepository.findById(id);
        AuthorIndexDto ath = new AuthorIndexDto(
                author.get().getId(),
                author.get().getTitle(),
                author.get().getFirstName(),
                author.get().getLastName(),
                author.get().getBirthDate(),
                author.get().getDeceasedDate(),
                author.get().getEducation(),
                author.get().getSummary(),
                author.get().getCreatedBy(),
                author.get().getModifiedBy()
        );

        return ath;
    }

    @Override
    public Boolean delete(Long id) {
        var data = authorRepository.findById(id);
        if(data==null){
            logService.saveLogs(AUTHOR,FAILED, DELETE);
            return false;
        }
        authorRepository.deleteById(id);
        logService.saveLogs(AUTHOR,SUCCESS, DELETE);
        return true;
    }

    @Override
    public AuthorInsertDto getAuthorByIdinsert(Long id) {
        return authorRepository.getAuthorByIdinsert(id);
    }

    @Override
    public void update(AuthorUpdateDto dto) {
        Author en = new Author();
        try{
            UpdateAccountMapper(en,dto);
            authorRepository.save(en);
            logService.saveLogs(AUTHOR,SUCCESS, UPDATE);
        }
        catch(Exception ex){
            LOGGER.info(FAILED +" "+ UPDATE);
            logService.saveLogs(AUTHOR,FAILED, UPDATE);
        }
    }
}
