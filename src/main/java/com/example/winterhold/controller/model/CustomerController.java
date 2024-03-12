package com.example.winterhold.controller.model;

import com.example.winterhold.Dto.CurrentLoginDetailDTO;
import com.example.winterhold.Dto.Customer.CustomerIndexDto;
import com.example.winterhold.Dto.Customer.CustomerInsertDto;
import com.example.winterhold.Dto.Customer.CustomerUpdateDto;
import com.example.winterhold.Dto.Models.DataDTO;
import com.example.winterhold.Entity.Customer;
import com.example.winterhold.Service.abs.CustomerService;
import com.example.winterhold.Service.imp.LoanServiceImp;
import com.example.winterhold.Utility.Dropdown;
import com.example.winterhold.constants.MvcRedirectConst;
import com.example.winterhold.constants.WinterholdConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    
    private final LoanServiceImp loanServiceImp;

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
        model.addAttribute(WinterholdConstants.CONTROLLER_DROPDOWN_GENDER, Dropdown.dropdownGender());

        return "Customer/insert";
    }

    @PostMapping("/insert")
    public String insert(@Valid @ModelAttribute("dto") CustomerInsertDto dto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("dto", dto);
            model.addAttribute(WinterholdConstants.CONTROLLER_DROPDOWN_GENDER, Dropdown.dropdownGender());

            return "Customer/insert";
        } else {

            customerService.insert(dto);
            return MvcRedirectConst.REDIRECT_CUSTOMER_INDEX;
        }
    }

    @GetMapping("/update")
    public String update(Model model, @RequestParam(required = false) String number) {

        CustomerUpdateDto dto = customerService.getCustomerByMemberInsert(number);

        model.addAttribute("dto", dto);
        model.addAttribute("profile", true);
        model.addAttribute(WinterholdConstants.CONTROLLER_DROPDOWN_GENDER, Dropdown.dropdownGender());

        return "Customer/update";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("dto") CustomerUpdateDto dto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("dto", dto);
            model.addAttribute("profile", true);
            model.addAttribute(WinterholdConstants.CONTROLLER_DROPDOWN_GENDER, Dropdown.dropdownGender());

            return "Customer/update";
        } else {

            customerService.update(dto);
            return MvcRedirectConst.REDIRECT_CUSTOMER_INDEX;
        }

    }

    @GetMapping("/delete")
    public String delete(@RequestParam String number) {
        return Boolean.TRUE.equals(customerService.delete(number)) ? MvcRedirectConst.REDIRECT_CUSTOMER_INDEX : "Customer/delete";
    }

    @GetMapping("/extend")
    public String extend(@RequestParam String membershipNumber) {
        //TODO please adjust return for failed extends
        return Boolean.TRUE.equals(customerService.doExtendMember(membershipNumber)) ? MvcRedirectConst.REDIRECT_CUSTOMER_INDEX : MvcRedirectConst.REDIRECT_CUSTOMER_INDEX;
    }

    @GetMapping("/detail")
    public String detail(Model model, @RequestParam String number) {

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

        return customerService.doBanCustomer(customerNumber) ? MvcRedirectConst.REDIRECT_CUSTOMER_INDEX : "Customer/valid";
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
    public String bannedList(Model model, @RequestParam(defaultValue = "1") Integer page) {

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
        model.addAttribute(WinterholdConstants.CONTROLLER_DROPDOWN_GENDER, Dropdown.dropdownGender());

        return "Customer/update";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@Valid @ModelAttribute("dto") CustomerUpdateDto dto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("dto", dto);
            model.addAttribute("profile", false);
            model.addAttribute(WinterholdConstants.CONTROLLER_DROPDOWN_GENDER, Dropdown.dropdownGender());

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
