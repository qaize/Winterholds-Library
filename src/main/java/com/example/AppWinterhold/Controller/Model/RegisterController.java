package com.example.AppWinterhold.Controller.Model;

import com.example.AppWinterhold.Dto.Account.AccountInsertDto;
import com.example.AppWinterhold.Dto.Ajax.AjaxResponseBodyDto;
import com.example.AppWinterhold.Dto.Ajax.ErrorValidationDto;
import com.example.AppWinterhold.Service.abs.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/register")
public class RegisterController extends BaseController{

    @Autowired
    private AccountService accountService;

    @GetMapping("/registerForm")
    public String signup(Model model){
        AccountInsertDto Accountdto = new AccountInsertDto();
        model.addAttribute("dto", Accountdto);
        return "Register/registerForm";
    }

    @PostMapping("/registerForm")
    public String signup(@Valid @ModelAttribute("dto") AccountInsertDto dto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("dto", dto);
            return "Register/registerForm";
        }
        else{
            accountService.insert(dto);
            return "redirect:/login/loginForm";
        }
    }

    @GetMapping("/popup")
    public String popup(Model model)
    {
        String token = getToken();
        model.addAttribute("token",token);
        return "Register/profile";
    }

}
