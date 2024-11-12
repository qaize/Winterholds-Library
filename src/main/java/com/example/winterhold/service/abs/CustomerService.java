package com.example.winterhold.service.abs;

import com.example.winterhold.dto.customer.CustomerIndexDto;
import com.example.winterhold.dto.customer.CustomerInsertDto;
import com.example.winterhold.dto.customer.CustomerProfileDto;
import com.example.winterhold.dto.customer.CustomerUpdateDto;
import com.example.winterhold.dto.models.DataDTO;
import com.example.winterhold.entity.Customer;

import java.text.ParseException;
import java.util.List;

public interface CustomerService {
    DataDTO<List<CustomerIndexDto>> getListCustomerBySearch(Integer page, String number, String name);

    Boolean delete(String number);
    List<CustomerIndexDto> getAll();
    String customerNumberGenerator();
    void update(CustomerUpdateDto dto);
    void insert(CustomerInsertDto dto);
    Boolean CustomerMemberChecker(String s);
    void updateWithEntity(Customer customer);
    boolean doBanCustomer(String customerNumber);
    List<CustomerIndexDto> getAvaliableCustomer();
    Long getCountPage(String number, String name);
    DataDTO<List<Customer>> getBannedCustomerlist(Integer page);
    Customer getCustomerByEntity(String customerNumber);
    CustomerIndexDto getCustomerByMember(String customerNumber);
    CustomerUpdateDto getCustomerByMemberInsert(String number);

    List<CustomerIndexDto> getAvaliableCustomerEdit(String customerNumber);
    void doUnbanCustomer(String customerNumber);
    Boolean doExtendMember(String number);

    CustomerProfileDto cutomerProfile(String username) throws ParseException;
}
