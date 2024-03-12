package com.example.winterhold.controller.rest;

import com.example.winterhold.Dto.Account.AccountInsertDto;
import com.example.winterhold.Dto.Account.AccountUpdateDto;
import com.example.winterhold.Dto.Ajax.AjaxResponseBodyDto;
import com.example.winterhold.Dto.Ajax.ErrorValidationDto;
import com.example.winterhold.Dto.Rest.*;
import com.example.winterhold.Entity.Mail;
import com.example.winterhold.Service.abs.AccountService;
import com.example.winterhold.Service.abs.EmailService;
import com.example.winterhold.Utility.JwtToken;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.example.winterhold.constants.enumList.INTERNAL_SERVER_ERROR;
import static com.example.winterhold.constants.enumList.RUNTIME_ERROR_SERVER;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtToken jwtToken;
    private final EmailService emailService;
    private final AccountService accountService;

    @PostMapping("/authenticate")
    public ResponseCrudRestDto post(@RequestBody RequestRestDto dto) {
        ResponseCrudRestDto responseCrudRestDto = new ResponseCrudRestDto();

        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    dto.getUsername(),
                    dto.getPassword());
            authenticationManager.authenticate(token);
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            String generatedToken = jwtToken.generateToken(dto.getSubject(), dto.getSecretKey(), dto.getAudience(), dto.getUsername());
            var response = new ResponseRestDto(dto.getUsername(), generatedToken);
            responseCrudRestDto.setMessage("Success");
            responseCrudRestDto.setStatus(HttpStatus.OK);
            responseCrudRestDto.setObject(response);

        } catch (Exception ex) {
            responseCrudRestDto.setMessage("ERROR ON SERVER");
            responseCrudRestDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseCrudRestDto.setObject(null);
        }

        return responseCrudRestDto;
    }

    @GetMapping("/getAccount")
    @ApiOperation(value = "Get list Account ", nickname = "get List Account")
    public ResponseEntity<Object> getAccount() {
        try {
            var list = accountService.getlist();
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RUNTIME_ERROR_SERVER.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestParam String username) {
        try {
            var list = accountService.getAccountByUsername(username);
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RUNTIME_ERROR_SERVER.getMessage());
        }
    }


    @PostMapping
    public ResponseEntity<Object> post(@Valid @RequestBody AccountInsertDto dto, BindingResult bindingResult) {
        try {
            if (!bindingResult.hasErrors()) {
                accountService.insert(dto);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseCrudRestDto(HttpStatus.OK, "Success Insert", dto));
            } else {
                List<ValidatorRestDto> list = new ArrayList<>();
                List<FieldError> errorList = bindingResult.getFieldErrors();
                List<ObjectError> objectErrorList = bindingResult.getGlobalErrors();
                for (FieldError err : errorList
                ) {
                    var data = new ValidatorRestDto(err.getField(), err.getDefaultMessage());
                    list.add(data);
                }

                for (ObjectError err : objectErrorList
                ) {
                    var data = new ValidatorRestDto(err.getObjectName(), err.getDefaultMessage());
                    list.add(data);
                }

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InsertFailedDto(HttpStatus.UNPROCESSABLE_ENTITY, "Validate Error", list));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(INTERNAL_SERVER_ERROR.getMessage());
        }

    }

    @PutMapping()
    public ResponseCrudRestDto put(@Valid @RequestBody AccountInsertDto dto, BindingResult bindingResult) {

        try {
            if (!bindingResult.hasErrors()) {
                accountService.insert(dto);
                return new ResponseCrudRestDto(HttpStatus.OK, "Success Update", dto);
            } else {
                List<ValidatorRestDto> list = new ArrayList<>();
                List<FieldError> errorList = bindingResult.getFieldErrors();
                List<ObjectError> objectErrorList = bindingResult.getGlobalErrors();
                for (FieldError err : errorList
                ) {
                    var data = new ValidatorRestDto(err.getField(), err.getDefaultMessage());
                    list.add(data);
                }

                for (ObjectError err : objectErrorList
                ) {
                    var data = new ValidatorRestDto(err.getObjectName(), err.getDefaultMessage());
                    list.add(data);
                }
                return new ResponseCrudRestDto(HttpStatus.UNPROCESSABLE_ENTITY, "Validation Failed", list);

            }

        } catch (Exception e) {
            return new ResponseCrudRestDto(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getMessage(), dto);
        }
    }

    @DeleteMapping("/deleteAccount={username}")
    public ResponseCrudRestDto delete(@PathVariable String username) {

        try {
            accountService.delete(username);
            return new ResponseCrudRestDto(HttpStatus.OK, "Username " + username + " Deleted");

        } catch (Exception e) {
            return new ResponseCrudRestDto(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getMessage());
        }
    }


    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody AccountUpdateDto dto,
                                            BindingResult bindingResult) {
        AjaxResponseBodyDto result;

        try {
            if (!bindingResult.hasErrors()) {
                accountService.update(dto);
                result = new AjaxResponseBodyDto(true, HttpStatus.OK, "Change Password Success");
                return ResponseEntity.ok(result);
            } else {
                List<ErrorValidationDto> listError = new LinkedList<>();
                bindingResult.getAllErrors().forEach(
                        data -> {
                            if (data instanceof FieldError fe) {
                                listError.add(new ErrorValidationDto(fe.getField(), data.getDefaultMessage()));
                            } else {
                                var argument = data.getArguments();
                                var msg = Objects.requireNonNull(data.getDefaultMessage()).split("_");
                                listError.add(new ErrorValidationDto(Objects.requireNonNull(argument)[2].toString(), msg[0]));
                            }
                        }
                );
                result = new AjaxResponseBodyDto(false, HttpStatus.UNPROCESSABLE_ENTITY, "Validate Failed", listError);
                return ResponseEntity.ok(result);
            }
        } catch (Exception ex) {
            result = new AjaxResponseBodyDto(false, HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }

    @PostMapping("/createAccount")
    public ResponseEntity<?> insertAccount(@Valid @RequestBody AccountInsertDto dto,
                                           BindingResult bindingResult) {
        AjaxResponseBodyDto result;
        try {
            if (!bindingResult.hasErrors()) {
                Mail em = new Mail();
                em.setRecipient("ikhwani.syahbana015@gmail.com");
                em.setSubject("Password Anda");
                em.setMsgBody(dto.getPassword());
                emailService.sendSimpleMail(em);
                accountService.insert(dto);
                result = new AjaxResponseBodyDto(true, HttpStatus.OK, "Insert Account Success");
                return ResponseEntity.ok(result);
            } else {
                List<ErrorValidationDto> listError = new LinkedList<>();
                bindingResult.getAllErrors().forEach(
                        data -> {
                            if (data instanceof FieldError fe) {
                                listError.add(new ErrorValidationDto(fe.getField(), data.getDefaultMessage()));
                            } else {
                                var argument = data.getArguments();
                                var msg = Objects.requireNonNull(data.getDefaultMessage()).split("_");
                                listError.add(new ErrorValidationDto(Objects.requireNonNull(argument)[2].toString(), msg[0]));
                            }
                        }
                );
                result = new AjaxResponseBodyDto(false, HttpStatus.UNPROCESSABLE_ENTITY, "Validate Failed", listError);
                return ResponseEntity.ok(result);
            }
        } catch (Exception ex) {
            result = new AjaxResponseBodyDto(false, HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getMessage());

            return ResponseEntity.internalServerError().body(result);
        }
    }

}
