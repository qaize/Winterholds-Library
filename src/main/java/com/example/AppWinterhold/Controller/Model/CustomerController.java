package com.example.AppWinterhold.Controller.Model;

import com.example.AppWinterhold.Dto.Author.AuthorIndexDto;
import com.example.AppWinterhold.Dto.Author.AuthorInsertDto;
import com.example.AppWinterhold.Dto.Customer.CustomerInsertDto;
import com.example.AppWinterhold.Dto.Customer.CustomerUpdateDto;
import com.example.AppWinterhold.Service.abs.AuthorService;
import com.example.AppWinterhold.Service.abs.CustomerService;
import com.example.AppWinterhold.Service.imp.AccountServiceImp;
import com.example.AppWinterhold.Utility.Dropdown;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/customer")
public class CustomerController {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountServiceImp account;

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(defaultValue = "") String number,
                        @RequestParam(defaultValue = "") String name,
                        @RequestParam(defaultValue = "1") Integer page){

        model.addAttribute("userLogin",account.getCurrentUserLogin());

        model.addAttribute("name",name);
        var listCustomer = customerService.getListCustomerBySearch(page,number,name);
        Long totalPage = customerService.getCountPage(number,name);
        model.addAttribute("listCustomer",listCustomer);
        if(totalPage==0){page=0;}
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPage",totalPage);

        return "Customer/index";
    }

    @GetMapping("/insert")
    public String insert(Model model) {
        CustomerInsertDto dto = new CustomerInsertDto();

        dto.setMembershipExpireDate(LocalDate.now().plusYears(2));
        model.addAttribute("dto", dto);
        model.addAttribute("dropdownGender", Dropdown.dropdownGender());
        return "Customer/insert";
    }

    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("dto") CustomerInsertDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("dto", dto);
            model.addAttribute("dropdownGender", Dropdown.dropdownGender());
            return "Customer/insert";
        } else {

            customerService.insert(dto);
            return "redirect:/customer/index";
        }

    }

    @GetMapping("/update")
    public String update(Model model,@RequestParam(required = false) String number) {
        CustomerInsertDto dto = customerService.getCustomerByMemberInsert(number);
        model.addAttribute("dto", dto);
        model.addAttribute("dropdownGender", Dropdown.dropdownGender());
        return "Customer/update";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("dto") CustomerUpdateDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("dto", dto);
            model.addAttribute("dropdownGender", Dropdown.dropdownGender());
            return "Customer/update";
        } else {

            customerService.update(dto);
            return "redirect:/customer/index";
        }

    }

    @GetMapping("/delete")
    public String delete(@RequestParam(required = true) String number) {

        Boolean result = customerService.delete(number);
        if(!result){
            return "Customer/delete";
        }
        return "redirect:/customer/index";
    }

    @GetMapping("/extend")
    public String extend(@RequestParam(required = true) String number) {
        var data = customerService.getCustomerByMemberInsert(number);
        data.setMembershipExpireDate(data.getMembershipExpireDate().plusYears(2));
        customerService.insert(data);
        return "redirect:/customer/index";
    }

    @GetMapping("/detail")
    public String detail(Model model, @RequestParam(required = true) String number) {
        model.addAttribute("membershipNumber",number);
        var memberDto = customerService.getCustomerByMember(number);
        model.addAttribute("memberDto", memberDto);
        return "Customer/Detail";
    }

}
