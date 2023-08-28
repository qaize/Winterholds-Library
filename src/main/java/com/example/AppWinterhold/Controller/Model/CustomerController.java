package com.example.AppWinterhold.Controller.Model;

import com.example.AppWinterhold.Dto.Customer.CustomerInsertDto;
import com.example.AppWinterhold.Dto.Customer.CustomerUpdateDto;
import com.example.AppWinterhold.Entity.Customer;
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
import java.util.List;

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
                        @RequestParam(defaultValue = "1") Integer page) {

        var listCustomer = customerService.getListCustomerBySearch(page, number, name);
        Long totalPage = customerService.getCountPage(number, name);
        if (totalPage == 0) {
            page = 0;
        }

        model.addAttribute("name", name);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("listCustomer", listCustomer);
        model.addAttribute("userLogin", account.getCurrentUserLogin());

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
    public String update(Model model, @RequestParam(required = false) String number) {

        CustomerUpdateDto dto = customerService.getCustomerByMemberInsert(number);

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
        if (!result) {
            return "Customer/delete";
        }
        return "redirect:/customer/index";
    }

    @GetMapping("/extend")
    public String extend(@RequestParam(required = true) String number) {

        var data = customerService.getCustomerByMemberInsert(number);
        data.setMembershipExpireDate(data.getMembershipExpireDate().plusYears(2));
        customerService.update(data);

        return "redirect:/customer/index";
    }

    @GetMapping("/detail")
    public String detail(Model model, @RequestParam(required = true) String number) {

        var memberDto = customerService.getCustomerByMember(number);

        model.addAttribute("membershipNumber", number);
        model.addAttribute("memberDto", memberDto);

        return "Customer/Detail";
    }

    @GetMapping("/valid")
    public String valid(Model model, @RequestParam String customerNumber) {

        int flag = 0;
        model.addAttribute("validationHeader", "Are you sure want to ban : " + customerNumber + " ?");
        model.addAttribute("validationReason", "cancel this by clicking back button!");
        model.addAttribute("membershipNumber", customerNumber);
        model.addAttribute("flag", flag);

        return "Customer/valid";

    }

    @GetMapping("/ban")
    public String ban(Model model, @RequestParam String customerNumber) {



        if (customerService.doBanCustomer(customerNumber)) {

            return "redirect:/customer/index";
        } else {

            int flag = 1;
            model.addAttribute("validationHeader", "Sorry you can't ban customer during loan");
            model.addAttribute("validationReason", "Please, complete the loan session first!");
            model.addAttribute("flag", flag);
            return "Customer/valid";
        }
    }

    @GetMapping("/unban")
    public String unban(Model model, @RequestParam String customerNumber) {
            int flag = 1;
            customerService.doUnbanCustomer(customerNumber);

            model.addAttribute("validationHeader", "Success unban : "+customerNumber );
            model.addAttribute("validationReason", "Please, be kind!");
            model.addAttribute("flag", flag);

            return "Customer/valid";
    }

    @GetMapping("/banned-customer")
    public String bannedlist(Model model, @RequestParam(defaultValue = "1") Integer page) {

        List<Customer> customers = customerService.getBannedCustomerlist(page);
        Long totalPage = customerService.getCountBannedList();
        if (totalPage == 0) {
            page = 0;
        }

        model.addAttribute("totalPage", totalPage);
        model.addAttribute("page", page);
        model.addAttribute("bannedList", customers);

        return "Customer/bannedList";
    }
}
