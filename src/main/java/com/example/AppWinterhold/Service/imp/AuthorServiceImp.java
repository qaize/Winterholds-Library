package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Controller.Model.BaseController;
import com.example.AppWinterhold.CustomException.CustomException;
import com.example.AppWinterhold.Dao.AuthorRepository;
import com.example.AppWinterhold.Dto.Author.AuthorIndexDto;
import com.example.AppWinterhold.Dto.Author.AuthorIndexDtoV2;
import com.example.AppWinterhold.Dto.Author.AuthorInsertDto;
import com.example.AppWinterhold.Dto.Author.AuthorUpdateDto;
import com.example.AppWinterhold.Dto.BaseResponseDTO;
import com.example.AppWinterhold.Dto.Rest.Request.Author.AuthorRequestDTO;
import com.example.AppWinterhold.Entity.Author;
import com.example.AppWinterhold.Service.abs.AuthorService;
import com.example.AppWinterhold.Utility.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.AppWinterhold.Const.actionConst.*;

@Service
public class AuthorServiceImp implements AuthorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorServiceImp.class);

    private final LogServiceImpl logService;
    private final AuthorRepository authorRepository;
    private final BaseController baseController;


    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public AuthorServiceImp(AuthorRepository authorRepository, BaseController baseController, LogServiceImpl logService, ObjectMapper objectMapper) {
        this.authorRepository = authorRepository;
        this.baseController = baseController;
        this.logService = logService;
        this.objectMapper = objectMapper;
    }


    @Override
    public List<AuthorIndexDto> getListAuthorBySearch(Integer page, String name) throws JsonProcessingException {


        int row = 10;
        Pageable paging = PageRequest.of(page - 1, row, Sort.by("id").descending());

        return authorRepository.getListAuthorBySearch(name, paging);
    }

    @Override
    public List<AuthorIndexDto> getListAuthorBySearchV2(AuthorIndexDtoV2 authorIndexDtoV2) throws JsonProcessingException {

        Pageable paging = PageRequest.of(authorIndexDtoV2.getPage() - 1, authorIndexDtoV2.getDataCount(), Sort.by("id").descending());
        return authorRepository.getListAuthorBySearch(authorIndexDtoV2.getFullname(), paging);
    }

    @Override
    public ResponseEntity<Object> getAllAuthorTuple(AuthorRequestDTO authorRequestDTO) {
        try {
            Pageable paging = PageRequest.of(authorRequestDTO.getPage()-1, authorRequestDTO.getDataCount());
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
        Long totalPage = (long) Math.ceil(totalData / row);

        return totalPage;
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

            UpdateAccountMapper(en,authIndex, dto);
            updateValidator(en);

            authorRepository.save(en);
            logService.saveLogs(AUTHOR, SUCCESS, UPDATE);

        } catch(CustomException e){
            LOGGER.info(e.getMessage());
            logService.saveLogs(AUTHOR, ALREADY, UPDATE);
        } catch (Exception ex) {

            LOGGER.info(ex.getMessage());
            logService.saveLogs(AUTHOR, FAILED, UPDATE);
        }
    }


    @Override
    public AuthorIndexDto getAuthorById(Long id) throws JsonProcessingException {
        Optional<Author> author = authorRepository.findById(id);
        AuthorIndexDto dataAuthor = author.isPresent() ? mapAuthorIndexDto(author.get()) : null;

        LOGGER.info(SUCCESS_GET_DATA, objectMapper.writeValueAsString(dataAuthor));
        return dataAuthor;
    }


    @Override
    public Boolean delete(Long id) {

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
                LOGGER.info(SUCCESS + " ".concat(DELETE));
                return true;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return false;
    }

    @Override
    public AuthorInsertDto getAuthorByIdinsert(Long id) {
        return authorRepository.getAuthorByIdinsert(id);
    }



    @Override
    public Page<AuthorIndexDto> getAllAuthorWithPage() {
        Pageable page = PageRequest.of(1, 5);

        return authorRepository.getAllAuthorWithPage(page);
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

    private AuthorIndexDto mapAuthorIndexDto(Author data) {
        AuthorIndexDto authorIndexDto = new AuthorIndexDto(
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
        return authorIndexDto;
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
    private List<AuthorIndexDto> mapTupleToAuthor(List<Tuple> data){
        List<AuthorIndexDto> authorIndexDtos = data.stream().map(auth -> new AuthorIndexDto(
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
        )).collect(Collectors.toList());

        return authorIndexDtos;
    }

    private void updateValidator(Author author) throws JsonProcessingException {

        Optional<Author> validatedData = authorRepository.findById(author.getId());
        author.setModifiedBy(validatedData.get().getModifiedBy());

        String dataExisting = objectMapper.writeValueAsString(validatedData);
        String dataUpdate = objectMapper.writeValueAsString(author);

        if(dataExisting.equals(dataUpdate)){
            throw new CustomException(VALIDATE_UPDATE);
        }
        author.setModifiedBy(baseController.getCurrentLogin());
    }

    private void UpdateAccountMapper(Author author,AuthorIndexDto authIndex,  AuthorUpdateDto authorInsertDto) {

        author.setId(authorInsertDto.getId());
        author.setTitle(authorInsertDto.getTitle());
        author.setFirstName(authorInsertDto.getFirstName());
        author.setLastName(authorInsertDto.getLastName());
        author.setBirthDate(authorInsertDto.getBirthDate());
        author.setDeceasedDate(authorInsertDto.getDeceasedDate());
        author.setEducation(authorInsertDto.getEducation());
        author.setSummary(authorInsertDto.getSummary());
        author.setCreatedBy(authIndex.getCreatedBy());
        author.setModifiedBy(baseController.getCurrentLogin() == null ? "":baseController.getCurrentLogin());
    }





}
