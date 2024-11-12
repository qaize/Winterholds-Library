package com.example.winterhold.service.imp;

import com.example.winterhold.controller.model.BaseController;
import com.example.winterhold.customexception.CustomException;
import com.example.winterhold.repository.AuthorRepository;
import com.example.winterhold.dto.author.AuthorIndexDto;
import com.example.winterhold.dto.author.AuthorIndexDtoV2;
import com.example.winterhold.dto.author.AuthorInsertDto;
import com.example.winterhold.dto.author.AuthorUpdateDto;
import com.example.winterhold.dto.BaseResponseDTO;
import com.example.winterhold.dto.models.DataDTO;
import com.example.winterhold.dto.rest.Request.Author.AuthorRequestDTO;
import com.example.winterhold.entity.Author;
import com.example.winterhold.service.abs.AuthorService;
import com.example.winterhold.utility.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import javax.persistence.Tuple;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.winterhold.constants.ActionConstants.*;

@Service
@Slf4j
public class AuthorServiceImp implements AuthorService {

    private final LogServiceImpl logService;
    private final AuthorRepository authorRepository;
    private final BaseController baseController;

    private final TemplateEngine engine;


    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public AuthorServiceImp(AuthorRepository authorRepository, BaseController baseController, LogServiceImpl logService, ObjectMapper objectMapper,TemplateEngine engine) {
        this.authorRepository = authorRepository;
        this.baseController = baseController;
        this.logService = logService;
        this.objectMapper = objectMapper;
        this.engine = engine;
    }


    @Override
    public DataDTO<List<AuthorIndexDto>> getListAuthorBySearch(Integer page, String name) {

        int row = 10;
        int flag = 0;
        String message = "";
        try {

            Pageable paging = PageRequest.of(page - 1, row, Sort.by("id").descending());
            Page<AuthorIndexDto> authorData = authorRepository.getListAuthorBySearch(name, paging);

            if (authorData.isEmpty()) {
                flag = 1;
                message = INDEX_EMPTY;
            }

            log.info(SUCCESS_GET_DATA, AUTHOR);
            return DataDTO.<List<AuthorIndexDto>>builder()
                    .flag(flag)
                    .totalPage((long) authorData.getTotalPages())
                    .message(message)
                    .data(authorData.getContent())
                    .build();

        } catch (Exception e) {
            log.error(e.getMessage());
            return DataDTO.<List<AuthorIndexDto>>builder()
                    .flag(flag)
                    .message(message)
                    .build();
        }
    }

    @Override
    public List<AuthorIndexDto> getListAuthorBySearchV2(AuthorIndexDtoV2 authorIndexDtoV2) throws JsonProcessingException {

        Pageable paging = PageRequest.of(authorIndexDtoV2.getPage() - 1, authorIndexDtoV2.getDataCount(), Sort.by("id").descending());
        Page<AuthorIndexDto> data = authorRepository.getListAuthorBySearch(authorIndexDtoV2.getFullname(), paging);
        return data.getContent();
    }

