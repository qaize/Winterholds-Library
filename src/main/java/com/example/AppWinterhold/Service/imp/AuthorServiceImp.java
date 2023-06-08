package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Controller.Model.BaseController;
import com.example.AppWinterhold.Dao.AuthorRepository;
import com.example.AppWinterhold.Dto.Author.AuthorIndexDto;
import com.example.AppWinterhold.Dto.Author.AuthorIndexDtoV2;
import com.example.AppWinterhold.Dto.Author.AuthorInsertDto;
import com.example.AppWinterhold.Dto.Author.AuthorUpdateDto;
import com.example.AppWinterhold.Entity.Author;
import com.example.AppWinterhold.Service.abs.AuthorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class AuthorServiceImp implements AuthorService {


    private AuthorRepository authorRepository;
    private BaseController baseController;

    @Autowired
    public AuthorServiceImp(AuthorRepository authorRepository,BaseController baseController) {
        this.authorRepository = authorRepository;
        this.baseController = baseController;
    }

    @Override
    public List<AuthorIndexDto> getListAuthorBySearch(Integer page, String name) {
        Integer row = 10;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> http = new HttpEntity<String>("Params",httpHeaders);
        Pageable paging = PageRequest.of(page-1,row, Sort.by("id").descending());

        ResponseEntity< String > result = restTemplate.exchange("http://localhost:7081/winterhold/api/author/getAuthor", HttpMethod.GET, http,
                String.class);

        System.out.println(result);

        return authorRepository.getListAuthorBySearch(name,paging);
    }
    @Override
    public List<AuthorIndexDto> getListAuthorBySearchV2(AuthorIndexDtoV2 authorIndexDtoV2) throws JsonProcessingException {
        Integer row = 10;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        ObjectMapper objectMapper = new ObjectMapper();
        String data = objectMapper.writeValueAsString(authorIndexDtoV2);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> http = new HttpEntity<String>(data,httpHeaders);
        Pageable paging = PageRequest.of(authorIndexDtoV2.getPage()-1,authorIndexDtoV2.getDataCount(), Sort.by("id").descending());

        ResponseEntity< String > result = restTemplate.exchange("http://localhost:7081/winterhold/api/author/getDataPaging", HttpMethod.GET, http,
                String.class);

        System.out.println(result);
        return authorRepository.getListAuthorBySearch(authorIndexDtoV2.getFullname(),paging);
    }

    @Override
    public List<AuthorIndexDto> getAll() {
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
        }
        catch(Exception ex){
            System.out.println("Unable To Insert!");
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
        return authorRepository.getAuthorById(id);
    }

    @Override
    public Boolean delete(Long id) {
        var data = authorRepository.getCountBooks(id);
        if(data>0){
            return false;
        }
        authorRepository.deleteById(id);
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
        }
        catch(Exception ex){
            System.out.println("Unable To Update!");
        }
    }
}
