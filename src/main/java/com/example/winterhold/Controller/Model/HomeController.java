package com.example.winterhold.Controller.Model;


import com.example.winterhold.Service.imp.AccountServiceImp;
import com.example.winterhold.Service.imp.LoanServiceImp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    final
    AccountServiceImp account;

    final
    LoanServiceImp loanServiceImp;

    public HomeController(AccountServiceImp account, LoanServiceImp loanServiceImp) {
        this.account = account;
        this.loanServiceImp = loanServiceImp;
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("newNotification",loanServiceImp.getNotification());
        return "Home/index";
    }
}
