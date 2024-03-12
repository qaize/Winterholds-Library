package com.example.winterhold.Controller.Model;

import com.example.winterhold.Dto.CurrentLoginDetailDTO;
import com.example.winterhold.Dto.Customer.CustomerIndexDto;
import com.example.winterhold.Dto.Customer.CustomerInsertDto;
import com.example.winterhold.Dto.Customer.CustomerUpdateDto;
import com.example.winterhold.Dto.Models.DataDTO;
import com.example.winterhold.Entity.Customer;
import com.example.winterhold.Service.abs.CustomerService;
import com.example.winterhold.Service.imp.AccountServiceImp;
import com.example.winterhold.Service.imp.LoanServiceImp;
import com.example.winterhold.Utility.Dropdown;
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

    @Autowired
    private LoanServiceImp loanServiceImp;

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(defaultValue = "") String number,
                        @RequestParam(defaultValue = "") String name,
                        @RequestParam(defaultValue = "1") Integer page) {
        DataDTO<List<CustomerIndexDto>> data = customerService.getListCustomerBySearch(page, number, name);

        model.addAttribute("name", name);
        model.addAttribute("number", number);
        model.addAttribute("flag", data.getFlag());
        model.addAttribute("message", data.getMessage());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", data.getTotalPage());
        model.addAttribute("listCustomer", data.getData());
        model.addAttribute("newNotification",loanServiceImp.getNotification());

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
        model.addAttribute("profile", true);
        model.addAttribute("dropdownGender", Dropdown.dropdownGender());

        return "Customer/update";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("dto") CustomerUpdateDto dto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("dto", dto);
            model.addAttribute("profile", true);
            model.addAttribute("dropdownGender", Dropdown.dropdownGender());

            return "Customer/update";
        } else {

            customerService.update(dto);
            return "redirect:/customer/index";
        }

    }

    @GetMapping("/delete")
    public String delete(@RequestParam(required = true) String number) {
        return customerService.delete(number) ? "redirect:/customer/index" : "Customer/delete";
    }

    @GetMapping("/extend")
    public String extend(@RequestParam(required = true) String number) {
        return customerService.doExtendMember(number) ? "redirect:/customer/index" : "redirect:/customer/index";
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

        model.addAttribute("validationHeader", "Sorry you can't ban customer during loan");
        model.addAttribute("validationReason", "Please, complete the loan session first!");
        model.addAttribute("flag", 1);

        return customerService.doBanCustomer(customerNumber) ? "redirect:/customer/index" : "Customer/valid";
    }

    @GetMapping("/unban")
    public String unban(Model model, @RequestParam String customerNumber) {
        int flag = 1;
        customerService.doUnbanCustomer(customerNumber);

        model.addAttribute("validationHeader", "Success unban : " + customerNumber);
        model.addAttribute("validationReason", "Please, be kind!");
        model.addAttribute("flag", flag);

        return "Customer/valid";
    }

    @GetMapping("/banned-customer")
    public String bannedlist(Model model, @RequestParam(defaultValue = "1") Integer page) {

        DataDTO<List<Customer>> data = customerService.getBannedCustomerlist(page);

        model.addAttribute("totalPage", data.getTotalPage());
        model.addAttribute("currentPage", page);
        model.addAttribute("bannedList", data.getData());

        return "Customer/bannedList";
    }


    @GetMapping("/update-profile")
    public String updateProfile(Model model) {
        BaseController baseController = new BaseController();
        CurrentLoginDetailDTO currentLogin = baseController.getCurrentLoginDetail();

        CustomerUpdateDto dto = customerService.getCustomerByMemberInsert(currentLogin.getUsername());

        model.addAttribute("dto", dto);
        model.addAttribute("profile", false);
        model.addAttribute("dropdownGender", Dropdown.dropdownGender());

        return "Customer/update";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@Valid @ModelAttribute("dto") CustomerUpdateDto dto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("dto", dto);
            model.addAttribute("profile", false);
            model.addAttribute("dropdownGender", Dropdown.dropdownGender());

            return "Customer/update";
        } else {
            customerService.update(dto);
            return "redirect:/customer/detail?number="+dto.getMembershipNumber();
        }

    }


    @GetMapping("/detail-profile")
    public String detail(Model model) {

        BaseController baseController = new BaseController();
        CurrentLoginDetailDTO currentLogin = baseController.getCurrentLoginDetail();

        var memberDto = customerService.getCustomerByMember(currentLogin.getUsername());

        model.addAttribute("membershipNumber", currentLogin.getRole());
        model.addAttribute("memberDto", memberDto);

        return "Customer/Detail";
    }
}