    @Override
    public ResponseEntity<Object> getAllAuthorTuple(AuthorRequestDTO authorRequestDTO) {
        try {
            Pageable paging = PageRequest.of(authorRequestDTO.getPage() - 1, authorRequestDTO.getDataCount());
            List<Tuple> pages = authorRepository.getAllByTupple(paging);
            List<AuthorIndexDto> authorIndexDtos = mapTupleToAuthor(pages);

            return ResponseEntity.status(HttpStatus.OK).body(authorIndexDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Override
    public List<AuthorIndexDto> getAllAuthor() {
        return authorRepository.getAll();
    }

    @Override
    public Long getCountPage(String name) {

        Integer row = 10;
        Double totalData = (double) authorRepository.getCountPage(name);

        return (long) Math.ceil(totalData / row);
    }

    @Override
    public void insert(AuthorInsertDto dto) {
        Author en = new Author();
        try {
            mapInsert(en, dto);
            authorRepository.save(en);
            logService.saveLogs(AUTHOR, SUCCESS, INSERT);
        } catch (Exception ex) {
            logService.saveLogs(AUTHOR, FAILED, INSERT);
        }
    }

    @Override
    public void update(AuthorUpdateDto dto) {

        Author en = new Author();
        try {
            AuthorIndexDto authIndex = authorRepository.getAuthorById(dto.getId());

            updateAccountMapper(en, authIndex, dto);
            updateValidator(en);

            authorRepository.save(en);
            logService.saveLogs(AUTHOR, SUCCESS, UPDATE);

        } catch (CustomException e) {
            log.info(e.getMessage());
            logService.saveLogs(AUTHOR, ALREADY, UPDATE);
        } catch (Exception ex) {
            log.info(ex.getMessage());
            logService.saveLogs(AUTHOR, FAILED, UPDATE);
        }
    }


    @Override
    public AuthorIndexDto getAuthorById(Long id){
        Optional<Author> author = authorRepository.findById(id);
        AuthorIndexDto dataAuthor = author.map(this::mapAuthorIndexDto).orElse(null);

        log.info(SUCCESS_GET_DATA, dataAuthor.getId());
        return dataAuthor;
    }


    @Override
    public Boolean delete(Long id) {

        log.info("Delete Author : {}", id);

        try {
            var data = authorRepository.findById(id);
            var totalBooks = authorRepository.getCountBooks(id);

            if (data.isPresent()) {

                if (totalBooks > 0) {
                    logService.saveLogs(AUTHOR, FAILED, DELETE);
                    return false;
                }

                authorRepository.deleteById(id);
                logService.saveLogs(AUTHOR, SUCCESS, DELETE);
                log.info("Success Delete author {}",id);
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return false;
    }

    @Override
    public AuthorInsertDto getAuthorByIdInsertion(Long id) {
        return authorRepository.getAuthorByIdinsert(id);
    }


    @Override
    public Page<AuthorIndexDto> getAllAuthorWithPage(Integer pages, String nama) {
        Pageable page = PageRequest.of(pages - 1, 5, Sort.by("id").descending());

        return authorRepository.getAllAuthorWithPage(page, nama);
    }

    @Override
    public ResponseEntity<Object> geAllAuthorByTupple(AuthorRequestDTO authorRequestDTO) {

        try {
            Pageable pages = PageRequest.of(authorRequestDTO.getPage(), authorRequestDTO.getDataCount());
            Page<Tuple> tuples = authorRepository.getPageAuthorByTuple(pages);

            BaseResponseDTO.MetaData metaData = new BaseResponseDTO.MetaData(tuples.getTotalElements(), tuples.getTotalPages(), tuples.getSize());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ResponseUtil.insertSuccessResponseWithMetaData(mapTupleToAuthor(tuples.getContent()), metaData));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.insertFailResponse(e.getMessage()));
        }
    }

    @Override
    public BaseResponseDTO<String> getHtml() throws JsonProcessingException {


        Context context = new Context();
        context.setVariable("message","test");
        String html = engine.process("email",context);
        String s = html.replaceAll("\\r\\n|\\r|\\n", " ");
        String r = s.replace("\\","\"");


        return BaseResponseDTO.<String>builder()
                .data(r)
                .build();
    }

    private AuthorIndexDto mapAuthorIndexDto(Author data) {
        return new AuthorIndexDto(
                data.getId(),
                data.getTitle(),
                data.getFirstName(),
                data.getLastName(),
                data.getBirthDate(),
                data.getDeceasedDate(),
                data.getEducation(),
                data.getSummary(),
                data.getCreatedBy(),
                data.getModifiedBy()
        );
    }

    public void mapInsert(Author author, AuthorInsertDto authorInsertDto) {
        String createdBy = baseController.getCurrentLogin();
        author.setId(authorInsertDto.getId());
        author.setTitle(authorInsertDto.getTitle());
        author.setFirstName(authorInsertDto.getFirstName());
        author.setLastName(authorInsertDto.getLastName());
        author.setBirthDate(authorInsertDto.getBirthDate());
        author.setDeceasedDate(authorInsertDto.getDeceasedDate());
        author.setEducation(authorInsertDto.getEducation());
        author.setSummary(authorInsertDto.getSummary());
        author.setCreatedBy(createdBy == null || createdBy.equalsIgnoreCase(EMPTY_STRING)
                ? EMPTY_STRING : createdBy);
        author.setModifiedBy("");

    }

    private List<AuthorIndexDto> mapTupleToAuthor(List<Tuple> data) {

        return data.stream().map(auth -> new AuthorIndexDto(
                auth.get("id", BigInteger.class).longValue(),
                auth.get("title", String.class),
                auth.get("firstName", String.class),
                auth.get("lastName", String.class),
                LocalDate.parse(auth.get("birthDate", String.class)),
                auth.get("deceasedDate", String.class) != null ? LocalDate.parse(auth.get("deceasedDate", String.class)) : null,
                auth.get("education", String.class),
                auth.get("summary", String.class),
                auth.get("createdBy", String.class),
                auth.get("modifiedBy", String.class)
        )).toList();
    }

    private void updateValidator(Author author) throws JsonProcessingException {
        try {
            Optional<Author> validatedData = authorRepository.findById(author.getId());
            validatedData.ifPresent(value -> author.setModifiedBy(value.getModifiedBy()));

            String dataExisting = objectMapper.writeValueAsString(validatedData);
            String dataUpdate = objectMapper.writeValueAsString(author);

            if (dataExisting.equals(dataUpdate)) {
                throw new CustomException(VALIDATE_UPDATE);
            }
            author.setModifiedBy(baseController.getCurrentLogin());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAccountMapper(Author author, AuthorIndexDto authIndex, AuthorUpdateDto authorInsertDto) {

        author.setId(authorInsertDto.getId());
        author.setTitle(authorInsertDto.getTitle());
        author.setFirstName(authorInsertDto.getFirstName());
        author.setLastName(authorInsertDto.getLastName());
        author.setBirthDate(authorInsertDto.getBirthDate());
        author.setDeceasedDate(authorInsertDto.getDeceasedDate());
        author.setEducation(authorInsertDto.getEducation());
        author.setSummary(authorInsertDto.getSummary());
        author.setCreatedBy(authIndex.getCreatedBy());
        author.setModifiedBy(baseController.getCurrentLogin() == null ? "" : baseController.getCurrentLogin());
    }


}
